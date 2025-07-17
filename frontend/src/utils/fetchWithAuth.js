// fetchWithAuth.js

let isRefreshing = false; // 토큰 재발급이 현재 진행 중인지 여부를 나타내는 플래그
let failedQueue = []; // 재발급 대기 중인(401 응답을 받은) 원본 요청들을 저장하는 큐

const addRequestToQueue = (originalRequest) => {
  return new Promise(resolve => {
    failedQueue.push({ originalRequest, resolve });
  });
};

const processQueue = (error, newAccessToken) => {
  failedQueue.forEach(promise => {
    if (error) {
      promise.resolve(Promise.reject(error));
    } else {
      const updatedOptions = {
        ...promise.originalRequest.options,
        headers: {
          ...(promise.originalRequest.options.headers || {}),
          Authorization: `Bearer ${newAccessToken}`,
        },
      };
      // 큐에 있던 요청도 전체 URL로 재요청합니다.
      const fullUrl = `${process.env.REACT_APP_API_URL}${promise.originalRequest.url}`;
      promise.resolve(fetch(fullUrl, updatedOptions));
    }
  });
  failedQueue = [];
};

export const fetchWithAuth = async (url, options = {}) => {
  const baseURL = process.env.REACT_APP_API_URL;
  const fullUrl = `${baseURL}${url}`; // 요청 URL을 전체 경로로 만듭니다.

  const accessToken = localStorage.getItem('token');
  const refreshToken = localStorage.getItem('refresh_token');

  if (accessToken) {
    options.headers = {
      ...(options.headers || {}),
      Authorization: `Bearer ${accessToken}`,
    };
  }

  let res = await fetch(fullUrl, options); // 수정된 전체 URL로 요청합니다.

  if (res.status === 401 && refreshToken) {
    if (isRefreshing) {
      console.warn('토큰 재발급이 이미 진행 중입니다. 현재 요청을 큐에 추가합니다:', url);
      // 큐에 추가할 때는 상대 경로 URL을 그대로 유지합니다.
      return addRequestToQueue({ url, options });
    }

    isRefreshing = true;
    console.log('액세스 토큰 만료. 리프레시 토큰 재발급을 시도합니다...');

    try {
      // 토큰 재발급 요청 URL도 전체 경로로 수정합니다.
      const refreshRes = await fetch(`${baseURL}/authentication/reissue-refresh-token?refreshToken=${refreshToken}`, {
        method: 'POST',
      });

      if (refreshRes.ok) {
        const data = await refreshRes.json();
        const newAccessToken = data.ACCESS_TOKEN;
        const newRefreshToken = data.REFRESH_TOKEN;

        localStorage.setItem('token', newAccessToken);
        localStorage.setItem('refresh_token', newRefreshToken);
        console.log('✅ 새로운 AccessToken 및 RefreshToken 발급 성공.');

        isRefreshing = false;
        processQueue(null, newAccessToken);

        const retryOptions = {
          ...options,
          headers: {
            ...(options.headers || {}),
            Authorization: `Bearer ${newAccessToken}`,
          },
        };
        // 재시도 요청도 전체 URL로 보냅니다.
        res = await fetch(fullUrl, retryOptions);
      } else {
        console.error('❌ RefreshToken 재발급 실패:', refreshRes.status, await refreshRes.text());
        isRefreshing = false;
        const error = new Error('RefreshToken 재발급 실패');
        processQueue(error);
        localStorage.clear();
        window.location.href = '/login';
        throw error;
      }
    } catch (refreshError) {
      console.error('🚨 RefreshToken 재발급 중 예외 발생:', refreshError);
      isRefreshing = false;
      processQueue(refreshError);
      localStorage.clear();
      window.location.href = '/login';
      throw refreshError;
    }
  } else if (res.status === 401 && !refreshToken) {
    console.warn('AccessToken 만료. 하지만 RefreshToken이 없습니다. 로그인 페이지로 이동합니다.');
    localStorage.clear();
    window.location.href = '/login';
    throw new Error('인증 토큰 없음. 재로그인 필요.');
  }

  return res;
};

// 아래 함수들은 fetchWithAuth를 사용하므로 별도 수정이 필요 없습니다.
export const toggleFavoritePassage = async (passageNo, isFavorite) => {
  try {
    const res = await fetchWithAuth('/passage/change-favorite-state', {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ passageNo, isFavorite }),
    });

    if (!res.ok) throw new Error('서버 응답 실패');

    const data = await res.json();
    console.log('✅ 즐겨찾기 변경 완료:', data);
    return true;
  } catch (err) {
    console.error('❌ 즐겨찾기 변경 실패:', err);
    return false;
  }
};

export const fetchFavoritePassageList = async () => {
  const res = await fetchWithAuth('/passage/get-favorite-passage-list');
  if (!res.ok) throw new Error('즐겨찾기 목록 실패');
  return res.json();
};

export default fetchWithAuth;
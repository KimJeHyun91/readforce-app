import axios from 'axios';

const api = axios.create({
  baseURL: 'https://readforce-app-backend-production.up.railway.app',
  headers: {
    'Content-Type': 'application/json',
  },
});

// 토큰 설정
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token && token !== 'null' && token !== 'undefined') {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, newAccessToken = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(newAccessToken);
    }
  });
  failedQueue = [];
};

// 🔐 로그인 리디렉션 제외 대상 API 목록
const skipRedirectUrls = [
  '/ranking/get-ranking-list',
  '/learning/get-most-incorrect-questions',
];

api.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      const refreshToken = localStorage.getItem('refresh_token');

      const shouldSkipRedirect = skipRedirectUrls.some(url =>
        originalRequest.url.includes(url)
      );

      // Refresh token 없을 때
      if (!refreshToken || refreshToken === 'null' || refreshToken === 'undefined') {
        if (!shouldSkipRedirect) {
          localStorage.clear();
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({
            resolve: (token) => {
              originalRequest.headers.Authorization = `Bearer ${token}`;
              resolve(api(originalRequest));
            },
            reject: (err) => reject(err),
          });
        });
      }

      isRefreshing = true;

      try {
        const res = await axios.post(
          `/authentication/reissue-refresh-token?refreshToken=${encodeURIComponent(refreshToken)}`
        );

        const { ACCESS_TOKEN, REFRESH_TOKEN } = res.data;

        localStorage.setItem('token', ACCESS_TOKEN);
        localStorage.setItem('refresh_token', REFRESH_TOKEN);

        api.defaults.headers.Authorization = `Bearer ${ACCESS_TOKEN}`;
        originalRequest.headers.Authorization = `Bearer ${ACCESS_TOKEN}`;

        processQueue(null, ACCESS_TOKEN);

        return api(originalRequest);
      } catch (err) {
        processQueue(err, null);

        if (!shouldSkipRedirect) {
          localStorage.clear();
          window.location.href = '/login';
        }

        return Promise.reject(err);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default api;

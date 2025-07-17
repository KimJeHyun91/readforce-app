# 1단계: Node.js 환경에서 React 앱 빌드
FROM node:18-alpine as builder
WORKDIR /app

# package.json과 lock 파일을 먼저 복사하여 종속성 캐싱 활용
COPY package.json package-lock.json ./
RUN npm install

# 나머지 소스 코드 복사
COPY . .

# 빌드 시점에 환경 변수를 전달하여 빌드 실행
ARG REACT_APP_API_URL
RUN REACT_APP_API_URL=$REACT_APP_API_URL npm run build

# 2단계: Nginx 환경에서 정적 파일 서빙
FROM nginx:stable-alpine

# 빌드된 결과물을 Nginx의 기본 웹 루트 디렉토리로 복사
COPY --from=builder /app/build /usr/share/nginx/html

# 프로젝트의 nginx.conf 파일을 컨테이너의 Nginx 설정 파일로 덮어쓰기
# (이 부분이 404 오류를 해결하는 핵심입니다)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 80번 포트 개방
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
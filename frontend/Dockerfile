# Dockerfile
# 1. 빌드 환경: Node.js 이미지를 사용하여 React 앱을 빌드
FROM node:18-alpine as builder
WORKDIR /app
COPY package.json .
COPY yarn.lock .
RUN yarn install
COPY . .
# 환경 변수를 참조하여 빌드
ARG REACT_APP_API_URL
ENV REACT_APP_API_URL=$REACT_APP_API_URL
RUN yarn build

# 2. 실행 환경: Nginx 이미지를 사용하여 정적 파일을 서빙
FROM nginx:stable-alpine
# 빌드된 결과물을 Nginx의 기본 HTML 폴더로 복사
COPY --from=builder /app/build /usr/share/nginx/html
# 외부 요청을 컨테이너의 80번 포트로 전달
EXPOSE 80
# Nginx를 포그라운드에서 실행
CMD ["nginx", "-g", "daemon off;"]
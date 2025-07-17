# 1. 빌드 환경: Node.js 이미지를 사용하여 React 앱을 빌드
FROM node:18-alpine as builder
WORKDIR /app

# package.json과 package-lock.json 파일을 복사
COPY package.json .
COPY package-lock.json .

# npm을 사용하여 의존성 설치
RUN npm install

COPY . .

# npm을 사용하여 빌드
RUN npm run build

# 2. 실행 환경: Nginx 이미지를 사용하여 정적 파일을 서빙
FROM nginx:stable-alpine
# 빌드된 결과물을 Nginx의 기본 HTML 폴더로 복사
COPY --from=builder /app/build /usr/share/nginx/html
# 외부 요청을 컨테이너의 80번 포트로 전달
EXPOSE 80
# Nginx를 포그라운드에서 실행
CMD ["nginx", "-g", "daemon off;"]
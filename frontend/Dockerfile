# Cache bust: 2025-07-17 12:18

# 1. 빌드 환경: Node.js 이미지를 사용하여 React 앱을 빌드
FROM node:18-alpine as builder
WORKDIR /app

COPY package.json .
COPY package-lock.json .
RUN npm install
COPY . .

# =================================================================
# 이 부분을 추가/수정합니다.
# Railway로부터 빌드 시점에 사용할 인자(ARG)를 받겠다고 선언합니다.
ARG REACT_APP_API_URL
# npm run build 명령어를 실행할 때, 위에서 받은 인자를 환경변수로 설정합니다.
# =================================================================
RUN REACT_APP_API_URL=$REACT_APP_API_URL npm run build

# 2. 실행 환경: Nginx 이미지를 사용하여 정적 파일을 서빙
FROM nginx:stable-alpine
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
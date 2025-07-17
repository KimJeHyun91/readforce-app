# Cache bust: 2025-07-17 12:18

# 1. 빌드 환경: Node.js 이미지를 사용하여 React 앱을 빌드
FROM node:18-alpine as builder
WORKDIR /app

COPY package.json .
COPY package-lock.json .
RUN npm install
COPY . .

ARG REACT_APP_API_URL
RUN REACT_APP_API_URL=$REACT_APP_API_URL npm run build

# 2. 실행 환경: Nginx 이미지를 사용하여 정적 파일을 서빙
FROM nginx:stable-alpine
COPY --from=builder /app/build /usr/share/nginx/html

# =================================================================
# 이 두 줄을 추가/수정합니다.
# 프로젝트 루트에 있는 custom nginx.conf 파일을 컨테이너 내 Nginx 설정으로 복사
COPY nginx.conf /etc/nginx/conf.d/default.conf 
# =================================================================

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
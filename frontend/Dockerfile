# 1. 빌드 환경
FROM node:18-alpine as builder
WORKDIR /app
COPY package.json .
COPY package-lock.json .
RUN npm install
COPY . .
RUN npm run build

# 2. 실행 환경
FROM nginx:stable-alpine
COPY --from=builder /app/build /usr/share/nginx/html

# ★★★ 이 줄이 정확히 있는지 확인하세요 ★★★
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

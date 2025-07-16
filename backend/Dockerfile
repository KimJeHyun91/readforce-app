# Dockerfile
# 1. 빌드 환경 구성
FROM openjdk:17-jdk-slim as builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# 2. 실행 환경 구성
FROM openjdk:17-jdk-slim
WORKDIR /app
# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=builder /app/build/libs/*.jar app.jar
# 컨테이너가 시작될 때 JAR 파일을 실행
ENTRYPOINT ["java","-jar","app.jar"]
# openjdk:14 이후부턴 xargs가 없기 때문에 문제가 됩니다. 따라서 알파인 버전같은 걸 사용
FROM openjdk:21-jdk-slim as stage1

WORKDIR /statistics

# gradle 빌드시에 필요한 파일들을 workdir로 copy
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Gradle Wrapper 스크립트에 실행 권한을 부여
RUN chmod +x ./gradlew

RUN ./gradlew bootJar

FROM openjdk:21
WORKDIR /statistics

# 빌드된 JAR 파일을 복사
COPY --from=stage1 /statistics/build/libs/*.jar statistics.jar

# 애플리케이션 실행
ENTRYPOINT [ "java", "-jar", "statistics.jar" ]

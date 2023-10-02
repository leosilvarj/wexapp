FROM amd64/gradle:jdk-alpine AS builder

WORKDIR /app

COPY ../build.gradle .

COPY ../settings.gradle .

COPY src ./src/

RUN gradle build --stacktrace -Pprod -x test

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/wexapp-0.0.1-SNAPSHOT.jar ./wexapp.jar

EXPOSE 8080

CMD ["java", "-jar", "wexapp.jar"]

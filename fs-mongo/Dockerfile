FROM openjdk:8-jdk-alpine
LABEL maintainer="daoyuan0626@gmail.com"

VOLUME /tmp
WORKDIR /app

RUN echo "Asia/Shanghai" > /etc/timezone && mkdir logs
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-Xms128m", "-Xmx128m", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/app/logs/", \
"-XX:+PrintGCDetails", "-XX:+PrintGCDateStamps", "-XX:+PrintHeapAtGC", "-Xloggc:/app/logs/gc.log", \
"-jar", "app.jar"]
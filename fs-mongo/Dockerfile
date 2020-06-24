FROM openjdk:8-jdk-alpine
VOLUME /tmp

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar

RUN echo "Asia/Shanghai" > /etc/timezone
RUN mkdir --parents /logs/

ENTRYPOINT ["java", "-Xms128m", "-Xmx128m", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/logs/", \
    "-XX:+PrintGCDetails", "-XX:+PrintGCDateStamps", "-XX:+PrintHeapAtGC", "-Xloggc:/logs/gc.log", \
    "-jar", "/app/app.jar"]
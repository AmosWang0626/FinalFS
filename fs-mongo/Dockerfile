FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

RUN echo "Asia/Shanghai" > /etc/timezone
RUN mkdir --parents /log/

ENTRYPOINT ["java", "-Xms128m", "-Xmx128m", \
    "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/log/", \
    "-XX:+PrintGCDetails", "-XX:+PrintGCDateStamps", "-XX:+PrintHeapAtGC", "-Xloggc:/log/gc.log", \
    "-cp", "app:app/lib/*", "com.amos.fs.mongo.MongoFsApplication"]
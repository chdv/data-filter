FROM ${pmh.container.image.base}
EXPOSE 8080

ARG jar
COPY $jar app.jar

RUN touch /app.jar && chmod 0644 /app.jar

ENV WAITFOR=""
ENV JAVA_BINARY="java"
ENV JAVA_OPTS=""
ENV JAVA_RUN="-Djava.security.egd=file:/dev/./urandom -jar /app.jar"

ENTRYPOINT ["bash", "-c", "$JAVA_BINARY $JAVA_OPTS $JAVA_RUN"]

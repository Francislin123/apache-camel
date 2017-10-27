FROM openjdk:8-jre-alpine

COPY docker/start.sh /usr/bin/start.sh

ADD ./build/libs/feeds-admin-api-*.jar /app/libs/

WORKDIR /app

RUN apk update && apk add libstdc++ curl grep && \
    mkdir -p /app/log && \
    mkdir -p /app/config && \
    mkdir -p /walmart && \
    mv -f /app/libs/*.jar /app && \
    adduser -S feeds-admin-api && \
    chown feeds-admin-api /usr/bin/start.sh && \
    chmod +x /usr/bin/start.sh && \
    chown -R feeds-admin-api /app && \
    chown -R feeds-admin-api /walmart

USER feeds-admin-api

RUN mkdir /tmp/tomcat static

EXPOSE 8080

ENTRYPOINT ["/usr/bin/start.sh"]
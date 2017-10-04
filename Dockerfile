FROM openjdk:8-jre-alpine

COPY docker/start.sh /usr/bin/start.sh

ADD ./build/libs/feeds-admin-api-*.jar /app/libs/

RUN mkdir -p /app/log && \
    mkdir -p /app/config && \
    mkdir -p /walmart && \
    mv -f /app/libs/*.jar /app

RUN adduser -S feeds-admin-api

RUN chown feeds-admin-api /usr/bin/start.sh && \
    chmod +x /usr/bin/start.sh && \
    chown -R feeds-admin-api /app && \
    chown -R feeds-admin-api /walmart

USER feeds-admin-api

EXPOSE 8080

WORKDIR /app

ENTRYPOINT ["/usr/bin/start.sh"]
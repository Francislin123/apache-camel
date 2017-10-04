FROM openjdk:8-jre-alpine

RUN  adduser -S feeds-admin-api

COPY docker/start.sh /usr/bin/start.sh

ADD ./build/libs/feeds-admin-api-*.jar /app/libs/

RUN mkdir -p /app/log && \
    mkdir -p /app/config && \
    mkdir -p /walmart && \
    mv -f /app/libs/*.jar /app

RUN chown feeds-admin-api /usr/bin/start.sh
RUN chmod +x /usr/bin/start.sh
RUN chown -R feeds-admin-api /app
RUN chown -R feeds-admin-api /walmart

USER feeds-admin-api

EXPOSE 8080

WORKDIR /app

ENTRYPOINT ["/usr/bin/start.sh"]
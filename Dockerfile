FROM openjdk:8-jre-alpine

COPY docker/start.sh /usr/bin/start.sh
RUN chmod -R 700 /usr/bin/start.sh

EXPOSE 8080

ADD ./build/libs/feeds-admin-api-*.jar /app/libs/

WORKDIR /app

RUN mkdir -p /app/log && \
    mkdir -p /app/config && \
    mv -f /app/libs/*.jar /app

ENTRYPOINT ["/usr/bin/start.sh"]
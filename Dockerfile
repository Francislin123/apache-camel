FROM openjdk:8-jdk-alpine

COPY docker/start.sh /usr/bin/start.sh
RUN chmod -R 700 /usr/bin/start.sh

ADD ./ /build

WORKDIR /build

RUN mkdir -p /app/log && \
    mkdir -p /app/config && \
    ./gradlew clean assemble && \
    mv -f build/libs/*.jar /app && \
    rm -rvf /root/.gradle/ && rm -rf /var/cache/apk/* && \
    rm -rf ~/.m2 && \
    rm -rf /build/*

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["/usr/bin/start.sh"]
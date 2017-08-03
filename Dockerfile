FROM openjdk:8-jre

VOLUME /tmp

ADD "./build/libs/feeds-admin-api-0.0.2-SNAPSHOT.jar" "app.jar"

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]

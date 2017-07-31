FROM openjdk:8-jre

VOLUME /tmp

ADD "feeds-admin-api-0.0.1-SNAPSHOT.jar" "app.jar"

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]

FROM maven:3.5.4-jdk-8

WORKDIR "/kataelastic"

EXPOSE 8080

COPY target/kataelastic-0.0.1-SNAPSHOT.jar ./
COPY target/classes/application.* ./

VOLUME ["/kataelastic"]


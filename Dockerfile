FROM java:8

COPY target/poc-0.0.1-SNAPSHOT.jar /workdir

WORKDIR /workdir

EXPOSE 8080

ENTRYPOINT java -jar poc-0.0.1-SNAPSHOT.jar
FROM azul/zulu-openjdk:23-latest
VOLUME /tmp
ENTRYPOINT ["java","-jar","/web-app.jar"]
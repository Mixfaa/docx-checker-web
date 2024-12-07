FROM azul/zulu-openjdk:23-latest
VOLUME /tmp
ENTRYPOINT ["java","-jar","./docx-checker-web-0.0.1-SNAPSHOT.jar"]
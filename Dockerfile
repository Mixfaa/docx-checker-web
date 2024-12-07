FROM azul/zulu-openjdk:23-latest
VOLUME /tmp
ENTRYPOINT ["java","-jar","./jarfile/docx-checker-web-0.0.1-SNAPSHOT.jar"]
FROM azul/zulu-openjdk:23-latest
VOLUME /tmp
COPY /jarfile/docx-checker-web-0.0.1-SNAPSHOT.jar docx-checker.jar
ENTRYPOINT ["java","-jar","docx-checker.jar"]
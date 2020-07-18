FROM openjdk:8
COPY ./target/*.jar /usr/app/backend.jar
WORKDIR /usr/app/
# Including -DSTORAGE_DIR as part of the container startup command so we won't have to include it when using
# docker run ...
CMD ["java", "-jar", "-DSTORAGE_DIR=/usr/app/configs", "backend.jar"]
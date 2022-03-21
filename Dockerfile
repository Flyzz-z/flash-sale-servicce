FROM openjdk:11
COPY ./target/flash-sale-service-0.0.1-SNAPSHOT.jar /usr/local/flash-sale/
WORKDIR /usr/local/flash-sale
RUN chmod u+x flash-sale-service-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","flash-sale-service-0.0.1-SNAPSHOT.jar"]
FROM openjdk:11
ADD target/vending-machine-api.jar vending-machine-api.jar
ENTRYPOINT ["java", "-jar","vending-machine-api.jar"]
EXPOSE 8080
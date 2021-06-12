FROM openjdk:11

COPY . .

CMD ["java", "-jar", "/build/libs/slice-api-0.1.0.jar"]
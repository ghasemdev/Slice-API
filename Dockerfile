FROM openjdk:11

COPY . .

CMD ["java", "-jar", "/build/libs/slice-api.jar"]
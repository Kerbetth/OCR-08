FROM openjdk:8
ADD build/libs/tourguide-1.0.0.jar tourguide-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tourguide-1.0.0.jar"]

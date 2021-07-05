FROM maven:3.8.1 as build
COPY src /home/app/tenpo-challenge/src
COPY pom.xml /home/app/tenpo-challenge
RUN mvn -f /home/app/tenpo-challenge/pom.xml clean package

FROM adoptopenjdk/openjdk11:latest
COPY --from=build /home/app/tenpo-challenge/target/*.jar /usr/local/lib/tenpo-challenge.jar
CMD ["java", "-jar", "/usr/local/lib/tenpo-challenge.jar"]

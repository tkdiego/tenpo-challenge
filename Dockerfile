FROM maven:3.8.1 as build
COPY src /home/diego/IdeaProjects/tenpo-challenge/src
COPY pom.xml /home/diego/IdeaProjects/tenpo-challenge
RUN mvn -f /home/diego/IdeaProjects/tenpo-challenge/pom.xml clean package

FROM adoptopenjdk/openjdk11:latest
COPY --from=build /home/diego/IdeaProjects/tenpo-challenge-*.jar /usr/local/lib/tenpo-challenge.jar
CMD ["java", "-jar", "/usr/local/lib/tenpo-challenge.jar"]

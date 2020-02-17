FROM openjdk:11
ADD target/git-adaptor-0.0.1-SNAPSHOT.jar git-adaptor-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "git-adaptor-0.0.1-SNAPSHOT.jar"]
FROM amazoncorretto:17.0.9
ENV TZ=Europe/Moscow

ADD /build/libs/open-school-java-task1-0.0.1-SNAPSHOT.jar /open-school-java-task1-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/open-school-java-task1-0.0.1-SNAPSHOT.jar"]
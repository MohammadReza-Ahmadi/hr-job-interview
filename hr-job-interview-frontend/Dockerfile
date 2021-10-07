FROM amazoncorretto:11-alpine-jdk
MAINTAINER checkbox.com.tr
COPY target/hr-job-interview-frontend-0.0.1-SNAPSHOT.jar frontend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/frontend.jar"]

FROM maven:3-jdk-8-alpine as builder
ADD ./pom.xml /tmp
ADD src/ /tmp/src/
WORKDIR /tmp
RUN mvn dependency:go-offline
ARG aws_region
ARG aws_access_key_id
ARG aws_secret_access_key
ENV AWS_REGION=$aws_region
ENV AWS_ACCESS_KEY_ID=$aws_access_key_id
ENV AWS_SECRET_ACCESS_KEY=$aws_secret_access_key
RUN mvn package
FROM openjdk:8-jdk-alpine
COPY --from=builder /tmp/target/transactions_api-0.0.1-SNAPSHOT.jar /tmp/
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/tmp/transactions_api-0.0.1-SNAPSHOT.jar"]
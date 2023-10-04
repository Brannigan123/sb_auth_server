
### First stage: build  

# Base image for maven build
FROM azul/zulu-openjdk-alpine:21 AS builder
# Install dependencies
RUN apk add --no-cache bash procps curl tar
# Install maven
ENV MAVEN_HOME /usr/share/maven
COPY --from=maven:3.9.4-eclipse-temurin-11 ${MAVEN_HOME} ${MAVEN_HOME}
COPY --from=maven:3.9.4-eclipse-temurin-11 /usr/local/bin/mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY --from=maven:3.9.4-eclipse-temurin-11 /usr/share/maven/ref/settings-docker.xml /usr/share/maven/ref/settings-docker.xml
RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

ARG MAVEN_VERSION=3.9.4
ARG USER_HOME_DIR="/root"
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# Add source code
ADD . .
# Build jar + minimal jre
RUN sh ./package-jre.sh

## Second stage: minimal runtime environment

# Base image for minimal runtime
FROM alpine:latest
# Set path to minimal jre
ENV JAVA_HOME /user/java/minimal
ENV PATH $JAVA_HOME/bin:$PATH
# Copy minimal jre from the build stage
COPY --from=builder /project-jre $JAVA_HOME
# Copy jar from the build stage
COPY --from=builder target/auth_server-latest.jar auth_server.jar
# Expose port
EXPOSE 8080
# Start jar
CMD ["java", "-jar", "auth_server.jar"]
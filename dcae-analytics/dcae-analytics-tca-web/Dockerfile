# NOTE: This Dockerfile is auto generated and filtered via maven resources plugin.
# Edits to generated Dockerfile will NOT be saved.
# Only changes made in docker/Dockerfile template file will be persistent
FROM openjdk:8-jre-alpine
MAINTAINER Rajiv Singla <rs153v@att.com>
ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dlogging.config=logback.xml", \
            "-Dlogging.file=logs/dcae-analytics-tca-web.log", \
            "-Dspring.profiles.active=dev", \
            "-jar", "dcae-analytics-tca-web.jar"]
EXPOSE 9091
VOLUME /tmp
ADD docker/logback.xml logback.xml
ADD target/dcae-analytics-tca-web-3.0.0-SNAPSHOT.jar dcae-analytics-tca-web.jar

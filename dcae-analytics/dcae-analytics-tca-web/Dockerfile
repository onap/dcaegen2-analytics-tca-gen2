# NOTE: This Dockerfile is auto generated and filtered via maven resources plugin.
# Edits to generated Dockerfile will NOT be saved.
# Only changes made in docker/Dockerfile template file will be persistent
# ==========================================================================

FROM openjdk:8-jre-alpine

MAINTAINER Rajiv Singla <rs153v@att.com>

VOLUME /tmp
ADD logback.xml logback.xml
EXPOSE 9091

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dlogging.config=logback.xml", \
            "-Dlogging.file=logs/${project.artifactId}.log", \
            "-Dspring.profiles.active=dev", \
            "-jar", "${project.build.finalName}.jar"]

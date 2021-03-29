# NOTE: This Dockerfile is auto generated and filtered via maven resources plugin.
# Edits to generated Dockerfile will NOT be saved.
# Only changes made in docker/Dockerfile template file will be persistent
# ==========================================================================

FROM openjdk:11-jre-slim

MAINTAINER Rajiv Singla <rs153v@att.com>

ARG PROJECT_BUILD_DIR_NAME
ARG FINAL_JAR
ARG DEPENDENCIES_DIR
ARG DOCKER_ARTIFACT_DIR

RUN mkdir -p /var/log/ONAP/dcaegen2/analytics/tca-gen2
#Add a new user and group to allow container to be run as non-root
RUN groupadd tca-gen2 && useradd -g tca-gen2 tca-gen2; \
    chmod a+rwx /opt; \
    chmod a+rwx /var/log/ONAP/dcaegen2/analytics/tca-gen2; \
    chown -R tca-gen2:tca-gen2 /opt; \
    chown -R tca-gen2:tca-gen2 /var/log/ONAP/dcaegen2/analytics/tca-gen2

#Copy dependencies and executable jar
WORKDIR ${DOCKER_ARTIFACT_DIR}
COPY ${PROJECT_BUILD_DIR_NAME}/${FINAL_JAR} .
#Overcome Docker limitation to put ARG inside ENTRYPOINT
RUN ln -s ${FINAL_JAR} tca-gen2.jar

EXPOSE 8100

USER tca-gen2:tca-gen2
ENTRYPOINT ["java", "-Dspring.profiles.active=configBindingService,dmaap", "-jar", "tca-gen2.jar"]

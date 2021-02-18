# NOTE: This Dockerfile is auto generated and filtered via maven resources plugin.
# Edits to generated Dockerfile will NOT be saved.
# Only changes made in docker/Dockerfile template file will be persistent
# ==========================================================================

FROM nexus3.onap.org:10001/onap/integration-java11:8.0.0

MAINTAINER Rajiv Singla <rs153v@att.com>

ARG PROJECT_BUILD_DIR_NAME
ARG FINAL_JAR
ARG DEPENDENCIES_DIR
ARG DOCKER_ARTIFACT_DIR

RUN mkdir -p /var/log/ONAP/dcaegen2/analytics/tca-gen2
#Add a new user and group to allow container to be run as non-root
RUN addgroup tca-gen2 && adduser --ingroup tca-gen2 tca-gen2 && \
    chmod u+rwx /opt; \
    chmod u+rw /var/log/ONAP/dcaegen2/analytics/tca-gen2 && \
    chown -R tca-gen2:tca-gen2 /opt && \
    chown -R tca-gen2:tca-gen2 /var/log/ONAP/dcaegen2/analytics/tca-gen2

#Copy dependencies and executable jar
WORKDIR ${DOCKER_ARTIFACT_DIR}
COPY ${PROJECT_BUILD_DIR_NAME}/${FINAL_JAR} .
#Overcome Docker limitation to put ARG inside ENTRYPOINT
RUN ln -s ${FINAL_JAR} tca-gen2.jar

EXPOSE 8100

USER tca-gen2:tca-gen2
ENTRYPOINT ["java", "-Dspring.profiles.active=configBindingService,dmaap,mongo", "-jar", "tca-gen2.jar"]

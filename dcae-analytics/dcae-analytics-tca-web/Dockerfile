FROM nexus3.onap.org:10001/onap/integration-java11:8.0.0

MAINTAINER Rajiv Singla <rs153v@att.com>

ARG PROJECT_BUILD_DIR_NAME
ARG FINAL_JAR
ARG DEPENDENCIES_DIR
ARG DOCKER_ARTIFACT_DIR
ARG user=tca-gen2
ARG group=tca-gen2

WORKDIR ${DOCKER_ARTIFACT_DIR}
#Copy dependencies and executable jar
COPY ${PROJECT_BUILD_DIR_NAME}/${FINAL_JAR} .

USER root

#Symlink to overcome Docker limitation to put ARG inside ENTRYPOINT
RUN mkdir -p /var/log/ONAP/dcaegen2/analytics/tca-gen2 && \
    addgroup $group && adduser --system --disabled-password --no-create-home --ingroup $group $user && \
    chmod g+rwx /opt; \
    chmod u+rw /var/log/ONAP/dcaegen2/analytics/tca-gen2 && \
    chown -R $user /opt && \
    chown -R $user /var/log/ONAP/dcaegen2/analytics/tca-gen2 && \
    ln -s ${FINAL_JAR} tca-gen2.jar && \
    chown $user ${FINAL_JAR} tca-gen2.jar

EXPOSE 8100

USER $user
ENTRYPOINT ["java", "-Dspring.profiles.active=configBindingService,dmaap,mongo", "-jar", "tca-gen2.jar"]

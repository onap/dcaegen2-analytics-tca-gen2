FROM nexus3.onap.org:10001/onap/integration-java11:8.0.0

MAINTAINER Rajiv Singla <rs153v@att.com>

ARG PROJECT_BUILD_DIR_NAME
ARG FINAL_JAR
ARG DEPENDENCIES_DIR
ARG DOCKER_ARTIFACT_DIR
ARG user=tca-gen2
ARG group=tca-gen2

USER root

RUN mkdir -p /var/log/ONAP/dcaegen2/analytics/tca-gen2
#Add a new user and group to allow container to be run as non-root
RUN addgroup $group && adduser --system --disabled-password --no-create-home --ingroup $group $user && \
    chmod a+rwx /opt && \
    chmod a+rwx /var/log/ONAP/dcaegen2/analytics/tca-gen2 && \
    chown -R $user:$group /opt && \
    chown -R $user:$group /var/log/ONAP/dcaegen2/analytics/tca-gen2

#Copy dependencies and executable jar
WORKDIR ${DOCKER_ARTIFACT_DIR}
COPY ${PROJECT_BUILD_DIR_NAME}/${FINAL_JAR} .
#Overcome Docker limitation to put ARG inside ENTRYPOINT
RUN ln -s ${FINAL_JAR} tca-gen2.jar

EXPOSE 8100

USER $user:$group
ENTRYPOINT ["java", "-Dspring.profiles.active=configBindingService,dmaap,mongo", "-jar", "tca-gen2.jar"]

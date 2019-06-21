# NOTE: This Dockerfile is auto generated and filtered via maven resources plugin.
# Edits to generated Dockerfile will NOT be saved.
# Only changes made in docker/Dockerfile template file will be persistent
# ==========================================================================

FROM openjdk:8-jre-alpine

MAINTAINER Rajiv Singla <rs153v@att.com>

ARG PROJECT_BUILD_DIR_NAME
ARG FINAL_JAR
ARG DEPENDENCIES_DIR
ARG DOCKER_ARTIFACT_DIR
ARG USER_GROUP=tca-gen2-group
ARG NON_ROOT_USER=tca-gen2

#Add a new user and group to allow container to be run as non-root
RUN addgroup -S ${USER_GROUP} && adduser -S ${NON_ROOT_USER} -G ${USER_GROUP}

#Copy dependencies and executable jar
WORKDIR ${DOCKER_ARTIFACT_DIR}
COPY ${PROJECT_BUILD_DIR_NAME}/${FINAL_JAR} .
#Overcome Docker limitation to put ARG inside ENTRYPOINT
RUN ln -s ${FINAL_JAR} tca-gen2.jar

EXPOSE 8100

USER ${NON_ROOT_USER}:${USER_GROUP}
ENTRYPOINT ["java", "-jar", "tca-gen2.jar"]

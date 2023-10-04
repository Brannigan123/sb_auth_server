#!bin/bash

JAVA_HOME=./project-jre
PATH=$JAVA_HOME/bin:$PATH

LOGGING_LEVEL_ROOT=WARN
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_WEB=WARN
LOGGING_LEVEL_ORG_HIBERNATE=WARN

java -jar \
-Dlogging.level.root=$LOGGING_LEVEL_ROOT \
-Dlogging.level.org.springframework.web=$LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_WEB \
-Dlogging.level.org.hibernate=$LOGGING_LEVEL_ORG_HIBERNATE \
target/auth_server-latest.jar
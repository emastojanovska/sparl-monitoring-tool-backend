FROM postgres as build
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB sparql_monitoring_tool_db
COPY sparql_monitoring_tool_db.sql /docker-entrypoint-initdb.d/

FROM openjdk:17-slim as build

COPY --chown=nobody:nogroup target/sparql-monitoring-tool-*.jar sparql-monitoring-tool.jar

USER root
RUN mkdir -p /files \
 && chown -R nobody:nogroup /files

USER nobody
ENTRYPOINT ["java", "-jar", "/sparql-monitoring-tool.jar"]
EXPOSE 9091

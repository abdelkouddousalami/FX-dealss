FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM quay.io/wildfly/wildfly:30.0.0.Final-jdk17

# Set environment variables
ENV WILDFLY_HOME=/opt/jboss/wildfly
ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=fxdeals
ENV DB_USER=fxdeals_user
ENV DB_PASSWORD=fxdeals_password

# Copy MySQL JDBC driver
ADD --chown=jboss:jboss https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar $WILDFLY_HOME/standalone/deployments/

# Copy the WAR file from build stage
COPY --from=build /app/target/fxdeals.war $WILDFLY_HOME/standalone/deployments/

# Setup datasource via CLI
USER root
RUN mkdir -p /opt/jboss/wildfly/standalone/log/fxdeals && \
    chown -R jboss:jboss /opt/jboss/wildfly

USER jboss

# Configure datasource using CLI file
RUN echo 'embed-server --std-out=echo --server-config=standalone.xml' > /tmp/datasource.cli && \
    echo 'module add --name=com.mysql --resources=/opt/jboss/wildfly/standalone/deployments/mysql-connector-j-8.2.0.jar --dependencies=javax.api,javax.transaction.api' >> /tmp/datasource.cli && \
    echo '/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-class-name=com.mysql.cj.jdbc.Driver)' >> /tmp/datasource.cli && \
    echo 'data-source add --name=FxDealsDS --jndi-name=java:jboss/datasources/FxDealsDS --driver-name=mysql --connection-url=jdbc:mysql://fxdeals-mysql:3306/fxdeals?useSSL=false&allowPublicKeyRetrieval=true --user-name=fxdeals_user --password=fxdeals_password --use-ccm=true --max-pool-size=25 --enabled=true' >> /tmp/datasource.cli && \
    echo 'stop-embedded-server' >> /tmp/datasource.cli && \
    /opt/jboss/wildfly/bin/jboss-cli.sh --file=/tmp/datasource.cli && \
    rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history /tmp/datasource.cli

# Expose ports
EXPOSE 8080 9990

# Start WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

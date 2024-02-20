FROM alpine:latest

# Install OpenJDK 11 and maven
RUN apk add --no-cache openjdk11

# Set JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk

# Add JAVA_HOME to PATH
ENV PATH=${PATH}:${JAVA_HOME}/bin

# Set up Gradle environment variables
ENV GRADLE_HOME /opt/gradle
ENV GRADLE_VERSION 7.6.4
ENV PATH $GRADLE_HOME/bin:$PATH

# Download and install Gradle
RUN wget -q --no-check-certificate "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -O /tmp/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip -q /tmp/gradle-${GRADLE_VERSION}-bin.zip -d /opt && \
    rm -f /tmp/gradle-${GRADLE_VERSION}-bin.zip && \
    ln -s /opt/gradle-${GRADLE_VERSION} $GRADLE_HOME

# Create app directory
RUN mkdir app

# Create src directory within app directory
RUN mkdir app/src

# Copy src directory to root of app directory
COPY src /app/src

# Copy config dir
RUN mkdir app/config
COPY config /app/config

## Copy pom.xml and checkstyle.xml to root of app directory
COPY build.gradle.kts settings.gradle.kts /app

# Copy .env to root of app directory
COPY .env /app

# Copy build.sh to root of app directory
COPY build.sh /app

# Copy run.sh to root of app directory
COPY run.sh /app

WORKDIR app
CMD ./run.sh



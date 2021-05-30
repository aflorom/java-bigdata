FROM ubuntu:18.04

ENV HBASE_VERSION=2.1.2
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre/
#ENV HBASE_HOME=/build/hbase-$HBASE_VERSION
ENV HBASE_HOME=/build/hbase

RUN apt update && apt -y install software-properties-common ssh openssh-server vim
RUN apt -y install openjdk-8-jdk && update-java-alternatives -s java-1.8.0-openjdk-amd64

# Download hbase
ADD https://archive.apache.org/dist/hbase/${HBASE_VERSION}/hbase-${HBASE_VERSION}-bin.tar.gz /build/
RUN tar -C /build/ -xzvf /build/hbase-$HBASE_VERSION-bin.tar.gz
RUN mkdir $HBASE_HOME
RUN mv /build/hbase-$HBASE_VERSION/* $HBASE_HOME
RUN rm -rf /build/hbase-$HBASE_VERSION
RUN rm -rf /build/hbase-${HBASE_VERSION}-bin.tar.gz

# Hbase configuration
COPY config/* /config/
COPY config/hbase-site.xml ${HBASE_HOME}/conf
COPY config/hbase-env.sh ${HBASE_HOME}/conf

# Add files .csv
ADD files/SET-dec-2013.csv /files/SET-dec-2013.csv

# Scripts to start hbase and loading and extracting functionalities
ADD scripts/start-hbase.sh /scripts/start-hbase.sh
ADD scripts/start-load.sh /scripts/start-load.sh
ADD scripts/start-extract.sh /scripts/start-extract.sh

RUN chmod +x /scripts/start-load.sh
RUN chmod +x /scripts/start-extract.sh

# .jar from the java project to run it
ADD jars/aflorom-0.0.1-SNAPSHOT-jar-with-dependencies.jar /jars/aflorom-0.0.1-SNAPSHOT-jar-with-dependencies.jar

RUN echo "alias hbase=${HBASE_HOME}/bin/hbase" >> ~/.bashrc

# Hbase ports
EXPOSE 2181
EXPOSE 16000 16001
EXPOSE 16010 16012 16013
EXPOSE 16030 16032 16033 16034 16035

CMD /bin/bash /scripts/start-hbase.sh && /bin/bash
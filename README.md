# Big Data: Fase 1

For compile the project you can run the next line:
- in aflorom/ -> mvn clean compile assembly:single

Anyway, the .jar already will be available in docker instance because is allocated in /jars directory, so for building the image you can run the next line:
- docker build -it bigdata-fase1 .

Next, for running the docker instance, execute the netx line:
- docker run --name=bigdata-fase1 -P -it -h hbase-docker bigdata-fase1

Once inside the docker, you have available some jars:

- inside /scripts -> execute ./start-load.sh for loading step. This script uses 2 and 3 like factors and the file SET-dec-2013.csv
- inside /scripts -> execute ./start-extract.sh for extraction step. This script uses 2 and 3 like factors and the file SET-dec-2013.csv
- inside /scripts -> execute ./drop-all.sh for dropping both tables if you have used some of the two scripts

Otherwise, you can navigate to jars/ directory and execute the jar with some avaiable commands:
- java -jar aflorom-0.0.1-SNAPSHOT-jar-with-dependencies F C /files/SET-dec-2013.csv CARGA
- java -jar aflorom-0.0.1-SNAPSHOT-jar-with-dependencies F C /files/SET-dec-2013.csv EXTRACCION

Example about this:
- java -jar aflorom-0.0.1-SNAPSHOT-jar-with-dependencies 2 3 /files/SET-dec-2013.csv CARGA
- java -jar aflorom-0.0.1-SNAPSHOT-jar-with-dependencies 2 3 /files/SET-dec-2013.csv EXTRACCION

Once you have completed the mentioned You can navigate to hbase, checking the ports and you can see both tables: table-loading-part-1 and table-extraction-pat-1

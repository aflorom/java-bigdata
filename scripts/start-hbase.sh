#!/bin/sh
${HBASE_HOME}/bin/hbase-daemon.sh start zookeeper
${HBASE_HOME}/bin/hbase-daemon.sh start regionserver
${HBASE_HOME}/bin/local-regionservers.sh start 2 3 4 5
${HBASE_HOME}/bin/hbase-daemon.sh start master
${HBASE_HOME}/bin/local-master-backup.sh start 2 3
${HBASE_HOME}/bin/hbase-daemon.sh start thrift

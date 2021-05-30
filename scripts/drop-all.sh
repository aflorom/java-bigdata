#!/bin/sh
echo "disable 'table-carga-parte-1'" | /build/hbase/bin/hbase shell -n
echo "drop 'table-carga-parte-1'" | /build/hbase/bin/hbase shell -n
echo "disable 'table-extraccion-parte-1'" | /build/hbase/bin/hbase shell -n
echo "drop 'table-extraccion-parte-1'" | /build/hbase/bin/hbase shell -n
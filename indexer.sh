#!/usr/bin/env bash

echo "This program convert Lucene index to sqlite records"

#java -cp MyJar.jar:lib/* com.somepackage.subpackage.Main
ant compile

# Run only if compile success
if [ $? -eq 0 ]; then
    java -cp ./binary:WebContent/WEB-INF/lib/* waqf.sqlite.GenSqlite $1 $2
fi

#WebContent/WEB-INF/index/g2b1

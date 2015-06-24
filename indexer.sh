#!/usr/bin/env bash

echo "This program convert Lucene index to sqlite records, see ex:"

echo "#./indexer.sh WebContent/WEB-INF/index/g2b1 g2b1"

read "press any key" input

#java -cp MyJar.jar:lib/* com.somepackage.subpackage.Main
ant compile

# Run only if compile success
if [ $? -eq 0 ]; then
    java -cp ./binary:WebContent/WEB-INF/lib/* waqf.sqlite.GenSqlite $1 $2

    # If conversion OK, copy file
    if [ $? -eq 0 ]; then
        cp ./sonna.sqlite ./../Books3/sonna/www/
    fi
fi

#WebContent/WEB-INF/index/g2b1

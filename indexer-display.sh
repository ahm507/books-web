#!/usr/bin/env bash


echo "This program search sqlite db using FTS"
echo "Example: ./indexer.sh ./war/WEB-INF/index/g2b2 g2b2"

#java -cp MyJar.jar:lib/* com.somepackage.subpackage.Main
ant compile

# Run only if compile success
if [ $? -eq 0 ]; then
    java -cp ./binary:WebContent/WEB-INF/lib/* waqf.sqlite.Display $1 $2
fi


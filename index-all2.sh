#!/usr/bin/env bash

function indexBook {
    echo "Indexing: " $1 "with code: " $2
    echo
    java -cp ./binary:WebContent/WEB-INF/lib/* waqf.sqlite.GenSqlite2 $1 $2
}

echo
echo "This program convert Lucene index to sqlite records, for all available indexes:"
echo "first build will be done to ensure everything is compiled"
read -p "press any key..." input

ant

echo
echo "Now will start indexing"
read -p "press any key..." input


#indexing books. The order is requested by Dr. Aly

echo "Indexing Bukhary"
indexBook "WebContent/WEB-INF/index/g2b1" "g2b1"

echo "Indexing Muslim"
indexBook "WebContent/WEB-INF/index/g2b2" "g2b2"

#echo "Indexing Aby Daood"
#indexBook "WebContent/WEB-INF/index/g2b5" "g2b5"
#echo "Indexing Termezy"
#indexBook "WebContent/WEB-INF/index/g2b3" "g2b3"
#echo "Indexing Nasa2y"
#indexBook "WebContent/WEB-INF/index/g2b4" "g2b4"
#echo "Indexing Ibn Maga"
#indexBook "WebContent/WEB-INF/index/g2b6" "g2b6"
#echo "Indexing Malek"
#indexBook "WebContent/WEB-INF/index/g2b7" "g2b7"
#echo "Indexing Ahmed"
#indexBook "WebContent/WEB-INF/index/g2b8" "g2b8"
#echo "Indexing Daremy"
#indexBook "WebContent/WEB-INF/index/g2b9" "g2b9"
#echo "Indexing Darkotny"
#indexBook "WebContent/WEB-INF/index/g2b10" "g2b10"
#echo "Indexing Homaidy"
#indexBook "WebContent/WEB-INF/index/g2b11" "g2b11"
#echo "Indexing Bayhaky"
#indexBook "WebContent/WEB-INF/index/g2b12" "g2b12"


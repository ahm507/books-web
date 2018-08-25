#!/usr/bin/env bash

function genSQLite {
    echo "Indexing: " $1 "with code: " $2
    echo
    java -cp ./binary:WebContent/WEB-INF/lib/* waqf.sqlite.GenSqlite $1 $2

}

#sample command line
# java -cp ./binary:WebContent/WEB-INF/lib/* waqf.sqlite.GenSqlite "WebContent/WEB-INF/index/g2b7" "g2b7"


echo "This program convert Lucene index to sqlite records, for all available indexes:"
read -p "press any key" input


#indexing books. The order is requested by Dr. Aly

#echo "Indexing Bukhary"
genSQLite "WebContent/WEB-INF/index/g2b1" "g2b1"
#echo "Indexing Muslim"
#genSQLite "WebContent/WEB-INF/index/g2b2" "g2b2"
#echo "Indexing Aby Daood"
#genSQLite "WebContent/WEB-INF/index/g2b5" "g2b5"
#echo "Indexing Termezy"
#genSQLite "WebContent/WEB-INF/index/g2b3" "g2b3"
#echo "Indexing Nasa2y"
#genSQLite "WebContent/WEB-INF/index/g2b4" "g2b4"
#echo "Indexing Ibn Maga"
#genSQLite "WebContent/WEB-INF/index/g2b6" "g2b6"


echo "Indexing Malek"
#genSQLite "WebContent/WEB-INF/index/g2b7" "g2b7"
#echo "Indexing Ahmed"
#genSQLite "WebContent/WEB-INF/index/g2b8" "g2b8"
# echo "Indexing Daremy"
# genSQLite "WebContent/WEB-INF/index/g2b9" "g2b9"
#echo "Indexing Darkotny"
#genSQLite "WebContent/WEB-INF/index/g2b10" "g2b10"
#echo "Indexing Homaidy"
#genSQLite "WebContent/WEB-INF/index/g2b11" "g2b11"
#echo "Indexing Bayhaky"
#genSQLite "WebContent/WEB-INF/index/g2b12" "g2b12"

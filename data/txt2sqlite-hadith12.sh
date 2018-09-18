#!/usr/bin/env bash


echo
echo "This program parse text files and generate SQLite file"


#indexing books. The order is requested by Dr. Aly

read -p "press any key to Index Bukhary ..." input
cd ./g2b1 && ../txt2sqlite.sh

read -p "press any key to Index Muslim ..." input
cd ../g2b2 && ../txt2sqlite.sh

read -p "press any key to Index Abo Daood ..." input
cd ../g2b5 && ../txt2sqlite.sh

read -p "press any key to Index Termezy" input
cd ../g2b3 && ../txt2sqlite.sh

read -p "press any key to Index Nasa2y" input
cd ../g2b4 && ../txt2sqlite.sh

read -p "press any key to Index Ibn Maga" input
cd ../g2b6 && ../txt2sqlite.sh

read -p "press any key to Index Malek" input
cd ../g2b7 && ../txt2sqlite.sh

read -p "press any key to Index  Ahmed" input
cd ../g2b8 && ../txt2sqlite.sh

read -p "press any key to Index Daremy" input
cd ../g2b9 && ../txt2sqlite.sh

read -p "press any key to Index Darkotny" input
cd ../g2b10 && ../txt2sqlite.sh

read -p "press any key to Index Homaidy" input
cd ../g2b11 && ../txt2sqlite.sh

read -p "press any key to Index Bayhaky" input
cd ../g2b12 && ../txt2sqlite.sh


cd ..


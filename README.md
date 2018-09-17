Books-Web
==========

This repository hosts data and various utilities used for text books processing.


/data folder:
-------------

All original books are located under data/ directory.

Books text are encoded as ASCII CP1256, it is not converted to unicode yet.

usually shell files to process data files are located in this folder.

Hadith books folders start with g2b1 up to g2b12.
Each folder contains various settings in addition to a list of all book files starting with key File1 and up to FileN,
where N is the sequential number. Files are ordered.

Level markers are important to know the table of contents of the file. It is customizable, but by default
it starts with #L0 and increase as needed. These Table of contents tags are located as the first word inside the record.



Txt2SQlite:
----------

This tool parses book text files and append its records to SQLite file. This file is used in Android application.

This shell script file, gen-sqlite-hadith12.sh, parses text files for the 12 Sonna books and append them to books.sqlite
file. Please make sure the file is empty. Just move or remove old books.sqlite file and make a copy from
books-empty.sqlite to books.sqlite. Then run the shell script to do the processing.

A typical scenarios is:

    rm books.sqlite
    cp books-empty.sqlite books.sqlite
    ./gen-sqlite-hadith12.sh

You need to copy books.sqlite to resources folder to the Android application

    cp books.sqlite ./hadith-android/





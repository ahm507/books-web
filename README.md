Books-Web
==========

This repository hosts data and various utilities used for text books processing.


/data folder:
-------------

All original books are located under data/ directory.

Books text are encoded as ASCII CP1256, it is not converted to Unicode yet.

Usually shell files to process data files are located in this folder.

Hadith books folders start with g2b1 up to g2b12.
Each folder contains various settings in addition to a list of all book files starting with key File1 and up to FileN,
where N is the sequential number. Files are ordered.

Level markers are important to know the table of contents of the file. It is customizable, but by default
it starts with #L0 and increase as needed. These Table of contents tags are located as the first word inside the record.



Txt2SQlite:
----------

This tool parses book text files and append its records to SQLite file. This file is used in Android application.

This shell script file, gen-sqlite-hadith12.sh, parses text files for the 12 Sonna books and append them 
to books.sqlite file. Please make sure the file is empty. 
Just move or remove old books.sqlite file and make a copy from
books-empty.sqlite to books.sqlite. Then run the shell script to do the processing.

A typical scenarios is:

    rm books.sqlite
    cp books-empty.sqlite books.sqlite
    ./gen-sqlite-hadith12.sh

You need to copy books.sqlite to resources folder to the Android application

    cp books.sqlite ./hadith-android/


Generating Initial Spring Boot Apps:
------------------------------------
First, make sure you installed Spring command line tools.

Show init projects options

    spring init --list

You can run Spring shell for autocomplete

    spring shell
    init <TAB>

Example generation command from inside "spring shell"
    
    init -a=indexer -p=jar -g=net.elazhar.books --package-name=net.elazhar.books 



Legacy: src/waqf/epub
----------------------
Experimental code used to generate standard epub files from books text files.
The generated files was so big and heavy on opening. 
The epub search is poor. You have to search with Arabic with diacritics to search into the file, 
I mean you have to search with exact diacritics. 
This is why we moved to use SQLite as it supports full text indexing.


Legacy: src/waqf/sqlite
------------------------
Converts Lucene index files to SQLite. It was because I though at that time the Arabic text
 is corrupted due to my move from Windows machine to Mac machine. 
 Later one I discovered that the text is just the old ASCII with CP1256 encoding. 
 Due to a bug that caused some missing file, I then created the new Txt2SQLite tool described above. 




Legacy src/waqf/** and /WebContent
-----------------------------------
This is the code of a legacy web application that read index books and search them using Apache Lucene search library.
It is not in production anymore.



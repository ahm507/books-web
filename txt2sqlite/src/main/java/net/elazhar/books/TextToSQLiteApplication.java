package net.elazhar.books;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileNotFoundException; 

@SpringBootApplication
public class TextToSQLiteApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(TextToSQLiteApplication.class);

    public static void main(String[] args) {
		SpringApplication.run(TextToSQLiteApplication.class, args);
	}

    @Override 
    public void run(String... args) throws Exception {
        LOG.info("******* Hello commands. Here you have full spring power inside the console application");


        Indexer indexer = new Indexer();
        int indexed = 0;
        try {
            indexed = indexer.indexDoc();
        } catch (FileNotFoundException e) {
            LOG.info("build.properties file not found");
        }

        LOG.info("Indexed records count: " + String.valueOf(indexed));

//        loadBuildProperties();
//
//        verifyBuildProperties();
//
//        parseTextFiles(new TextLineReader(), new SQLiteAppender());

//        for (int i = 0; i < args.length; ++i) {
//            LOG.info("args[{}]: {}", i, args[i]);
//        }
    }
}

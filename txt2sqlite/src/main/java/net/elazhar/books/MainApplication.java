package net.elazhar.books;

import net.elazhar.books.scanner.BookScanner;
import net.elazhar.books.scanner.IndexWriter;
import net.elazhar.books.scanner.WiterMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileNotFoundException; 
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class MainApplication implements CommandLineRunner {


    @Autowired
    BookScanner bookScanner;

    @Autowired
    IndexWriter sqliteWriter;


    private static Logger LOG = LoggerFactory
            .getLogger(MainApplication.class);

    public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

    @Override 
    public void run(String... args) throws Exception {

        bookScanner.setSetWriter(sqliteWriter);
        int indexed = 0;
        try {
            indexed = bookScanner.indexDoc();
        } catch (FileNotFoundException e) {
            LOG.info("build.properties file not found");
        } catch (WiterMissingException e) {
            LOG.info(e.getMessage());
            e.printStackTrace();
        }

        LOG.info("Indexed records count: " + String.valueOf(indexed));

    }
}

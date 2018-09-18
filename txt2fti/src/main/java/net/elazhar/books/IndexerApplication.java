package net.elazhar.books;

import net.elazhar.books.indexing.FTSWriter;
import net.elazhar.scanner.BookScanner;
import net.elazhar.scanner.MissingWriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

@SpringBootApplication(scanBasePackages = "net.elazhar")
public class IndexerApplication implements CommandLineRunner {


    @Autowired
    BookScanner bookScanner;

    @Autowired
    FTSWriter ftsWriter;

    private static Logger LOG = LoggerFactory.getLogger(IndexerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(IndexerApplication.class, args);
	}

    @Override 
    public void run(String... args) throws Exception {

        LOG.info("Full Text Indexing is started");

        bookScanner.setSetWriter(ftsWriter);
        int indexed = 0;
        try {
            indexed = bookScanner.indexDoc();
        } catch (FileNotFoundException e) {
            LOG.info("build.properties file not found");
        } catch (MissingWriterException e) {
            LOG.info("Configuration error!");
            LOG.info(e.getMessage());
            e.printStackTrace();
        }

        LOG.info("Indexed records count: " + String.valueOf(indexed));

    }
}



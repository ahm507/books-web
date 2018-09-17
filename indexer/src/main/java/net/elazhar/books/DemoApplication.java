package net.elazhar.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class IndexerApplication implements CommandLineRunner {


    private static Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Override 
    public void run(String... args) throws Exception {

        LOG.info("Application is running");

    }
}



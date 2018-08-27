package net.elazhar.books;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(DemoApplication.class);

    public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Override 
    public void run(String... args) {
        LOG.info("Hello commands. Here you have full spring power inside the console application");

//        loadBuildProperties();
//
//        verifyBuildProperties();
//
//        parseTextFiles(new TextLineReader(), new SQLiteAppender());

        for (int i = 0; i < args.length; ++i) {
            LOG.info("args[{}]: {}", i, args[i]);
        }
    }
}

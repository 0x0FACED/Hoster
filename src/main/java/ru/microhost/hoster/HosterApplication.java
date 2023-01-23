package ru.microhost.hoster;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.microhost.hoster.repos.FilesRepository;
import ru.microhost.hoster.storage.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class HosterApplication {

	public static void main(String[] args) {
		SpringApplication.run(HosterApplication.class, args);
	}
}

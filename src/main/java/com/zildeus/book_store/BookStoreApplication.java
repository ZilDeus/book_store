package com.zildeus.book_store;

import com.zildeus.book_store.config.security.RsaKeyPair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyPair.class)
@SpringBootApplication
public class BookStoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
	}
}

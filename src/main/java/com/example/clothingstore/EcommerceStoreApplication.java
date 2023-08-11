package com.example.clothingstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class EcommerceStoreApplication {

	public static void main(String[] args) {
//		if (!Desktop.isDesktopSupported()) {
//			System.out.println("This app needs a desktop manager to run, exiting.");
//			System.exit(1);
//		}
//		new SpringApplicationBuilder(EcommerceStoreApplication.class).headless(false).run(args);

		SpringApplication.run(EcommerceStoreApplication.class, args);

	}


//	"http://localhost:8080/log_in_system"


//	@EventListener(ApplicationReadyEvent.class)
//	public void openBrowserAfterStartup() throws IOException, URISyntaxException {
//		// open default browser after start:
//		Desktop.getDesktop().browse(new URI("http://localhost:8080/log_in_system"));
//	}

}

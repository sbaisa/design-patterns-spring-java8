package victor.training.oo.behavioral.template;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import lombok.Data;

@SpringBootApplication
public class TemplateSpringApp implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(TemplateSpringApp.class, args);
	}

	@Autowired
	private EmailService service;
	
	public void run(String... args) throws Exception {
		service.sendOrderReceivedEmail("a@b.com");
		
		new Hackareala().sendOrderReceivedEmail("a@b.com");
	}
}

@Service
class EmailService {

	public void sendOrderReceivedEmail(String emailAddress) {
		EmailContext context = new EmailContext(/*smtpConfig,etc*/);
		int MAX_RETRIES = 3;
		for (int i = 0; i < MAX_RETRIES; i++ ) {
			Email email = new Email(); // constructor generates new unique ID
			email.setSender("noreply@corp.com");
			email.setReplyTo("/dev/null");
			email.setTo(emailAddress);
			p(email);
			boolean success = context.send(email);
			if (success) break;
		}
	}

	protected void p(Email email) {
		// triunghiuletz
		email.setSubject("Order Received");
		email.setBody("Thank you for your order");
	}
}

class Hackareala extends EmailService {
	protected void p(Email email) {
		//patratzel
		email.setSubject("Order Shipped");
		email.setBody("Ti-am trimas! Speram s-ajunga (de data asta)!");
	}
}

class EmailContext {
	public boolean send(Email email) {
		System.out.println("Trying to send " + email);
		return new Random(System.nanoTime()).nextBoolean();
	}
}

@Data
class Email {
	private final long id = new Random(System.nanoTime()).nextLong();
	private String sender;
	private String subject;
	private String body;
	private String replyTo;
	private String to;
}
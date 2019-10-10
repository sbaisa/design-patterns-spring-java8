package victor.training.oo.behavioral.template;

import java.util.Random;

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

//	@Autowired
//	private EmailService service;
	
	public void run(String... args) {
		new OrderReceivedEmailSender().sendOrderReceivedEmail("a@b.com");
		new OrderShippedEmailSender().sendOrderReceivedEmail("a@b.com");
	}
}

@Service
abstract class BaseEmailSender {

	public void sendOrderReceivedEmail(String emailAddress) {
		EmailContext context = new EmailContext(/*smtpConfig,etc*/);
		int MAX_RETRIES = 3;
		for (int i = 0; i < MAX_RETRIES; i++ ) {
			Email email = new Email(); // constructor generates new unique ID
			email.setSender("noreply@corp.com");
			email.setReplyTo("/dev/null");
			email.setTo(emailAddress);
			writeEmail(email);
			boolean success = context.send(email);
			if (success) break;
		}
	}
	protected abstract void writeEmail(Email email);
}
class OrderReceivedEmailSender extends BaseEmailSender {
	public void writeEmail(Email email) {
		email.setSubject("Order Received");
		email.setBody("Thank you for your order");
	}

}
class OrderShippedEmailSender extends BaseEmailSender {
	@Override
	protected void writeEmail(Email email) {
			email.setSubject("Order Shipped");
			email.setBody("Ti-am trimis. Speram sa ajunga (de data asta)");
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
	private String subject;
	private String body;
	private final long id = new Random(System.nanoTime()).nextLong();
	private String sender;
	private String replyTo;
	private String to;
}
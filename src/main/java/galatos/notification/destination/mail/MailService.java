package galatos.notification.destination.mail;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import galatos.notification.destination.DestinationService;
import galatos.notification.destination.DestinationType;
import galatos.notification.request.NotificationRequest;

@Service
@Qualifier("mail")
public class MailService implements DestinationService {
	
	@Autowired
	private JavaMailSender mailSender;

    private SimpleMailMessage getMailMessage(NotificationRequest request) {
    	MailRequest mail = request.getMail();
    	String text = StringUtils.isNotBlank(mail.getText()) ? mail.getText() : request.getText() ;
    	
    	SimpleMailMessage message = new SimpleMailMessage();

        message.setText(text);
        message.setTo(mail.getTo().toArray(new String[0]));
        message.setFrom(mail.getFrom());
        
        return message;
	}
    
	@Override
	public void notify(NotificationRequest request) {
		SimpleMailMessage mailMessage = getMailMessage(request);
		
		try {
            mailSender.send(mailMessage);
            System.out.println("Email enviado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar email.");
        }
	}


	@Override
	public DestinationType getType() {
		return DestinationType.MAIL;
	}
	
}

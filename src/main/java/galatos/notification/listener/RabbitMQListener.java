package galatos.notification.listener;

import java.io.IOException;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import galatos.notification.destination.DestinationRequest;
import galatos.notification.destination.DestinationServiceFactory;
import galatos.notification.request.NotificationRequest;

@Component
public class RabbitMQListener {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	@Qualifier("requestValidator")
	private Validator requestValidator;

	@RabbitListener(bindings=@QueueBinding(
			key = "${galatos.rabbitmq.key}",
			exchange = @Exchange(durable = "${galatos.rabbitmq.exchange.durable}",
								type = "${galatos.rabbitmq.exchange.type}",
						 		value = "${galatos.rabbitmq.exchange.value}"),
			value = @Queue(value = "${galatos.rabbitmq.queue.value}",
						durable = "${galatos.rabbitmq.queue.durable}")))
	public void onMessage(String message) throws JsonParseException, JsonMappingException, IOException {
		NotificationRequest notificationRequest = mapper.readValue(message, NotificationRequest.class);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(notificationRequest, NotificationRequest.class.getSimpleName());
		requestValidator.validate(notificationRequest, errors);
		if(errors.hasErrors()) {
			throw new RuntimeException();
		}
		
		List<DestinationRequest> destinationRequests = notificationRequest.getDestinationRequests();
		destinationRequests.parallelStream()
			.forEach(destinationRequest -> DestinationServiceFactory.getNotificationService(
					destinationRequest.getType()).notify(notificationRequest));
		
	}
}

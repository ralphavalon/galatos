package galatos.notification.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import galatos.notification.destination.DestinationServiceFactory;
import galatos.notification.destination.slack.SlackRequest;
import galatos.notification.request.NotificationRequest;
import galatos.notification.service.NotificationService;
import galatos.notification.stub.TestNotificationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {RabbitMQListener.class, DestinationServiceFactory.class})
@SuppressWarnings("unchecked")
public class RabbitMQListenerTest {
	
	@MockBean
	@Qualifier("requestValidator")
	private Validator requestValidator;
	
	@MockBean
	private ObjectMapper mapper;
	
	@SpyBean
	private TestNotificationService notificationService;
	
	@SpyBean
	private RabbitMQListener listener;
	
	@TestConfiguration
	public class TestConfig {
		
		@Bean
		public NotificationService notificationService() {
			return new TestNotificationService();
		}
	}
	
	@Test
	public void whenPassesRightClass_thenShouldSupport() throws JsonParseException, JsonMappingException, IOException {
		NotificationRequest request = NotificationRequest.builder()
				.text("test message")
				.slack(SlackRequest.builder()
						.urls(Arrays.asList("url 01"))
						.build())
				.build();
		when(mapper.readValue(anyString(), any(Class.class))).thenReturn(request);
		
		try {
			listener.onMessage("ad");
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		verify(requestValidator, times(1)).validate(any(NotificationRequest.class), any(Errors.class));
		verify(notificationService, times(1)).notify(any(NotificationRequest.class));
	}
	
}

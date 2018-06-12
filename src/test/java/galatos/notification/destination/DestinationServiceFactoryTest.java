package galatos.notification.destination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import galatos.notification.destination.slack.SlackService;
import galatos.notification.exception.DestinationServiceNotFound;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DestinationService.class, DestinationServiceFactory.class, SlackService.class })
public class DestinationServiceFactoryTest {

	@Autowired
	@Qualifier("slack")
	private DestinationService destinationService;

	@MockBean
	private RestTemplate restTemplate;

	@Test
	public void whenValidDestinationType_thenShouldGetDestinationService() {
		assertThat(DestinationServiceFactory.getDestinationService(DestinationType.SLACK))
				.isEqualTo(destinationService);
	}

	@Test
	public void whenInvalidDestinationType_thenShouldThrowException() {
		Throwable thrown = catchThrowable(() -> {
			DestinationServiceFactory.getDestinationService(null);
		});

		assertThat(thrown)
			.isInstanceOf(DestinationServiceNotFound.class)
			.hasMessageContaining("Cannot found destination service implementation for type: null");

	}

}
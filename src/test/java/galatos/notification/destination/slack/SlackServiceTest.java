package galatos.notification.destination.slack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;

import galatos.notification.destination.DestinationType;
import galatos.notification.helper.JsonHelper;
import galatos.notification.request.NotificationRequest;

@SuppressWarnings("unchecked")
@RunWith(SpringRunner.class)
public class SlackServiceTest {
	
	@SpyBean
	private SlackService slackService;
	@MockBean
	private RestTemplate restTemplate;
	
	private final static String SLACK_CHANNEL_URL = "http://localhost:9090";
	
	@Captor
	private ArgumentCaptor<HttpEntity<String>> httpEntityCaptor;
	@Captor
	private ArgumentCaptor<String> fullUrlCaptor;
	
	@Test
	public void whenGetType_thenShouldGetDestinationType() {
		assertThat(slackService.getType()).isEqualTo(DestinationType.SLACK);
	}
	
	@Test
	public void whenHasDefaultTextAndUrls_thenShouldNotifyWithDefaultText() {
		NotificationRequest request = NotificationRequest.builder()
				.text("my default text")
				.slack(SlackRequest.builder()
						.urls(Arrays.asList("http://localhost:9090"))
						.build())
				.build();
		
		slackService.notify(request);
		
		verify(restTemplate, times(1)).postForObject(fullUrlCaptor.capture(), httpEntityCaptor.capture(), any(Class.class));
		
		assertRequest("notifier/slack/notify_default_text_success.json");
	}
	
	@Test
	public void whenHasDefaultTextAndSpecificTextAndUrls_thenShouldNotifyWithSpecificText() {
		NotificationRequest request = NotificationRequest.builder()
				.text("my default text")
				.slack(SlackRequest.builder()
						.urls(Arrays.asList("http://localhost:9090"))
						.text("This text is a test.")
						.build())
				.build();
		
		slackService.notify(request);
		
		verify(restTemplate, times(1)).postForObject(fullUrlCaptor.capture(), httpEntityCaptor.capture(), any(Class.class));
		
		assertRequest("notifier/slack/notify_specific_text_success.json");
	}
	
	@Test
	public void whenHasDefaultTextAndUrlsButEmptySpecificText_thenShouldNotifyWithDefaultText() {
		NotificationRequest request = NotificationRequest.builder()
				.text("my default text")
				.slack(SlackRequest.builder()
						.urls(Arrays.asList("http://localhost:9090"))
						.text("")
						.build())
				.build();
		
		slackService.notify(request);
		
		verify(restTemplate, times(1)).postForObject(fullUrlCaptor.capture(), httpEntityCaptor.capture(), any(Class.class));
		
		assertRequest("notifier/slack/notify_default_text_success.json");
	}
	
	@Test
	public void whenHasDefaultTextAndUrlsButNullSpecificText_thenShouldNotifyWithDefaultText() {
		NotificationRequest request = NotificationRequest.builder()
				.text("my default text")
				.slack(SlackRequest.builder()
						.urls(Arrays.asList("http://localhost:9090"))
						.text(null)
						.build())
				.build();
		
		slackService.notify(request);
		
		verify(restTemplate, times(1)).postForObject(fullUrlCaptor.capture(), httpEntityCaptor.capture(), any(Class.class));
		
		assertRequest("notifier/slack/notify_default_text_success.json");
	}

	private void assertRequest(String requestBodyFile) {
		assertThat(fullUrlCaptor.getValue()).isEqualTo(SLACK_CHANNEL_URL);
		
		assertRestTemplateHeaders();
		
		JsonObject expectedJson = JsonHelper.getRequestFileAsJsonObject(requestBodyFile);
		String requestJson = httpEntityCaptor.getValue().getBody();
		JsonHelper.assertJson(expectedJson.toString(), requestJson);
	}
	
	private void assertRestTemplateHeaders() {
		HttpHeaders headers = httpEntityCaptor.getValue().getHeaders();
		assertThat(headers.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
	}
	
}

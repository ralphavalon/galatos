package galatos.notification.destination.slack;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import galatos.notification.destination.DestinationService;
import galatos.notification.destination.DestinationType;
import galatos.notification.request.NotificationRequest;

@Service
@Qualifier("slack")
public class SlackService implements DestinationService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public void notify(NotificationRequest request) {
		HttpHeaders headers = getRequestHeaders();
		String requestJSON = createNotificationJson(request);
		HttpEntity<String> entity = new HttpEntity<String>(requestJSON, headers);
		notifyEachChannel(request.getSlack().getUrls(), entity);
	}

	@Override
	public DestinationType getType() {
		return DestinationType.SLACK;
	}

	private HttpHeaders getRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return headers;
	}
	
	private String createNotificationJson(NotificationRequest request) {
		SlackRequest slack = request.getSlack();
		String text = StringUtils.isNotBlank(slack.getText()) ? slack.getText() : request.getText() ; 
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("text", text);
		return jsonObject.toString();
	}
	
	private void notifyEachChannel(List<String> slackChannelsURL, HttpEntity<String> entity) {
		if(slackChannelsURL != null && slackChannelsURL.size() > 0) {
			slackChannelsURL.stream()
				.forEach(url -> restTemplate.postForObject(url, entity, String.class));
		}
	}
	
}

package galatos.notification.request;

import javax.validation.Valid;

import galatos.notification.destination.slack.SlackRequest;
import galatos.notification.validation.AtLeastOneNotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@AtLeastOneNotNull(fields= {"slack"})
public class NotificationRequest {
	
	private String text;
	@Valid
	private SlackRequest slack;
	
}

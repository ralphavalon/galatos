package galatos.notification.service;

import org.springframework.stereotype.Service;

import galatos.notification.destination.DestinationType;
import galatos.notification.request.NotificationRequest;

@Service
public class SlackService implements NotificationService {

	@Override
	public void notify(NotificationRequest request) {
		System.out.println("Slack");
	}

	@Override
	public DestinationType getType() {
		return DestinationType.SLACK;
	}

}

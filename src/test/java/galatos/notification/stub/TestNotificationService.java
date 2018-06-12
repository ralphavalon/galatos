package galatos.notification.stub;

import org.springframework.stereotype.Service;

import galatos.notification.destination.DestinationService;
import galatos.notification.destination.DestinationType;
import galatos.notification.request.NotificationRequest;

@Service
public class TestNotificationService implements DestinationService {

	@Override
	public void notify(NotificationRequest request) {
		
	}

	@Override
	public DestinationType getType() {
		return DestinationType.SLACK;
	}
	
}
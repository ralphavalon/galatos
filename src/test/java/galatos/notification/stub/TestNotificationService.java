package galatos.notification.stub;

import org.springframework.stereotype.Service;

import galatos.notification.destination.DestinationType;
import galatos.notification.request.NotificationRequest;
import galatos.notification.service.NotificationService;

@Service
public class TestNotificationService implements NotificationService {

	@Override
	public void notify(NotificationRequest request) {
		
	}

	@Override
	public DestinationType getType() {
		return DestinationType.SLACK;
	}
	
}
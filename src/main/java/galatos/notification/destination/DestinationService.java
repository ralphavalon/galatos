package galatos.notification.destination;

import galatos.notification.request.NotificationRequest;

public interface DestinationService {
	
	void notify(NotificationRequest request);
	
	DestinationType getType();

}

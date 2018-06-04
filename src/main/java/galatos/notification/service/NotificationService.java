package galatos.notification.service;

import galatos.notification.destination.DestinationType;
import galatos.notification.request.NotificationRequest;

public interface NotificationService {
	
	void notify(NotificationRequest request);
	
	DestinationType getType();

}

package galatos.notification.destination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import galatos.notification.service.NotificationService;

@Service
public class DestinationServiceFactory {
	
	@Autowired
    private List<NotificationService> providers;
	
	private static Map<DestinationType, NotificationService> notificationProviderMap = new HashMap<>();
	
	@PostConstruct
	void started() {
		for(NotificationService provider : providers) {
			notificationProviderMap.put(provider.getType(), provider);
        }
	}
	
	public static NotificationService getNotificationService(DestinationType providerName) {
		NotificationService notificationProvider = notificationProviderMap.get(providerName);
		if(notificationProvider == null) {
			throw new RuntimeException("Provider not found"); //TODO: Define an exception to be thrown
		}
		return notificationProvider;
	}

}

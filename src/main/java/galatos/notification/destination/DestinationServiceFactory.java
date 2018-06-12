package galatos.notification.destination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import galatos.notification.exception.DestinationServiceNotFound;

@Service
public class DestinationServiceFactory {
	
	@Autowired
    private List<DestinationService> providers;
	
	private static Map<DestinationType, DestinationService> destinationServiceMap = new HashMap<>();
	
	@PostConstruct
	void started() {
		for(DestinationService provider : providers) {
			destinationServiceMap.put(provider.getType(), provider);
        }
	}
	
	public static DestinationService getDestinationService(DestinationType providerName) {
		DestinationService destinationService = destinationServiceMap.get(providerName);
		if(destinationService == null) {
			throw new DestinationServiceNotFound(providerName);
		}
		return destinationService;
	}

}

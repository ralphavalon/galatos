package galatos.notification.exception;

import galatos.notification.destination.DestinationType;

public class DestinationServiceNotFound extends RuntimeException {

	private static final long serialVersionUID = 3878369951704865746L;
	
	public DestinationServiceNotFound(DestinationType destinationType) {
		super("Cannot found destination service implementation for type: " + destinationType);
	}

}

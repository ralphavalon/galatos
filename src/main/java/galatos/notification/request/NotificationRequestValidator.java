package galatos.notification.request;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import galatos.notification.destination.DestinationRequest;
import galatos.notification.validation.FieldHelper;

@Service
public class NotificationRequestValidator implements Validator {
	
	@Autowired
	private Validator validator;

	@Override
	public boolean supports(Class<?> clazz) {
		return (NotificationRequest.class.isAssignableFrom(clazz));
	}

	@Override
	public void validate(Object target, Errors errors) {
		NotificationRequest request = (NotificationRequest) target;
		validator.validate(target, errors);
		
		if(isBlank(request.getText())) {
			List<DestinationRequest> destinationRequests = request.getDestinationRequests();
			destinationRequests.stream()
				.filter(destination -> isBlank(destination.getText()))
				.forEach(destination -> errors.rejectValue(FieldHelper.getFieldName(destination, request), "ShouldHaveText", "se tiver, esta"));
		}

	}
	
}

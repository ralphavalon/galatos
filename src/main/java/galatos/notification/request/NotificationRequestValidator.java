package galatos.notification.request;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
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
			List<Field> fields = Arrays.asList(request.getClass().getDeclaredFields());
			
			fields.stream()
				.filter(field -> ClassUtils.isAssignable(field.getType(), DestinationRequest.class)
					&& isBlank(
						((DestinationRequest) FieldHelper.getField(field, request)).getText())
					)
				.forEach(field -> errors.rejectValue(field.getName(), "ShouldHaveText", "se tiver, esta"));
		}

	}
	
}

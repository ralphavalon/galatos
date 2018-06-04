package galatos.notification.request;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.ClassUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import galatos.notification.destination.DestinationRequest;
import galatos.notification.destination.slack.SlackRequest;
import galatos.notification.validation.AtLeastOneNotNull;
import galatos.notification.validation.FieldHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@AtLeastOneNotNull(fields= {"slack"})
public class NotificationRequest {
	
	private String text;
	@Valid
	private SlackRequest slack;
	
	@JsonIgnore
	public List<DestinationRequest> getDestinationRequests() {
		List<Field> fields = Arrays.asList(this.getClass().getDeclaredFields());
		
		return fields.stream()
			.filter(field -> ClassUtils.isAssignable(field.getType(), DestinationRequest.class)
				&& FieldHelper.getField(field, this) != null)
			.map(field -> (DestinationRequest) FieldHelper.getField(field, this))
			.collect(Collectors.toList());
	}
	
}

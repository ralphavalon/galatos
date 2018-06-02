package galatos.notification.destination.slack;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import galatos.notification.destination.DestinationRequest;
import galatos.notification.validation.NotBlankFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class SlackRequest implements DestinationRequest {

	@Size(min=1, message="Minimum size is {min}")
	@NotEmpty
	@NotBlankFields
	private List<String> urls;
	private String text;
	
}

package galatos.notification.destination.mail;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import galatos.notification.destination.DestinationRequest;
import galatos.notification.destination.DestinationType;
import galatos.notification.validation.NotBlankFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class MailRequest implements DestinationRequest {

	@NotEmpty @Email
	private String from;
	@Size(min=1, message="Minimum size is {min}")
	@NotEmpty
	@NotBlankFields
	private List<String> to;
	@NotEmpty
	private String subject;
	private String text;
	
	@Override @JsonIgnore
	public DestinationType getType() {
		return DestinationType.MAIL;
	}
	
}

package galatos.notification.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import galatos.notification.destination.slack.SlackRequest;
import galatos.notification.validation.AtLeastOneNotNull;
import galatos.notification.validation.NotBlankFields;

@RunWith(SpringRunner.class)
public class NotificationRequestValidatorTest {
	
	@Autowired
	@Qualifier("requestValidator")
	private Validator requestValidator;
	
	@TestConfiguration
	static class TestBeans {
		@Bean
		public NotificationRequestValidator requestValidator() {
			return new NotificationRequestValidator();
		}
		
		@Bean
		public Validator validator() {
			return new LocalValidatorFactoryBean();
		}
	}
	
	private List<ObjectError> validate(NotificationRequest request) {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, NotificationRequest.class.getSimpleName());
		requestValidator.validate(request, errors);
		return errors.getAllErrors();
	}
	
	@Test
	public void whenPassesRightClass_thenShouldSupport() {
		assertThat(requestValidator.supports(NotificationRequest.class)).isTrue();
	}
	
	@Test
	public void whenPassesAnotherClass_thenShouldNotSupport() {
		assertThat(requestValidator.supports(SlackRequest.class)).isFalse();
	}

	@Test
	public void whenHasAllAttributes_thenShouldBeValid() {
		NotificationRequest request = NotificationRequest.builder()
				.text("Mensagem enviada 2")
				.slack(SlackRequest.builder()
						.urls(Arrays.asList("asd"))
						.text("Pok")
						.build())
				.build();
		List<ObjectError> errors = validate(request);
		assertThat(errors.size()).isEqualTo(0);
		
	}
	
	@Test
	public void whenHasNoDefaultTextButAllInnersDo_thenShouldBeValid() {
		NotificationRequest request = NotificationRequest.builder()
				.slack(SlackRequest.builder()
						.urls(Arrays.asList("asd"))
						.text("Pok")
						.build())
				.build();
		List<ObjectError> errors = validate(request);
		assertThat(errors.size()).isEqualTo(0);
	}
	
	@Test
	public void whenHasDefaultTextAndAllInnersDont_thenShouldBeValid() {
		NotificationRequest request = NotificationRequest.builder()
				.text("Mensagem enviada 2")
				.slack(SlackRequest.builder()
						.urls(Arrays.asList("asd"))
						.build())
				.build();
		List<ObjectError> errors = validate(request);
		assertThat(errors.size()).isEqualTo(0);
	}
	
	@Test
	public void whenHasNoDefaultTextAndInnerDoesntHaveTextButHaveOtherRequiredParams_thenShouldNotBeValid() {
		NotificationRequest request = NotificationRequest.builder()
				.slack(SlackRequest.builder()
						.urls(Arrays.asList("asd"))
						.build())
				.build();
		List<ObjectError> errors = validate(request);
		assertThat(errors.size()).isEqualTo(1);
		
		ObjectError error = errors.get(0);
		assertThat(error.getCode()).isEqualTo("ShouldHaveText");
	}
	
	@Test
	public void whenHasNoDefaultTextAndInnerDoesntHaveRequiredParams_thenShouldNotBeValid() {
		NotificationRequest request = NotificationRequest.builder()
				.slack(SlackRequest.builder()
						.text("Pok")
						.build())
				.build();
		List<ObjectError> errors = validate(request);
		assertThat(errors.size()).isEqualTo(1);
		
		ObjectError error = errors.get(0);
		assertThat(error.getCode()).isEqualTo(NotEmpty.class.getSimpleName());
	}
	
	@Test
	public void whenHasDefaultTextButNoInner_thenShouldNotBeValid() {
		NotificationRequest request = NotificationRequest.builder()
				.text("Mensagem enviada 2")
				.build();
		List<ObjectError> errors = validate(request);
		assertThat(errors.size()).isEqualTo(1);
		
		ObjectError error = errors.get(0);
		assertThat(error.getCode()).isEqualTo(AtLeastOneNotNull.class.getSimpleName());
	}
	
	@Test
	public void whenHasDefaultTextButInnerRequiredParamsAreInvalid_thenShouldNotBeValid() {
		NotificationRequest request = NotificationRequest.builder()
				.text("Mensagem enviada 2")
				.slack(SlackRequest.builder()
						.urls(Arrays.asList(""))
						.build())
				.build();
		List<ObjectError> errors = validate(request);
		assertThat(errors.size()).isEqualTo(1);
		
		ObjectError error = errors.get(0);
		assertThat(error.getCode()).isEqualTo(NotBlankFields.class.getSimpleName());
	}
	
}

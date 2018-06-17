package galatos.notification.destination.mail;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import galatos.notification.destination.DestinationType;
import galatos.notification.validation.NotBlankFields;

@RunWith(SpringRunner.class)
public class MailRequestTest {

    @Autowired
    private Validator validator;

    @TestConfiguration
    static class TestBeans {

        @Bean
        public Validator validator() {
            return new LocalValidatorFactoryBean();
        }
    }

    private List<ObjectError> validate(MailRequest request) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request,
                MailRequest.class.getSimpleName());
        validator.validate(request, errors);
        return errors.getAllErrors();
    }

    @Test
    public void whenGetType_thenShouldGetDestinationType() {
        assertThat(MailRequest.builder().build().getType()).isEqualTo(DestinationType.MAIL);
    }

    @Test
    public void whenHasAllAttributes_thenShouldBeValid() {
        MailRequest request = MailRequest.builder()
                .from("from@mailinator.com")
                .to(Arrays.asList("to@mailinator.com"))
                .subject("My subject")
                .text("This text is a test.")
                .build();
        List<ObjectError> errors = validate(request);
        assertThat(errors.size()).isEqualTo(0);
    }

    @Test
    public void whenHasAllRequiredAttributes_thenShouldBeValid() {
        MailRequest request = MailRequest.builder()
                .from("from@mailinator.com")
                .to(Arrays.asList("to@mailinator.com"))
                .subject("My subject")
                .build();
        List<ObjectError> errors = validate(request);
        assertThat(errors.size()).isEqualTo(0);
    }

    @Test
    public void whenHasNoAttributes_thenShouldNotBeValid() {
        MailRequest request = MailRequest.builder()
                .build();
        List<ObjectError> errors = validate(request);
        assertThat(errors.size()).isEqualTo(3);
    }

    @Test
    public void whenHasRequiredParamsButFromIsNotEmail_thenShouldNotBeValid() {
        MailRequest request = MailRequest.builder()
                .from("frommailinator.com")
                .to(Arrays.asList("to@mailinator.com"))
                .subject("My subject")
                .build();
        List<ObjectError> errors = validate(request);
        assertThat(errors.size()).isEqualTo(1);

        ObjectError error = errors.get(0);
        assertThat(error.getCode()).isEqualTo(Email.class.getSimpleName());
    }

    @Test
    public void whenHasAllRequiredAttributesButToIsEmpty_thenShouldBeValid() {
        MailRequest request = MailRequest.builder()
                .from("from@mailinator.com")
                .to(Arrays.asList(""))
                .subject("My subject")
                .build();
        List<ObjectError> errors = validate(request);
        assertThat(errors.size()).isEqualTo(1);

        ObjectError error = errors.get(0);
        assertThat(error.getCode()).isEqualTo(NotBlankFields.class.getSimpleName());
    }

    @Test
    public void whenHasOnlyFromAndSubject_thenShouldNotBeValid() {
        MailRequest request = MailRequest.builder()
                .from("from@mailinator.com")
                .subject("My subject")
                .build();
        List<ObjectError> errors = validate(request);
        assertThat(errors.size()).isEqualTo(1);

        ObjectError error = errors.get(0);
        assertThat(error.getCode()).isEqualTo(NotEmpty.class.getSimpleName());
    }

    @Test
    public void whenHasOnlyToAndSubject_thenShouldNotBeValid() {
        MailRequest request = MailRequest.builder()
                .to(Arrays.asList("to@mailinator.com"))
                .subject("My subject")
                .build();
        List<ObjectError> errors = validate(request);
        assertThat(errors.size()).isEqualTo(1);

        ObjectError error = errors.get(0);
        assertThat(error.getCode()).isEqualTo(NotEmpty.class.getSimpleName());
    }

}

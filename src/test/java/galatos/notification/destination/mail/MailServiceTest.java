package galatos.notification.destination.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import galatos.notification.destination.DestinationType;
import galatos.notification.helper.JsonHelper;
import galatos.notification.request.NotificationRequest;

@RunWith(SpringRunner.class)
public class MailServiceTest {

    @SpyBean
    private MailService mailService;
    @MockBean
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    @Test
    public void whenGetType_thenShouldGetDestinationType() {
        assertThat(mailService.getType()).isEqualTo(DestinationType.MAIL);
    }

    @Test
    public void whenHasDefaultTextAndFromAndTo_thenShouldNotifyWithDefaultText() {
        NotificationRequest request = NotificationRequest.builder()
                .text("my default text")
                .mail(MailRequest.builder()
                        .from("from@mailinator.com")
                        .to(Arrays.asList("to@mailinator.com"))
                        .build())
                .build();

        mailService.notify(request);

        verify(mailSender, times(1)).send(mailMessageCaptor.capture());

        assertRequest("notifier/mail/notify_default_text_success.json");
    }

    @Test
    public void whenHasDefaultTextAndSpecificTextAndFromAndTo_thenShouldNotifyWithSpecificText() {
        NotificationRequest request = NotificationRequest.builder()
                .text("my default text")
                .mail(MailRequest.builder()
                        .from("from@mailinator.com")
                        .to(Arrays.asList("to@mailinator.com"))
                        .text("This text is a test.")
                        .build())
                .build();

        mailService.notify(request);

        verify(mailSender, times(1)).send(mailMessageCaptor.capture());

        assertRequest("notifier/mail/notify_specific_text_success.json");
    }

    @Test
    public void whenHasDefaultTextAndFromAndToButEmptySpecificText_thenShouldNotifyWithDefaultText() {
        NotificationRequest request = NotificationRequest.builder()
                .text("my default text")
                .mail(MailRequest.builder()
                        .from("from@mailinator.com")
                        .to(Arrays.asList("to@mailinator.com"))
                        .text("")
                        .build())
                .build();

        mailService.notify(request);

        verify(mailSender, times(1)).send(mailMessageCaptor.capture());

        assertRequest("notifier/mail/notify_default_text_success.json");
    }

    @Test
    public void whenHasDefaultTextAndFromAndToButNullSpecificText_thenShouldNotifyWithDefaultText() {
        NotificationRequest request = NotificationRequest.builder()
                .text("my default text")
                .mail(MailRequest.builder()
                        .from("from@mailinator.com")
                        .to(Arrays.asList("to@mailinator.com"))
                        .text(null)
                        .build())
                .build();

        mailService.notify(request);

        verify(mailSender, times(1)).send(mailMessageCaptor.capture());

        assertRequest("notifier/mail/notify_default_text_success.json");
    }

    private void assertRequest(String requestBodyFile) {
        JsonObject expectedJson = JsonHelper.getRequestFileAsJsonObject(requestBodyFile);
        String requestJson = "{}";
        try {
            requestJson = new ObjectMapper().writeValueAsString(mailMessageCaptor.getValue());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonHelper.assertJson(expectedJson.toString(), requestJson);
    }

}

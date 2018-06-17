package galatos.notification.helper;

import java.util.Arrays;

import galatos.notification.destination.slack.SlackRequest;

public class SlackRequestHelper {

    public static SlackRequest getValidSlackRequest() {
        return SlackRequest.builder()
                .urls(Arrays.asList("http://localhost:9090"))
                .text("This text is a test.")
                .build();
    }

}

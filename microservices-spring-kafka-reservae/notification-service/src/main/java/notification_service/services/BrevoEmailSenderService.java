package notification_service.services;

import notification_service.templates.EmailAttachment;
import notification_service.templates.EmailTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailSenderService {

    private final RestClient restClient;
    private final String senderEmail;
    private final String senderName;

    public BrevoEmailSenderService(
        RestClient.Builder restClientBuilder,
        @Value("${brevo.api.url}") String apiUrl,
        @Value("${brevo.api.key}") String apiKey,
        @Value("${notification.email.sender-email}") String senderEmail,
        @Value("${notification.email.sender-name}") String senderName
    ) {
        this.restClient = restClientBuilder
            .baseUrl(apiUrl)
            .defaultHeader("api-key", apiKey)
            .defaultHeader("accept", MediaType.APPLICATION_JSON_VALUE)
            .build();

        this.senderEmail = senderEmail;
        this.senderName = senderName;
    }

    public void send(
        String recipientEmail,
        String recipientName,
        EmailTemplate template
    ) {
        send(
            recipientEmail,
            recipientName,
            template,
            List.of()
        );
    }

    public void send(
        String recipientEmail,
        String recipientName,
        EmailTemplate template,
        List<EmailAttachment> attachments
    ) {
        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put(
            "sender",
            Map.of(
                "name", senderName,
                "email", senderEmail
            )
        );

        requestBody.put(
            "to",
            List.of(
                Map.of(
                    "name", recipientName,
                    "email", recipientEmail
                )
            )
        );

        requestBody.put("subject", template.subject());
        requestBody.put("htmlContent", template.html());

        if (attachments != null && !attachments.isEmpty()) {
            requestBody.put(
                "attachment",
                attachments.stream()
                    .map(attachment -> Map.of(
                        "name", attachment.name(),
                        "content", attachment.content()
                    ))
                    .toList()
            );
        }

        restClient.post()
            .uri("/smtp/email")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .toBodilessEntity();
    }

    public String getSenderName() {
        return senderName;
    }
}
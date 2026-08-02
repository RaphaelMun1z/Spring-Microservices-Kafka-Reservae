package notification_service.templates;

import org.springframework.stereotype.Component;

@Component
public class EventChangedEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/event-changed-hero.png";

    private final EmailLayout emailLayout;

    public EventChangedEmailTemplate(
        EmailLayout emailLayout
    ) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        String customerName,
        String eventName,
        String previousInformation,
        String newInformation,
        String changeDescription,
        String senderName
    ) {
        String body = """
            <p style="
                margin: 0 0 20px;
                color: #374151;
                font-size: 18px;
                line-height: 1.8;
            ">
                Olá, <strong>%s</strong>.
            </p>
            
            <p style="
                margin: 0 0 26px;
                color: #4b5563;
                font-size: 17px;
                line-height: 1.8;
            ">
                Uma informação do evento <strong>%s</strong> foi atualizada.
            </p>
            
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="
                    margin: 0 0 18px;
                    background-color: #f7f7f8;
                "
            >
                <tr>
                    <td style="padding: 20px 24px 8px;">
                        <div style="%s">Informação anterior</div>
                    </td>
                </tr>
            
                <tr>
                    <td style="
                        padding: 0 24px 20px;
                        color: #4b5563;
                        font-size: 16px;
                        line-height: 1.7;
                    ">
                        %s
                    </td>
                </tr>
            </table>
            
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="
                    margin: 0 0 24px;
                    background-color: #fff5f3;
                "
            >
                <tr>
                    <td style="padding: 20px 24px 8px;">
                        <div style="
                            color: #ff5a49;
                            font-size: 13px;
                            font-weight: 700;
                            text-transform: uppercase;
                            letter-spacing: 0.5px;
                        ">
                            Nova informação
                        </div>
                    </td>
                </tr>
            
                <tr>
                    <td style="
                        padding: 0 24px 20px;
                        color: #111827;
                        font-size: 17px;
                        line-height: 1.7;
                        font-weight: 700;
                    ">
                        %s
                    </td>
                </tr>
            </table>
            
            <p style="
                margin: 0;
                color: #4b5563;
                font-size: 16px;
                line-height: 1.8;
                white-space: pre-line;
            ">
                %s
            </p>
            """.formatted(
            emailLayout.escapeHtml(customerName),
            emailLayout.escapeHtml(eventName),
            labelStyle(),
            emailLayout.escapeHtml(previousInformation),
            emailLayout.escapeHtml(newInformation),
            emailLayout.escapeHtml(changeDescription)
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "Uma informação do seu evento foi atualizada.",
            "Reservae • Atualização de evento",
            "Evento atualizado",
            "Confira as novas informações",
            HERO_IMAGE,
            "Ilustração de alteração de evento",
            body,
            null,
            null,
            "Verifique os dados atualizados antes de comparecer ao evento.",
            senderName
        );

        return new EmailTemplate(
            "Evento atualizado — Reservae",
            emailLayout.render(layoutData)
        );
    }

    private String labelStyle() {
        return """
            color: #6b7280;
            font-size: 13px;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            """;
    }
}
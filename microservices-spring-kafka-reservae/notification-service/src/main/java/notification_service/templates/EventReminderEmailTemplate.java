package notification_service.templates;

import org.springframework.stereotype.Component;

@Component
public class EventReminderEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/event-reminder-hero.png";

    private final EmailLayout emailLayout;

    public EventReminderEmailTemplate(
        EmailLayout emailLayout
    ) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        String customerName,
        String eventName,
        String date,
        String time,
        String location,
        String sector,
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
                O evento <strong>%s</strong> está se aproximando.
                Confira os principais detalhes antes de sair.
            </p>
            
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="background-color: #f7f7f8;"
            >
                <tr>
                    <td style="padding: 22px 24px 10px;">
                        <div style="
                            color: #ff5a49;
                            font-size: 13px;
                            font-weight: 700;
                            text-transform: uppercase;
                            letter-spacing: 0.5px;
                        ">
                            Informações do evento
                        </div>
                    </td>
                </tr>
            
                <tr>
                    <td style="padding: 0 24px 24px;">
                        <table
                            role="presentation"
                            width="100%%"
                            cellspacing="0"
                            cellpadding="0"
                            border="0"
                        >
                            <tr>
                                <td style="%s">Data</td>
                                <td align="right" style="%s">%s</td>
                            </tr>
            
                            <tr>
                                <td style="%s">Horário</td>
                                <td align="right" style="%s">%s</td>
                            </tr>
            
                            <tr>
                                <td style="%s">Local</td>
                                <td align="right" style="%s">%s</td>
                            </tr>
            
                            <tr>
                                <td style="%s">Setor</td>
                                <td align="right" style="%s">%s</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            
            <p style="
                margin: 24px 0 0;
                color: #6b7280;
                font-size: 15px;
                line-height: 1.8;
            ">
                Deixe seu ingresso preparado antes de chegar ao local
                e confira antecipadamente o trajeto.
            </p>
            """.formatted(
            emailLayout.escapeHtml(customerName),
            emailLayout.escapeHtml(eventName),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(date),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(time),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(location),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(sector)
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "Seu evento está se aproximando.",
            "Reservae • Lembrete de evento",
            "Seu evento está chegando",
            "Confira os detalhes para aproveitar a experiência",
            HERO_IMAGE,
            "Ilustração de lembrete de evento",
            body,
            null,
            null,
            "Tenha seu ingresso disponível antes de chegar ao local.",
            senderName
        );

        return new EmailTemplate(
            "Lembrete do evento — Reservae",
            emailLayout.render(layoutData)
        );
    }

    private String detailLabelStyle() {
        return """
            padding: 9px 0;
            color: #6b7280;
            font-size: 14px;
            line-height: 1.5;
            vertical-align: top;
            """;
    }

    private String detailValueStyle() {
        return """
            padding: 9px 0;
            color: #111827;
            font-size: 14px;
            line-height: 1.5;
            font-weight: 700;
            vertical-align: top;
            """;
    }
}
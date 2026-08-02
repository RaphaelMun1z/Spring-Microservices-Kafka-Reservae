package notification_service.templates;

import org.springframework.stereotype.Component;

@Component
public class EventCanceledEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/event-canceled-hero.png";

    private final EmailLayout emailLayout;

    public EventCanceledEmailTemplate(
        EmailLayout emailLayout
    ) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        String customerName,
        String eventName,
        String orderId,
        String refundInstructions,
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
                Informamos que o evento <strong>%s</strong> foi cancelado.
            </p>
            
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="
                    margin: 0 0 26px;
                    background-color: #f7f7f8;
                "
            >
                <tr>
                    <td style="padding: 20px 24px 8px;">
                        <div style="%s">
                            Pedido relacionado
                        </div>
                    </td>
                </tr>
            
                <tr>
                    <td style="
                        padding: 0 24px 20px;
                        color: #111827;
                        font-size: 18px;
                        font-weight: 700;
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
                style="background-color: #fff5f3;"
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
                            Orientações sobre o reembolso
                        </div>
                    </td>
                </tr>
            
                <tr>
                    <td style="
                        padding: 0 24px 24px;
                        color: #7f1d1d;
                        font-size: 16px;
                        line-height: 1.8;
                        white-space: pre-line;
                    ">
                        %s
                    </td>
                </tr>
            </table>
            """.formatted(
            emailLayout.escapeHtml(customerName),
            emailLayout.escapeHtml(eventName),
            labelStyle(),
            emailLayout.escapeHtml(orderId),
            emailLayout.escapeHtml(refundInstructions)
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "O evento relacionado ao seu pedido foi cancelado.",
            "Reservae • Evento cancelado",
            "Evento cancelado",
            "Confira as orientações relacionadas ao seu pedido",
            HERO_IMAGE,
            "Ilustração de evento cancelado",
            body,
            null,
            null,
            "Acompanhe o status do pedido e do reembolso pela plataforma Reservae.",
            senderName
        );

        return new EmailTemplate(
            "Evento cancelado — Reservae",
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
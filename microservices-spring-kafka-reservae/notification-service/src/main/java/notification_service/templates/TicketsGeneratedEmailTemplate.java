package notification_service.templates;

import notification_service.messaging.event.TicketGeneratedEvent;
import org.springframework.stereotype.Component;

@Component
public class TicketsGeneratedEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/tickets-generated-hero.png";

    private final EmailLayout emailLayout;

    public TicketsGeneratedEmailTemplate(
        EmailLayout emailLayout
    ) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        TicketGeneratedEvent event,
        String senderName
    ) {
        String ticketsHtml = buildTicketsHtml(event);

        String body = """
            <p style="
                margin: 0 0 20px;
                color: #374151;
                font-size: 18px;
                line-height: 1.8;
            ">
                Seus ingressos foram gerados e já estão disponíveis.
            </p>
            
            <p style="
                margin: 0 0 26px;
                color: #4b5563;
                font-size: 17px;
                line-height: 1.8;
            ">
                Os QR Codes estão anexados individualmente a esta mensagem.
                Guarde-os em um local seguro e apresente o código correspondente
                no acesso ao evento.
            </p>
            
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="
                    margin: 0 0 24px;
                    background-color: #f7f7f8;
                "
            >
                <tr>
                    <td style="padding: 20px 24px 8px;">
                        <div style="
                            color: #6b7280;
                            font-size: 13px;
                            font-weight: 700;
                            text-transform: uppercase;
                            letter-spacing: 0.5px;
                        ">
                            Pedido
                        </div>
                    </td>
                </tr>
            
                <tr>
                    <td style="
                        padding: 0 24px 20px;
                        color: #111827;
                        font-size: 18px;
                        line-height: 1.5;
                        font-weight: 700;
                    ">
                        %s
                    </td>
                </tr>
            </table>
            
            <div style="
                margin: 0 0 14px;
                color: #111827;
                font-size: 20px;
                line-height: 1.4;
                font-weight: 700;
            ">
                Ingressos deste pedido
            </div>
            
            %s
            
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="
                    margin: 26px 0 0;
                    background-color: #fff5f3;
                "
            >
                <tr>
                    <td style="
                        padding: 22px 24px;
                        color: #9f3a2f;
                        font-size: 15px;
                        line-height: 1.8;
                    ">
                        <strong>Importante</strong><br>
                        Cada QR Code representa um ingresso individual.
                        Não compartilhe os códigos publicamente e apresente apenas
                        o ingresso correspondente no momento da validação.
                    </td>
                </tr>
            </table>
            """.formatted(
            emailLayout.escapeHtml(event.orderId()),
            ticketsHtml
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "Seus ingressos e QR Codes já estão disponíveis.",
            "Reservae • Ingressos emitidos",
            "Seus ingressos estão prontos",
            "Apresente os QR Codes anexados na entrada do evento",
            HERO_IMAGE,
            "Ilustração de ingressos disponíveis",
            body,
            null,
            null,
            "Os QR Codes anexados são individuais e devem ser mantidos em segurança.",
            senderName
        );

        return new EmailTemplate(
            "Seus ingressos estão disponíveis — Reservae",
            emailLayout.render(layoutData)
        );
    }

    private String buildTicketsHtml(
        TicketGeneratedEvent event
    ) {
        if (event.items() == null || event.items().isEmpty()) {
            return """
                <table
                    role="presentation"
                    width="100%%"
                    cellspacing="0"
                    cellpadding="0"
                    border="0"
                    style="background-color: #f7f7f8;"
                >
                    <tr>
                        <td style="
                            padding: 22px 24px;
                            color: #6b7280;
                            font-size: 15px;
                            line-height: 1.7;
                        ">
                            Nenhum ingresso foi informado para este pedido.
                        </td>
                    </tr>
                </table>
                """;
        }

        StringBuilder ticketsHtml = new StringBuilder();

        for (int index = 0; index < event.items().size(); index++) {
            var item = event.items().get(index);

            ticketsHtml.append("""
                <table
                    role="presentation"
                    width="100%%"
                    cellspacing="0"
                    cellpadding="0"
                    border="0"
                    style="
                        margin: 0 0 12px;
                        background-color: #f7f7f8;
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
                                Ingresso %d
                            </div>
                        </td>
                    </tr>
                
                    <tr>
                        <td style="padding: 0 24px 20px;">
                            <table
                                role="presentation"
                                width="100%%"
                                cellspacing="0"
                                cellpadding="0"
                                border="0"
                            >
                                <tr>
                                    <td style="%s">
                                        Ticket ID
                                    </td>
                                    <td align="right" style="%s">
                                        %s
                                    </td>
                                </tr>
                
                                <tr>
                                    <td style="%s">
                                        Setor
                                    </td>
                                    <td align="right" style="%s">
                                        %s
                                    </td>
                                </tr>
                
                                <tr>
                                    <td style="%s">
                                        Tipo
                                    </td>
                                    <td align="right" style="%s">
                                        %s
                                    </td>
                                </tr>
                
                                <tr>
                                    <td style="%s">
                                        QR Code
                                    </td>
                                    <td align="right" style="%s">
                                        Anexado ao e-mail
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                """.formatted(
                index + 1,

                ticketLabelStyle(),
                ticketValueStyle(),
                emailLayout.escapeHtml(
                    String.valueOf(item.ticketId())
                ),

                ticketLabelStyle(),
                ticketValueStyle(),
                emailLayout.escapeHtml(
                    String.valueOf(item.sectorId())
                ),

                ticketLabelStyle(),
                ticketValueStyle(),
                emailLayout.escapeHtml(
                    formatTicketType(item.ticketType())
                ),

                ticketLabelStyle(),
                ticketValueStyle()
            ));
        }

        return ticketsHtml.toString();
    }

    private String formatTicketType(Object ticketType) {
        if (ticketType == null) {
            return "Não informado";
        }

        String value = ticketType.toString();

        return switch (value) {
            case "FULL_TICKET_PRICE" -> "Inteira";
            case "HALF_TICKET_PRICE" -> "Meia-entrada";
            default -> value;
        };
    }

    private String ticketLabelStyle() {
        return """
            padding: 8px 0;
            color: #6b7280;
            font-size: 14px;
            line-height: 1.5;
            vertical-align: top;
            """;
    }

    private String ticketValueStyle() {
        return """
            padding: 8px 0;
            color: #111827;
            font-size: 14px;
            line-height: 1.5;
            font-weight: 700;
            vertical-align: top;
            """;
    }
}
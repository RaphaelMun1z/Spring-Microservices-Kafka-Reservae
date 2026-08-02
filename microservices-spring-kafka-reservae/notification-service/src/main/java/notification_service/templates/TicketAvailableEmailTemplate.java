package notification_service.templates;

import org.springframework.stereotype.Component;

@Component
public class TicketAvailableEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/tickets-generated-hero.png";

    private final EmailLayout emailLayout;

    public TicketAvailableEmailTemplate(
        EmailLayout emailLayout
    ) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        String customerName,
        String orderId,
        String eventName,
        String sectorName,
        String quantity,
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
                Seu ingresso foi gerado e já está disponível na plataforma Reservae.
            </p>
            
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="
                    margin: 0;
                    background-color: #f7f7f8;
                "
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
                            Detalhes do ingresso
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
                                <td style="%s">Pedido</td>
                                <td align="right" style="%s">%s</td>
                            </tr>
            
                            <tr>
                                <td style="%s">Evento</td>
                                <td align="right" style="%s">%s</td>
                            </tr>
            
                            <tr>
                                <td style="%s">Setor</td>
                                <td align="right" style="%s">%s</td>
                            </tr>
            
                            <tr>
                                <td style="%s">Quantidade</td>
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
                Acesse sua conta para consultar os detalhes e apresentar o ingresso
                no momento da entrada.
            </p>
            """.formatted(
            emailLayout.escapeHtml(customerName),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(orderId),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(eventName),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(sectorName),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(quantity)
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "Seu ingresso já está disponível.",
            "Reservae • Ingresso disponível",
            "Ingresso disponível",
            "Seu acesso ao evento já pode ser consultado",
            HERO_IMAGE,
            "Ilustração de ingresso disponível",
            body,
            null,
            null,
            "Mantenha os dados do seu ingresso em segurança.",
            senderName
        );

        return new EmailTemplate(
            "Ingresso disponível — Reservae",
            emailLayout.render(layoutData)
        );
    }

    private String detailLabelStyle() {
        return """
            padding: 9px 0;
            color: #6b7280;
            font-size: 14px;
            line-height: 1.5;
            """;
    }

    private String detailValueStyle() {
        return """
            padding: 9px 0;
            color: #111827;
            font-size: 14px;
            line-height: 1.5;
            font-weight: 700;
            """;
    }
}
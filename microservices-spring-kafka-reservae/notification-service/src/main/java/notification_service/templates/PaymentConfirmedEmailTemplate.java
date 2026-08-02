package notification_service.templates;

import org.springframework.stereotype.Component;

@Component
public class PaymentConfirmedEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/payment-confirmed-hero.png";

    private final EmailLayout emailLayout;

    public PaymentConfirmedEmailTemplate(
        EmailLayout emailLayout
    ) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        String customerName,
        String orderId,
        String eventName,
        String eventDate,
        String totalAmount,
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
                Seu pagamento foi confirmado com sucesso.
                O pedido está sendo finalizado e os ingressos serão disponibilizados
                assim que o processamento for concluído.
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
                    <td style="padding: 22px 24px 10px;">
                        <div style="
                            color: #ff5a49;
                            font-size: 13px;
                            font-weight: 700;
                            text-transform: uppercase;
                            letter-spacing: 0.5px;
                        ">
                            Compra confirmada
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
                                <td style="%s">
                                    Pedido
                                </td>
                                <td align="right" style="%s">
                                    %s
                                </td>
                            </tr>
            
                            <tr>
                                <td style="%s">
                                    Evento
                                </td>
                                <td align="right" style="%s">
                                    %s
                                </td>
                            </tr>
            
                            <tr>
                                <td style="%s">
                                    Data
                                </td>
                                <td align="right" style="%s">
                                    %s
                                </td>
                            </tr>
            
                            <tr>
                                <td style="%s">
                                    Total pago
                                </td>
                                <td align="right" style="
                                    padding: 9px 0;
                                    color: #ff5a49;
                                    font-size: 16px;
                                    line-height: 1.5;
                                    font-weight: 700;
                                ">
                                    %s
                                </td>
                            </tr>
                        </table>
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
                    margin: 0;
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
                        <strong>Próxima etapa</strong><br>
                        Você receberá uma nova mensagem quando os ingressos e seus
                        respectivos QR Codes estiverem disponíveis.
                    </td>
                </tr>
            </table>
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
            emailLayout.escapeHtml(eventDate),

            emailLayout.escapeHtml(totalAmount)
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "Seu pagamento foi confirmado com sucesso.",
            "Reservae • Pagamento confirmado",
            "Pagamento confirmado",
            "Sua compra foi processada com sucesso",
            HERO_IMAGE,
            "Ilustração de pagamento confirmado",
            body,
            null,
            null,
            "Você receberá outra mensagem quando seus ingressos estiverem disponíveis.",
            senderName
        );

        return new EmailTemplate(
            "Pagamento confirmado — Reservae",
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
package notification_service.templates;

import org.springframework.stereotype.Component;

@Component
public class PaymentFailedEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/payment-failed-hero.png";

    private final EmailLayout emailLayout;

    public PaymentFailedEmailTemplate(
        EmailLayout emailLayout
    ) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        String customerName,
        String orderId,
        String eventName,
        String reason,
        String paymentUrl,
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
                Não foi possível confirmar o pagamento do seu pedido.
                Revise as informações abaixo e, caso o link ainda esteja disponível,
                realize uma nova tentativa.
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
                            Pagamento não confirmado
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
                    <td style="padding: 22px 24px 8px;">
                        <div style="
                            color: #9f3a2f;
                            font-size: 13px;
                            font-weight: 700;
                            text-transform: uppercase;
                            letter-spacing: 0.5px;
                        ">
                            Motivo informado
                        </div>
                    </td>
                </tr>
            
                <tr>
                    <td style="
                        padding: 0 24px 22px;
                        color: #7f1d1d;
                        font-size: 16px;
                        line-height: 1.8;
                    ">
                        %s
                    </td>
                </tr>
            </table>
            
            <p style="
                margin: 24px 0 0;
                color: #6b7280;
                font-size: 15px;
                line-height: 1.8;
            ">
                Nenhuma nova cobrança será realizada sem que você inicie
                outra tentativa de pagamento.
            </p>
            """.formatted(
            emailLayout.escapeHtml(customerName),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(orderId),

            detailLabelStyle(),
            detailValueStyle(),
            emailLayout.escapeHtml(eventName),

            emailLayout.escapeHtml(reason)
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "Não foi possível confirmar o pagamento do seu pedido.",
            "Reservae • Problema no pagamento",
            "Pagamento não confirmado",
            "Revise os dados e tente novamente",
            HERO_IMAGE,
            "Ilustração de falha no pagamento",
            body,
            hasText(paymentUrl)
                ? "Tentar novamente"
                : null,
            hasText(paymentUrl)
                ? paymentUrl
                : null,
            "Caso tenha dúvidas, consulte o status atualizado do pedido na plataforma Reservae.",
            senderName
        );

        return new EmailTemplate(
            "Pagamento não confirmado — Reservae",
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

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
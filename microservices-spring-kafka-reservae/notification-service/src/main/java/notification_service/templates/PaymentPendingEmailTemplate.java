package notification_service.templates;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentPendingEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/payment-pending-hero.png";

    private final EmailLayout emailLayout;

    public PaymentPendingEmailTemplate(EmailLayout emailLayout) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        String customerName,
        String orderId,
        String paymentExpiresAt,
        String totalAmount,
        List<EmailItem> items,
        String paymentUrl,
        String orderUrl,
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
                margin: 0 0 24px;
                color: #4b5563;
                font-size: 17px;
                line-height: 1.8;
            ">
                Seu pedido foi criado e está aguardando o pagamento.
                Finalize a compra dentro do prazo para garantir os seus ingressos.
            </p>
            
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="background-color: #f7f7f8; margin-bottom: 24px;"
            >
                <tr>
                    <td style="padding: 18px 22px 10px;">
                        <div style="
                            color: #6b7280;
                            font-size: 13px;
                            text-transform: uppercase;
                            font-weight: 700;
                            letter-spacing: 0.4px;
                        ">
                            Resumo do pedido
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 0 22px 22px;">
                        <table
                            role="presentation"
                            width="100%%"
                            cellspacing="0"
                            cellpadding="0"
                            border="0"
                        >
                            <tr>
                                <td style="padding: 7px 0; color: #6b7280; font-size: 14px;">
                                    Pedido
                                </td>
                                <td align="right" style="padding: 7px 0; color: #111827; font-size: 14px; font-weight: 700;">
                                    %s
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 7px 0; color: #6b7280; font-size: 14px;">
                                    Expira em
                                </td>
                                <td align="right" style="padding: 7px 0; color: #111827; font-size: 14px; font-weight: 700;">
                                    %s
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 7px 0; color: #6b7280; font-size: 14px;">
                                    Total
                                </td>
                                <td align="right" style="padding: 7px 0; color: #ff5a49; font-size: 16px; font-weight: 700;">
                                    %s
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            
            %s
            
            %s
            """.formatted(
            emailLayout.escapeHtml(customerName),
            emailLayout.escapeHtml(orderId),
            emailLayout.escapeHtml(paymentExpiresAt),
            emailLayout.escapeHtml(totalAmount),
            buildItemsTable(items),
            buildOrderLink(orderUrl)
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "Seu pedido está aguardando pagamento.",
            "Reservae • Pedido em aberto",
            "Finalize seu pagamento",
            "Garanta seus ingressos antes do vencimento",
            HERO_IMAGE,
            "Ilustração de pagamento pendente",
            body,
            "Realizar pagamento",
            paymentUrl,
            "Caso você já tenha finalizado o pagamento, desconsidere esta mensagem.",
            senderName
        );

        return new EmailTemplate(
            "Pagamento pendente — Reservae",
            emailLayout.render(layoutData)
        );
    }

    private String buildItemsTable(List<EmailItem> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }

        StringBuilder rows = new StringBuilder();

        for (EmailItem item : items) {
            rows.append("""
                <tr>
                    <td style="
                        padding: 16px 0;
                        border-bottom: 1px solid #eceef2;
                    ">
                        <div style="
                            color: #111827;
                            font-size: 16px;
                            font-weight: 700;
                            line-height: 1.5;
                        ">
                            %s
                        </div>
                        <div style="
                            color: #6b7280;
                            font-size: 14px;
                            line-height: 1.7;
                            margin-top: 4px;
                        ">
                            Setor: %s • Quantidade: %s
                        </div>
                    </td>
                    <td align="right" style="
                        padding: 16px 0;
                        border-bottom: 1px solid #eceef2;
                    ">
                        <div style="
                            color: #111827;
                            font-size: 15px;
                            font-weight: 700;
                        ">
                            %s
                        </div>
                        <div style="
                            color: #6b7280;
                            font-size: 13px;
                            margin-top: 4px;
                        ">
                            %s por ingresso
                        </div>
                    </td>
                </tr>
                """.formatted(
                emailLayout.escapeHtml(item.eventName()),
                emailLayout.escapeHtml(item.sectorName()),
                item.quantity() == null ? "0" : item.quantity(),
                emailLayout.escapeHtml(item.subtotal()),
                emailLayout.escapeHtml(item.unitPrice())
            ));
        }

        return """
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="margin: 12px 0 0;"
            >
                %s
            </table>
            """.formatted(rows);
    }

    private String buildOrderLink(String orderUrl) {
        if (orderUrl == null || orderUrl.isBlank()) {
            return "";
        }

        return """
            <p style="
                margin: 22px 0 0;
                color: #6b7280;
                font-size: 14px;
                line-height: 1.7;
            ">
                Acompanhe o status do pedido:
                <a
                    href="%s"
                    target="_blank"
                    style="
                        color: #ff5a49;
                        text-decoration: none;
                        font-weight: 700;
                    "
                >
                    consultar pedido
                </a>
            </p>
            """.formatted(emailLayout.escapeHtml(orderUrl));
    }
}
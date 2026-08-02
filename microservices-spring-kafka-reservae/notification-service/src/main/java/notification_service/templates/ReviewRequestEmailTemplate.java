package notification_service.templates;

import org.springframework.stereotype.Component;

@Component
public class ReviewRequestEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/review-request-hero.png";

    private final EmailLayout emailLayout;

    public ReviewRequestEmailTemplate(
        EmailLayout emailLayout
    ) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        String customerName,
        String eventName,
        String orderId,
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
                Esperamos que você tenha aproveitado o evento
                <strong>%s</strong>.
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
            ">
                Sua avaliação ajuda organizadores e outros participantes
                a terem experiências melhores dentro da plataforma Reservae.
            </p>
            """.formatted(
            emailLayout.escapeHtml(customerName),
            emailLayout.escapeHtml(eventName),
            emailLayout.escapeHtml(orderId)
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "Conte como foi sua experiência no evento.",
            "Reservae • Avaliação",
            "Como foi sua experiência?",
            "Compartilhe sua opinião sobre o evento",
            HERO_IMAGE,
            "Ilustração de solicitação de avaliação",
            body,
            null,
            null,
            "Sua opinião é importante para melhorar a experiência na plataforma.",
            senderName
        );

        return new EmailTemplate(
            "Avalie sua experiência — Reservae",
            emailLayout.render(layoutData)
        );
    }
}
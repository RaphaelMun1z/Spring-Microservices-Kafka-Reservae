package notification_service.templates;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ManualNotificationEmailTemplate {

    private static final String HERO_IMAGE =
        "classpath:email-assets/manual-notification-hero.png";

    private final EmailLayout emailLayout;

    public ManualNotificationEmailTemplate(EmailLayout emailLayout) {
        this.emailLayout = emailLayout;
    }

    public EmailTemplate build(
        String recipientName,
        String title,
        String message,
        String senderName
    ) {
        // Formata a data atual para exibição no cabeçalho (opcional, como visto na referência)
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Mensagem principal centralizada
        String body = """
            <p style="margin: 0 0 16px;">
                Olá, <strong>%s</strong>!
            </p>
            
            <div style="
                background-color: #f9fafb;
                border: 1px dashed #d1d5db;
                border-radius: 8px;
                padding: 24px;
                margin: 24px 0;
                text-align: center;
                color: #4b5563;
                font-size: 16px;
                line-height: 1.8;
                white-space: pre-line;
            ">
                %s
            </div>
            
            <p style="
                margin: 24px 0 0;
                color: #9ca3af;
                font-size: 14px;
                line-height: 1.6;
            ">
                Caso necessário, acompanhe novas atualizações<br>diretamente na plataforma Reservae.
            </p>
            """.formatted(
            emailLayout.escapeHtml(recipientName),
            emailLayout.escapeHtml(message)
        );

        EmailLayoutData layoutData = new EmailLayoutData(
            "Você recebeu uma nova notificação do Reservae.",
            currentDate, // Utilizando data no topo ao invés do texto estático, como na referência
            title,       // Título forte centralizado no novo Layout
            null,        // Sem subtítulo explícito, direto para o conteúdo
            HERO_IMAGE,
            "Ilustração da notificação",
            body,
            "Acessar Reservae", // Você pode alterar ou passar null se não quiser botão
            "https://app.reservae.com.br", // Link de exemplo
            "Esta é uma comunicação oficial da plataforma Reservae.",
            senderName
        );

        return new EmailTemplate(
            title + " — Reservae",
            emailLayout.render(layoutData)
        );
    }
}
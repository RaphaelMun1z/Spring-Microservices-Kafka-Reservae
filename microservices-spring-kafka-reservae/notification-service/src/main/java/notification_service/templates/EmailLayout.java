package notification_service.templates;

import notification_service.services.EmailAssetService;
import org.springframework.stereotype.Component;

@Component
public class EmailLayout {

    private final EmailAssetService emailAssetService;

    public EmailLayout(EmailAssetService emailAssetService) {
        this.emailAssetService = emailAssetService;
    }

    public String render(EmailLayoutData data) {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="color-scheme" content="light">
                <meta name="supported-color-schemes" content="light">
                <title>%s</title>
            </head>
            <body style="
                margin: 0;
                padding: 0;
                background-color: #f4f5f7;
                font-family: 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
                color: #111827;
                -webkit-font-smoothing: antialiased;
            ">
                <div style="
                    display: none;
                    max-height: 0;
                    overflow: hidden;
                    opacity: 0;
                    color: transparent;
                ">
                    %s
                </div>
            
                <table
                    role="presentation"
                    width="100%%"
                    cellspacing="0"
                    cellpadding="0"
                    border="0"
                    style="background-color: #f4f5f7; padding: 40px 16px;"
                >
                    <tr>
                        <td align="center">
                            <!-- Início do Card Principal -->
                            <table
                                role="presentation"
                                width="100%%"
                                cellspacing="0"
                                cellpadding="0"
                                border="0"
                                style="
                                    max-width: 600px;
                                    background-color: #ffffff;
                                    border-radius: 16px;
                                    border-top: 6px solid #ff5a49;
                                    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
                                    border-collapse: separate;
                                    overflow: hidden;
                                "
                            >
                                %s
                                %s
                                %s
                            </table>
                            <!-- Fim do Card Principal -->
            
                            <!-- Footer Externo -->
                            %s
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(
            escapeHtml(data.title()),
            escapeHtml(data.preheader()),
            buildHeader(data),
            buildHeroSection(data),
            buildBodySection(data),
            buildFooter(data)
        );
    }

    private String buildHeader(EmailLayoutData data) {
        String headerContext = safe(data.headerContext());

        return """
            <tr>
                <td style="padding: 32px 40px 10px; background-color: #ffffff;">
                    <table
                        role="presentation"
                        width="100%%"
                        cellspacing="0"
                        cellpadding="0"
                        border="0"
                    >
                        <tr>
                            <td valign="middle" align="left">
                                <img
                                    src="%s"
                                    alt="Reservae"
                                    style="
                                        display: block;
                                        max-width: 130px;
                                        height: auto;
                                    "
                                >
                            </td>
                            <td valign="middle" align="right" style="
                                color: #9ca3af;
                                font-size: 13px;
                                font-weight: 600;
                                white-space: nowrap;
                            ">
                                %s
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            """.formatted(
            emailAssetService.getLogoDataUri(),
            escapeHtml(headerContext)
        );
    }

    private String buildHeroSection(EmailLayoutData data) {
        if (isBlank(data.heroImageClasspath())) {
            return "";
        }

        String heroImageDataUri = emailAssetService.getAssetDataUri(
            data.heroImageClasspath()
        );

        return """
            <tr>
                <td align="center" style="padding: 20px 40px 10px; background-color: #ffffff;">
                    <img
                        src="%s"
                        alt="%s"
                        style="
                            display: block;
                            max-width: 220px;
                            width: 100%%;
                            height: auto;
                            margin: 0 auto;
                        "
                    >
                </td>
            </tr>
            """.formatted(
            heroImageDataUri,
            escapeHtml(safe(data.heroImageAlt()))
        );
    }

    private String buildBodySection(EmailLayoutData data) {
        String titleBlock = "";
        if (!isBlank(data.title())) {
            titleBlock = """
                <h1 style="
                    margin: 0 0 8px;
                    color: #111827;
                    font-size: 24px;
                    line-height: 1.3;
                    font-weight: 800;
                    text-align: center;
                ">
                    %s
                </h1>
                """.formatted(escapeHtml(data.title()));
        }

        String subtitleBlock = "";
        if (!isBlank(data.subtitle())) {
            subtitleBlock = """
                <p style="
                    margin: 0 0 24px;
                    color: #6b7280;
                    font-size: 16px;
                    line-height: 1.5;
                    text-align: center;
                ">
                    %s
                </p>
                """.formatted(escapeHtml(data.subtitle()));
        }

        String primaryButtonBlock = "";
        if (!isBlank(data.primaryButtonLabel()) && !isBlank(data.primaryButtonUrl())) {
            primaryButtonBlock = """
                <table
                    role="presentation"
                    width="100%%"
                    cellspacing="0"
                    cellpadding="0"
                    border="0"
                    style="margin: 32px 0 10px;"
                >
                    <tr>
                        <td align="center">
                            <a
                                href="%s"
                                target="_blank"
                                style="
                                    display: inline-block;
                                    padding: 14px 36px;
                                    background-color: #ff5a49;
                                    color: #ffffff;
                                    text-decoration: none;
                                    font-size: 16px;
                                    font-weight: 700;
                                    border-radius: 8px;
                                    text-align: center;
                                "
                            >
                                %s
                            </a>
                        </td>
                    </tr>
                </table>
                """.formatted(
                escapeHtml(data.primaryButtonUrl()),
                escapeHtml(data.primaryButtonLabel())
            );
        }

        return """
            <tr>
                <td style="padding: 10px 40px 40px; background-color: #ffffff;">
                    %s
                    %s
            
                    <!-- Conteúdo Específico Injetado -->
                    <div style="text-align: center; color: #4b5563; font-size: 15px; line-height: 1.6;">
                        %s
                    </div>
            
                    %s
                </td>
            </tr>
            """.formatted(
            titleBlock,
            subtitleBlock,
            safe(data.bodyHtml()),
            primaryButtonBlock
        );
    }

    private String buildFooter(EmailLayoutData data) {
        String footerNote = safe(data.footerNote());

        if (footerNote.isBlank()) {
            footerNote = "Esta é uma mensagem automática. Não responda diretamente a este e-mail.";
        }

        return """
            <table
                role="presentation"
                width="100%%"
                cellspacing="0"
                cellpadding="0"
                border="0"
                style="max-width: 600px; margin-top: 24px;"
            >
                <tr>
                    <td align="center" style="
                        color: #9ca3af;
                        font-size: 12px;
                        line-height: 1.6;
                    ">
                        <p style="margin: 0 0 8px;">%s</p>
                        <p style="margin: 0;">
                            Enviado por <strong style="color: #6b7280;">%s</strong>
                        </p>
                    </td>
                </tr>
            </table>
            """.formatted(
            escapeHtml(footerNote),
            escapeHtml(data.senderName())
        );
    }

    public String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
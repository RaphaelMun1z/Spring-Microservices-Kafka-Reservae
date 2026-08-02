package notification_service.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailFormattingService {

    private static final Locale BRAZILIAN_LOCALE =
        Locale.of("pt", "BR");

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String formatMoney(BigDecimal value) {
        if (value == null) {
            return "R$ 0,00";
        }

        return NumberFormat
            .getCurrencyInstance(BRAZILIAN_LOCALE)
            .format(value);
    }

    public String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Não informado";
        }

        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Não informada";
        }

        return dateTime.format(DATE_FORMATTER);
    }

    public String safeText(String value) {
        if (value == null || value.isBlank()) {
            return "Não informado";
        }

        return value;
    }
}
package event_catalog_service.exceptions;

import event_catalog_service.exceptions.models.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(
                fieldName,
                errorMessage
            );
        });

        return new ResponseEntity<>(
            errors,
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<ExceptionResponse> badCredentialsException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(
        AccessDeniedException ex,
        WebRequest request
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of("Acesso Negado. Você não tem permissão para executar esta ação."),
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ExceptionResponse> notFoundException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<ExceptionResponse> businessException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ExceptionResponse> illegalArgumentException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex, WebRequest request) {

        String nomeParametro = ex.getName();
        String valorRecebido = ex.getValue() != null ? ex.getValue().toString() : "null";
        String tipoEsperado = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido";

        String mensagem = String.format(
            "O valor '%s' enviado para o parâmetro '%s' é inválido. O tipo esperado é '%s'.",
            valorRecebido,
            nomeParametro,
            tipoEsperado
        );

        ExceptionResponse response = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of(mensagem),
            request.getDescription(false)
        );

        return new ResponseEntity<>(
            response,
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public final ResponseEntity<ExceptionResponse> invalidDataAccess(
        InvalidDataAccessApiUsageException ex,
        WebRequest request
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of("Filtro inválido para consulta. Verifique os parâmetros informados."),
            request.getDescription(false)
        );

        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ExceptionResponse> dataIntegrityViolationException(
        DataIntegrityViolationException ex,
        WebRequest request
    ) {
        String mensagemAmigavel = "Ocorreu um erro de integridade dos dados. Verifique os campos informados.";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Throwable causaRaiz = ex.getMostSpecificCause();
        String mensagemCausa = causaRaiz.getMessage().toLowerCase();

        if (mensagemCausa.contains("unique constraint") || mensagemCausa.contains("duplicate entry") || mensagemCausa.contains("viola a restrição de unicidade")) {

            mensagemAmigavel = "Recurso já existe.";
            status = HttpStatus.CONFLICT;
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of(mensagemAmigavel),
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            status
        );
    }

    @ExceptionHandler(DuplicatedResourceException.class)
    public final ResponseEntity<ExceptionResponse> duplicatedResourceException(
        DuplicatedResourceException ex,
        WebRequest request
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ExceptionResponse> constraintViolationException(
        ConstraintViolationException ex,
        WebRequest request
    ) {
        List<String> mensagens = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            mensagens,
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> invalidJwtAuthenticationException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error(
            "Ocorreu uma exceção não tratada",
            ex
        );

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now().toString(),
            List.of("Ocorreu um erro inesperado no servidor. Por favor, tente novamente mais tarde."),
            request.getDescription(false)
        );
        return new ResponseEntity<>(
            exceptionResponse,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

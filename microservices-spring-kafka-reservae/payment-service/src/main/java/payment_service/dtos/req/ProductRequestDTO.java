package payment_service.dtos.req;

import jakarta.validation.constraints.NotBlank;

public record ProductRequestDTO(
    @NotBlank(message = "O identificador do pedido é obrigatório.")
    String orderId,

    @NotBlank(message = "O identificador do usuário é obrigatório.")
    String userId
) {
}
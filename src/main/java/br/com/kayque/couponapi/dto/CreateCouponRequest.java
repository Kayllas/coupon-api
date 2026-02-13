package br.com.kayque.couponapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateCouponRequest(

    @NotBlank
    @Schema(example = "A@1-2#B$3C", description = "Código do cupom (caracteres especiais permitidos; será normalizado para 6 caracteres alfanuméricos).")
    String code,

    @NotBlank
    @Schema(example = "Black Friday 50% OFF", description = "Descrição do cupom")
    String description,

    @NotNull
    @DecimalMin("0.5")
    @Schema(example = "10.5", minimum = "0.5", description = "Valor do desconto")
    BigDecimal discountValue,

    @NotNull
    @Schema(type = "string", format = "date-time", example = "2027-12-31T23:59:59.000Z", description = "Data de expiração do cupom (deve ser no futuro)")
    Instant expirationDate,

    @Schema(example = "false", description = "Opcional. Padrão é false.")
    Boolean published
) {}

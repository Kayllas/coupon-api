package br.com.kayque.couponapi.domain.vo;

import br.com.kayque.couponapi.domain.exception.DomainValidationException;

public record CouponCode(String value) {

    public CouponCode {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Código do cupom é obrigatório");
        }

        // Normaliza o código do cupom para conter apenas caracteres alfanuméricos em maiúsculas
        String normalizedValue = value.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (normalizedValue.length() != 6) {
            throw new DomainValidationException("Código do cupom deve conter 6 caracteres alfanuméricos");
        }

        value = normalizedValue;
    }
}
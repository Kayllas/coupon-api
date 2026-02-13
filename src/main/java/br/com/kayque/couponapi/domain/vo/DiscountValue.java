package br.com.kayque.couponapi.domain.vo;

import br.com.kayque.couponapi.domain.exception.DomainValidationException;
import java.math.BigDecimal;

public record DiscountValue(BigDecimal value) {

    // O valor de desconto do cupom possui um saldo mínimo de 0,5 sem máximo predeterminado.
    private static final BigDecimal MIN_DISCOUNT = new BigDecimal("0.5");

    public DiscountValue {
        if (value == null) {
            throw new DomainValidationException("Valor de desconto é obrigatório");
        }
        if (value.compareTo(MIN_DISCOUNT) < 0) {
            throw new DomainValidationException("Valor de desconto deve ser no mínimo " + MIN_DISCOUNT);
        }
    }
}
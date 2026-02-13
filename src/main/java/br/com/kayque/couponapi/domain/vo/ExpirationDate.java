package br.com.kayque.couponapi.domain.vo;

import br.com.kayque.couponapi.domain.exception.DomainValidationException;
import java.time.Instant;

public record ExpirationDate(Instant value) {

    public ExpirationDate {
        if (value == null) {
            throw new DomainValidationException("Data de expiração é obrigatória");
        }
        if (value.isBefore(Instant.now())) {
            throw new DomainValidationException("Data de expiração deve ser uma data futura");
        }
    }
}
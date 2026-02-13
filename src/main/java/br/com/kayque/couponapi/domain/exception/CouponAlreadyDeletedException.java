package br.com.kayque.couponapi.domain.exception;

import java.util.UUID;

public class CouponAlreadyDeletedException extends DomainValidationException {
  public CouponAlreadyDeletedException(UUID id) {
    super("Cupom já deletado: " + id);
  }
}

package br.com.kayque.couponapi.usecase.exception;

import java.util.UUID;

public class CouponNotFoundException extends RuntimeException {
  public CouponNotFoundException(UUID id) {
    super("Cupom não encontrado: " + id);
  }
}

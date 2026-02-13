package br.com.kayque.couponapi.usecase;

import br.com.kayque.couponapi.domain.model.Coupon;
import br.com.kayque.couponapi.usecase.exception.CouponNotFoundException;
import br.com.kayque.couponapi.usecase.port.CouponRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetCouponByIdUseCase {

  private final CouponRepositoryPort repository;

  public GetCouponByIdUseCase(CouponRepositoryPort repository) {
    this.repository = repository;
  }

  public Coupon execute(UUID id) {
    return repository.findById(id).orElseThrow(() -> new CouponNotFoundException(id));
  }
}

package br.com.kayque.couponapi.usecase;

import br.com.kayque.couponapi.domain.model.Coupon;
import br.com.kayque.couponapi.usecase.exception.CouponNotFoundException;
import br.com.kayque.couponapi.usecase.port.CouponRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeleteCouponUseCase {

  private final CouponRepositoryPort repository;

  public DeleteCouponUseCase(CouponRepositoryPort repository) {
    this.repository = repository;
  }

  public void execute(UUID id) {
    Coupon coupon = repository.findById(id).orElseThrow(() -> new CouponNotFoundException(id));
    coupon.delete();
    repository.save(coupon);
  }
}

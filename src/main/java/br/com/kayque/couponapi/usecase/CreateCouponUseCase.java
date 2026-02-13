package br.com.kayque.couponapi.usecase;

import br.com.kayque.couponapi.domain.model.Coupon;
import br.com.kayque.couponapi.domain.vo.CouponCode;
import br.com.kayque.couponapi.domain.vo.DiscountValue;
import br.com.kayque.couponapi.domain.vo.ExpirationDate;
import br.com.kayque.couponapi.usecase.command.CreateCouponCommand;
import br.com.kayque.couponapi.usecase.port.CouponRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class CreateCouponUseCase {

  private final CouponRepositoryPort repository;

  public CreateCouponUseCase(CouponRepositoryPort repository) {
    this.repository = repository;
  }

  public Coupon execute(CreateCouponCommand cmd) {
    Coupon coupon = Coupon.create(
        new CouponCode(cmd.code()),
        cmd.description(),
        new DiscountValue(cmd.discountValue()),
        new ExpirationDate(cmd.expirationDate()),
        cmd.published()
    );

    return repository.save(coupon);
  }
}

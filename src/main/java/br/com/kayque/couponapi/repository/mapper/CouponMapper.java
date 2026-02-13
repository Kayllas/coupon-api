package br.com.kayque.couponapi.repository.mapper;

import br.com.kayque.couponapi.domain.model.Coupon;
import br.com.kayque.couponapi.domain.vo.CouponCode;
import br.com.kayque.couponapi.domain.vo.DiscountValue;
import br.com.kayque.couponapi.domain.vo.ExpirationDate;
import br.com.kayque.couponapi.repository.model.CouponEntity;

public final class CouponMapper {

  private CouponMapper() {}

  public static CouponEntity toEntity(Coupon domain) {
    return new CouponEntity(
        domain.getId(),
        domain.getCode().value(),
        domain.getDescription(),
        domain.getDiscountValue().value(),
        domain.getExpirationDate().value(),
        domain.getStatus(),
        domain.isPublished(),
        domain.isRedeemed()
    );
  }

  public static Coupon toDomain(CouponEntity entity) {
    return Coupon.restore(
        entity.getId(),
        new CouponCode(entity.getCode()),
        entity.getDescription(),
        new DiscountValue(entity.getDiscountValue()),
        new ExpirationDate(entity.getExpirationDate()),
        entity.getStatus(),
        entity.isPublished(),
        entity.isRedeemed()
    );
  }
}

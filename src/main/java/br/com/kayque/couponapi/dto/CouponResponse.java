package br.com.kayque.couponapi.dto;

import br.com.kayque.couponapi.domain.enums.CouponStatus;
import br.com.kayque.couponapi.domain.model.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CouponResponse(
    UUID id,
    String code,
    String description,
    BigDecimal discountValue,
    Instant expirationDate,
    CouponStatus status,
    boolean published,
    @Schema(example = "false", description = "Indica se o cupom foi resgatado")
    boolean redeemed
) {
  public static CouponResponse from(Coupon c) {
    return new CouponResponse(
        c.getId(),
        c.getCode().value(),
        c.getDescription(),
        c.getDiscountValue().value(),
        c.getExpirationDate().value(),
        c.getStatus(),
        c.isPublished(),
        c.isRedeemed()
    );
  }
}

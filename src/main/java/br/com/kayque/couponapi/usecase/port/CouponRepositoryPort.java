package br.com.kayque.couponapi.usecase.port;

import br.com.kayque.couponapi.domain.model.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepositoryPort {
    Coupon save(Coupon coupon);

    Optional<Coupon> findById(UUID id);
}

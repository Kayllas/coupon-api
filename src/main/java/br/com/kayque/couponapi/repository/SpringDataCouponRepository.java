package br.com.kayque.couponapi.repository;

import br.com.kayque.couponapi.repository.model.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataCouponRepository extends JpaRepository<CouponEntity, UUID> {
}

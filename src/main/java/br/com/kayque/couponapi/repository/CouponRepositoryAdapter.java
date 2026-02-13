package br.com.kayque.couponapi.repository;

import br.com.kayque.couponapi.repository.mapper.CouponMapper;
import br.com.kayque.couponapi.repository.model.CouponEntity;
import br.com.kayque.couponapi.usecase.port.CouponRepositoryPort;
import br.com.kayque.couponapi.domain.model.Coupon;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CouponRepositoryAdapter implements CouponRepositoryPort {

  private final SpringDataCouponRepository repository;

  public CouponRepositoryAdapter(SpringDataCouponRepository repository) {
    this.repository = repository;
  }

  @Override
  public Coupon save(Coupon coupon) {
    CouponEntity saved = repository.save(CouponMapper.toEntity(coupon));
    return CouponMapper.toDomain(saved);
  }

  @Override
  public Optional<Coupon> findById(UUID id) {
    return repository.findById(id).map(CouponMapper::toDomain);
  }
}

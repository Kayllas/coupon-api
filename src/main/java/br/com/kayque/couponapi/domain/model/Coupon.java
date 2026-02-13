package br.com.kayque.couponapi.domain.model;

import br.com.kayque.couponapi.domain.enums.CouponStatus;
import br.com.kayque.couponapi.domain.exception.CouponAlreadyDeletedException;
import br.com.kayque.couponapi.domain.exception.DomainValidationException;
import br.com.kayque.couponapi.domain.vo.CouponCode;
import br.com.kayque.couponapi.domain.vo.DiscountValue;
import br.com.kayque.couponapi.domain.vo.ExpirationDate;

import java.util.UUID;

public class Coupon {
    private final UUID id;
    private final CouponCode code;
    private final String description;
    private final DiscountValue discountValue;
    private final ExpirationDate expirationDate;
    private CouponStatus status;
    private final boolean published;
    private boolean redeemed;

    private Coupon(UUID id, CouponCode code, String description, DiscountValue discountValue,
                    ExpirationDate expirationDate, CouponStatus status, boolean published, boolean redeemed) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = status;
        this.published = published;
        this.redeemed = redeemed;
    }

    public static Coupon create(CouponCode code, String description, DiscountValue discount, ExpirationDate expirationDate, boolean published) {
        if (description == null || description.isBlank()) throw new DomainValidationException("Descrição do cupom é obrigatória");

        CouponStatus status = published ? CouponStatus.ACTIVE : CouponStatus.INACTIVE;
        return new Coupon(UUID.randomUUID(), code, description, discount, expirationDate, status, published, false);
    }

    public static Coupon restore(UUID id, CouponCode code, String description, DiscountValue discount, ExpirationDate expirationDate, CouponStatus status, boolean published, boolean redeemed) {
        if (id == null) throw new DomainValidationException("ID do cupom é obrigatório");
        if (status == null) throw new DomainValidationException("Status do cupom é obrigatório");

        if (description == null || description.isBlank()) throw new DomainValidationException("Descrição do cupom é obrigatória");

        return new Coupon(id, code, description, discount, expirationDate, status, published, redeemed);
    }

    public void delete() {
        if (this.status == CouponStatus.DELETED) throw new CouponAlreadyDeletedException(this.id);
        this.status = CouponStatus.DELETED;
    }

    public UUID getId() { return id; }
    public CouponCode getCode() { return code; }
    public String getDescription() { return description; }
    public DiscountValue getDiscountValue() { return discountValue; }
    public ExpirationDate getExpirationDate() { return expirationDate; }
    public CouponStatus getStatus() { return status; }
    public boolean isPublished() { return published; }
    public boolean isRedeemed() { return redeemed; }
}

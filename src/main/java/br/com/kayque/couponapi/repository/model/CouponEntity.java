package br.com.kayque.couponapi.repository.model;

import br.com.kayque.couponapi.domain.enums.CouponStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@jakarta.persistence.Table(name = "coupon")
public class CouponEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private Instant expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean redeemed;

    protected CouponEntity() {};

    public CouponEntity(UUID id, String code, String description, BigDecimal discountValue,
                    Instant expirationDate, CouponStatus status, boolean published, boolean redeemed) {
    this.id = id;
    this.code = code;
    this.description = description;
    this.discountValue = discountValue;
    this.expirationDate = expirationDate;
    this.status = status;
    this.published = published;
    this.redeemed = redeemed;
}

    public UUID getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public Instant getExpirationDate() { return expirationDate; }
    public CouponStatus getStatus() { return status; }
    public boolean isPublished() { return published; }
    public boolean isRedeemed() { return redeemed; }

    public void setStatus(CouponStatus status) { this.status = status; }
}


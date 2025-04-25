package com.rafalwizen.restaurantcouponsserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coupon name is required")
    @Size(max = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @NotNull(message = "Discount value is required")
    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discountValue;

    @NotNull(message = "Valid from date is required")
    @Column(name = "valid_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validFrom;

    @NotNull(message = "Valid to date is required")
    @Column(name = "valid_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validTo;

    @Column(name = "terms_and_conditions", length = 1000)
    private String termsAndConditions;

    @NotNull
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
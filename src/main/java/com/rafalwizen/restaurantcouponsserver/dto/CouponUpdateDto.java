package com.rafalwizen.restaurantcouponsserver.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponUpdateDto {
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Positive(message = "Discount value must be positive")
    private BigDecimal discountValue;

    private Date validFrom;
    private Date validTo;

    @Size(max = 1000, message = "Terms and conditions cannot exceed 1000 characters")
    private String termsAndConditions;

    private Boolean isActive;
}
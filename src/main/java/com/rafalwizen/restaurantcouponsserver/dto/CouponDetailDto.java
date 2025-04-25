package com.rafalwizen.restaurantcouponsserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDetailDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal discountValue;
    private Date validFrom;
    private Date validTo;
    private String termsAndConditions;
    private Boolean isActive;
    private Long imageId;
    private String imageUrl;
}
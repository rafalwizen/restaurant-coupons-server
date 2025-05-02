package com.rafalwizen.restaurantcouponsserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponSummaryDto {
    private Long id;
    private String name;
    private BigDecimal discountValue;
    private Long imageId;
    private String imageUrl;
}
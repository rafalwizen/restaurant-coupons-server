
package com.rafalwizen.restaurantcouponsserver.controller;

import com.rafalwizen.restaurantcouponsserver.dto.ApiResponse;
import com.rafalwizen.restaurantcouponsserver.dto.CouponDetailDto;
import com.rafalwizen.restaurantcouponsserver.dto.CouponSummaryDto;
import com.rafalwizen.restaurantcouponsserver.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@Tag(name = "Public Coupon API", description = "API endpoints for public coupon access")
public class PublicCouponController {

    private final CouponService couponService;

    @Autowired
    public PublicCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    @Operation(summary = "Get all active coupons", description = "Retrieve a paginated list of all active coupons")
    public ResponseEntity<ApiResponse<Page<CouponSummaryDto>>> getAllActiveCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<CouponSummaryDto> coupons = couponService.getAllActiveCoupons(pageable);
        return ResponseEntity.ok(ApiResponse.success("Active coupons retrieved successfully", coupons));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coupon by ID", description = "Retrieve detailed information about a specific coupon")
    public ResponseEntity<ApiResponse<CouponDetailDto>> getCouponById(@PathVariable Long id) {
        CouponDetailDto coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(ApiResponse.success("Coupon retrieved successfully", coupon));
    }
}
package com.rafalwizen.restaurantcouponsserver.controller;

import com.rafalwizen.restaurantcouponsserver.dto.*;
import com.rafalwizen.restaurantcouponsserver.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/coupons")
@Tag(name = "Admin Coupon API", description = "API endpoints for admin coupon management")
@SecurityRequirement(name = "bearerAuth")
public class AdminCouponController {

    private final CouponService couponService;

    @Autowired
    public AdminCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    @Operation(summary = "Get all coupons", description = "Retrieve a paginated list of all coupons")
    public ResponseEntity<ApiResponse<Page<CouponSummaryDto>>> getAllCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<CouponSummaryDto> coupons = couponService.getAllCoupons(pageable);
        return ResponseEntity.ok(ApiResponse.success("Coupons retrieved successfully", coupons));
    }

    @PostMapping
    @Operation(summary = "Create new coupon", description = "Create a new coupon with the provided information")
    public ResponseEntity<ApiResponse<CouponDetailDto>> createCoupon(@Valid @RequestBody CouponCreateDto couponDto) {
        CouponDetailDto createdCoupon = couponService.createCoupon(couponDto);
        return new ResponseEntity<>(ApiResponse.success("Coupon created successfully", createdCoupon), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update coupon", description = "Update an existing coupon with the provided information")
    public ResponseEntity<ApiResponse<CouponDetailDto>> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody CouponUpdateDto couponDto) {
        CouponDetailDto updatedCoupon = couponService.updateCoupon(id, couponDto);
        return ResponseEntity.ok(ApiResponse.success("Coupon updated successfully", updatedCoupon));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete coupon", description = "Delete a coupon by its ID")
    public ResponseEntity<ApiResponse<?>> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(ApiResponse.success("Coupon deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coupon by ID (Admin)", description = "Retrieve detailed information about a specific coupon (Admin access)")
    public ResponseEntity<ApiResponse<CouponDetailDto>> getAdminCouponById(@PathVariable Long id) {
        CouponDetailDto coupon = couponService.getAdminCouponById(id);
        return ResponseEntity.ok(ApiResponse.success("Coupon retrieved successfully", coupon));
    }
}
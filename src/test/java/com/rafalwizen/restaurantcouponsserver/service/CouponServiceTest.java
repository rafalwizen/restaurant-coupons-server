package com.rafalwizen.restaurantcouponsserver.service;

import com.rafalwizen.restaurantcouponsserver.dto.CouponCreateDto;
import com.rafalwizen.restaurantcouponsserver.dto.CouponDetailDto;
import com.rafalwizen.restaurantcouponsserver.dto.CouponUpdateDto;
import com.rafalwizen.restaurantcouponsserver.dto.CouponSummaryDto;
import com.rafalwizen.restaurantcouponsserver.exception.ResourceNotFoundException;
import com.rafalwizen.restaurantcouponsserver.model.Coupon;
import com.rafalwizen.restaurantcouponsserver.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    private Coupon coupon;
    private CouponCreateDto couponCreateDto;
    private CouponUpdateDto couponUpdateDto;
    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();
        Date tomorrow = new Date(now.getTime() + 86400000);

        coupon = new Coupon();
        coupon.setId(1L);
        coupon.setName("Test Coupon");
        coupon.setDescription("Test Description");
        coupon.setDiscountValue(new BigDecimal("10.00"));
        coupon.setValidFrom(now);
        coupon.setValidTo(tomorrow);
        coupon.setTermsAndConditions("Test Terms");
        coupon.setIsActive(true);

        couponCreateDto = new CouponCreateDto();
        couponCreateDto.setName("Test Coupon");
        couponCreateDto.setDescription("Test Description");
        couponCreateDto.setDiscountValue(new BigDecimal("10.00"));
        couponCreateDto.setValidFrom(now);
        couponCreateDto.setValidTo(tomorrow);
        couponCreateDto.setTermsAndConditions("Test Terms");
        couponCreateDto.setIsActive(true);

        couponUpdateDto = new CouponUpdateDto();
        couponUpdateDto.setName("Updated Coupon");
        couponUpdateDto.setDiscountValue(new BigDecimal("15.00"));
    }

    @Test
    void getAllActiveCoupons_ShouldReturnPageOfCouponSummaryDto() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Coupon> couponPage = new PageImpl<>(Arrays.asList(coupon));

        when(couponRepository.findAllActiveCoupons(any(Date.class), eq(pageable))).thenReturn(couponPage);

        // Act
        Page<CouponSummaryDto> result = couponService.getAllActiveCoupons(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(coupon.getId(), result.getContent().get(0).getId());
        assertEquals(coupon.getName(), result.getContent().get(0).getName());
        assertEquals(coupon.getDiscountValue(), result.getContent().get(0).getDiscountValue());

        verify(couponRepository, times(1)).findAllActiveCoupons(any(Date.class), eq(pageable));
    }

    @Test
    void getCouponById_WithValidId_ShouldReturnCouponDetailDto() {
        // Arrange
        when(couponRepository.findActiveById(eq(1L), any(Date.class))).thenReturn(coupon);

        // Act
        CouponDetailDto result = couponService.getCouponById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(coupon.getId(), result.getId());
        assertEquals(coupon.getName(), result.getName());
        assertEquals(coupon.getDescription(), result.getDescription());

        verify(couponRepository, times(1)).findActiveById(eq(1L), any(Date.class));
    }

    @Test
    void getCouponById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(couponRepository.findActiveById(eq(999L), any(Date.class))).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            couponService.getCouponById(999L);
        });

        verify(couponRepository, times(1)).findActiveById(eq(999L), any(Date.class));
    }

    @Test
    void createCoupon_ShouldReturnCreatedCouponDetailDto() {
        // Arrange
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        // Act
        CouponDetailDto result = couponService.createCoupon(couponCreateDto);

        // Assert
        assertNotNull(result);
        assertEquals(coupon.getId(), result.getId());
        assertEquals(coupon.getName(), result.getName());
        assertEquals(coupon.getDescription(), result.getDescription());

        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    void updateCoupon_WithValidId_ShouldReturnUpdatedCouponDetailDto() {
        // Arrange
        Coupon updatedCoupon = new Coupon();
        updatedCoupon.setId(1L);
        updatedCoupon.setName("Updated Coupon");
        updatedCoupon.setDescription("Test Description");
        updatedCoupon.setDiscountValue(new BigDecimal("15.00"));
        updatedCoupon.setValidFrom(now);
        updatedCoupon.setValidTo(new Date(now.getTime() + 86400000));
        updatedCoupon.setTermsAndConditions("Test Terms");
        updatedCoupon.setIsActive(true);

        when(couponRepository.findById(eq(1L))).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(updatedCoupon);

        // Act
        CouponDetailDto result = couponService.updateCoupon(1L, couponUpdateDto);

        // Assert
        assertNotNull(result);
        assertEquals(updatedCoupon.getId(), result.getId());
        assertEquals(updatedCoupon.getName(), result.getName());
        assertEquals(updatedCoupon.getDiscountValue(), result.getDiscountValue());

        verify(couponRepository, times(1)).findById(eq(1L));
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    void updateCoupon_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(couponRepository.findById(eq(999L))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            couponService.updateCoupon(999L, couponUpdateDto);
        });

        verify(couponRepository, times(1)).findById(eq(999L));
        verify(couponRepository, never()).save(any(Coupon.class));
    }

    @Test
    void deleteCoupon_WithValidId_ShouldDeleteCoupon() {
        // Arrange
        when(couponRepository.existsById(eq(1L))).thenReturn(true);
        doNothing().when(couponRepository).deleteById(eq(1L));

        // Act
        couponService.deleteCoupon(1L);

        // Assert
        verify(couponRepository, times(1)).existsById(eq(1L));
        verify(couponRepository, times(1)).deleteById(eq(1L));
    }

    @Test
    void deleteCoupon_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(couponRepository.existsById(eq(999L))).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            couponService.deleteCoupon(999L);
        });

        verify(couponRepository, times(1)).existsById(eq(999L));
        verify(couponRepository, never()).deleteById(any());
    }
}
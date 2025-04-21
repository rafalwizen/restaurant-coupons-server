package com.rafalwizen.restaurantcouponsserver.service;

import com.rafalwizen.restaurantcouponsserver.dto.CouponCreateDto;
import com.rafalwizen.restaurantcouponsserver.dto.CouponDetailDto;
import com.rafalwizen.restaurantcouponsserver.dto.CouponSummaryDto;
import com.rafalwizen.restaurantcouponsserver.dto.CouponUpdateDto;
import com.rafalwizen.restaurantcouponsserver.exception.ResourceNotFoundException;
import com.rafalwizen.restaurantcouponsserver.model.Coupon;
import com.rafalwizen.restaurantcouponsserver.repository.CouponRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Page<CouponSummaryDto> getAllActiveCoupons(Pageable pageable) {
        Page<Coupon> coupons = couponRepository.findAllActiveCoupons(new Date(), pageable);
        return coupons.map(this::convertToSummaryDto);
    }

    public CouponDetailDto getCouponById(Long id) {
        Coupon coupon = couponRepository.findActiveById(id, new Date());
        if (coupon == null) {
            throw new ResourceNotFoundException("Coupon not found with id: " + id);
        }
        return convertToDetailDto(coupon);
    }

    public CouponDetailDto getAdminCouponById(Long id) {
        return couponRepository.findById(id)
                .map(this::convertToDetailDto)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
    }

    @Transactional
    public CouponDetailDto createCoupon(CouponCreateDto couponDto) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDto, coupon);
        Coupon savedCoupon = couponRepository.save(coupon);
        return convertToDetailDto(savedCoupon);
    }

    @Transactional
    public CouponDetailDto updateCoupon(Long id, CouponUpdateDto couponDto) {
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));

        if (couponDto.getName() != null) {
            existingCoupon.setName(couponDto.getName());
        }
        if (couponDto.getDescription() != null) {
            existingCoupon.setDescription(couponDto.getDescription());
        }
        if (couponDto.getDiscountValue() != null) {
            existingCoupon.setDiscountValue(couponDto.getDiscountValue());
        }
        if (couponDto.getValidFrom() != null) {
            existingCoupon.setValidFrom(couponDto.getValidFrom());
        }
        if (couponDto.getValidTo() != null) {
            existingCoupon.setValidTo(couponDto.getValidTo());
        }
        if (couponDto.getTermsAndConditions() != null) {
            existingCoupon.setTermsAndConditions(couponDto.getTermsAndConditions());
        }
        if (couponDto.getIsActive() != null) {
            existingCoupon.setIsActive(couponDto.getIsActive());
        }

        Coupon updatedCoupon = couponRepository.save(existingCoupon);
        return convertToDetailDto(updatedCoupon);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coupon not found with id: " + id);
        }
        couponRepository.deleteById(id);
    }

    private CouponSummaryDto convertToSummaryDto(Coupon coupon) {
        CouponSummaryDto dto = new CouponSummaryDto();
        dto.setId(coupon.getId());
        dto.setName(coupon.getName());
        dto.setDiscountValue(coupon.getDiscountValue());
        return dto;
    }

    private CouponDetailDto convertToDetailDto(Coupon coupon) {
        CouponDetailDto dto = new CouponDetailDto();
        BeanUtils.copyProperties(coupon, dto);
        return dto;
    }
}
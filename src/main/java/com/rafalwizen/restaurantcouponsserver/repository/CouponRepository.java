package com.rafalwizen.restaurantcouponsserver.repository;

import com.rafalwizen.restaurantcouponsserver.model.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c WHERE c.isActive = true AND c.validFrom <= ?1 AND c.validTo >= ?1")
    Page<Coupon> findAllActiveCoupons(Date currentDate, Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.isActive = true AND c.validFrom <= ?1 AND c.validTo >= ?1")
    List<Coupon> findAllActiveCoupons(Date currentDate);

    @Query("SELECT c FROM Coupon c WHERE c.id = ?1 AND c.isActive = true AND c.validFrom <= ?2 AND c.validTo >= ?2")
    Coupon findActiveById(Long id, Date currentDate);
}
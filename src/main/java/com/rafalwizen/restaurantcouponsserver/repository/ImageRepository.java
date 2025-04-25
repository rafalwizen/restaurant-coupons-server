package com.rafalwizen.restaurantcouponsserver.repository;

import com.rafalwizen.restaurantcouponsserver.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
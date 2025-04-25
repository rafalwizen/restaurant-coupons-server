package com.rafalwizen.restaurantcouponsserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
	private Long id;
	private String fileName;
	private String fileType;
	private Long fileSize;
	private String description;
	private Date createdAt;
}
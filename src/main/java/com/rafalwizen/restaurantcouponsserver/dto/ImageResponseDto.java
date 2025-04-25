package com.rafalwizen.restaurantcouponsserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseDto {
	private Long id;
	private String fileName;
	private String fileType;
	private Long fileSize;
	private String description;
	private String url;
}
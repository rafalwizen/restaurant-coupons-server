package com.rafalwizen.restaurantcouponsserver.controller;

import com.rafalwizen.restaurantcouponsserver.dto.ApiResponse;
import com.rafalwizen.restaurantcouponsserver.dto.ImageResponseDto;
import com.rafalwizen.restaurantcouponsserver.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Image API", description = "API endpoints for managing images")
public class ImageController {

	private final ImageService imageService;

	@Autowired
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

	@GetMapping
	@Operation(summary = "Get all images", description = "Retrieve a list of all images")
	public ResponseEntity<ApiResponse<List<ImageResponseDto>>> getAllImages() {
		List<ImageResponseDto> images = imageService.getAllImages();
		return ResponseEntity.ok(ApiResponse.success("Images retrieved successfully", images));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get image metadata by ID", description = "Retrieve metadata about a specific image")
	public ResponseEntity<ApiResponse<ImageResponseDto>> getImageById(@PathVariable Long id) {
		ImageResponseDto image = imageService.getImageById(id);
		return ResponseEntity.ok(ApiResponse.success("Image retrieved successfully", image));
	}

	@GetMapping("/{id}/content")
	@Operation(summary = "Get image content by ID", description = "Retrieve the actual image file")
	public ResponseEntity<Resource> getImageContent(@PathVariable Long id) {
		Resource resource = imageService.loadImageAsResource(id);

		// Determine content type
		String contentType = "application/octet-stream";
		try {
			contentType = resource.getFile().toPath().getFileName().toString();
			if (contentType.contains(".")) {
				String extension = contentType.substring(contentType.lastIndexOf(".") + 1);
				contentType = switch (extension.toLowerCase()) {
					case "jpg", "jpeg" -> "image/jpeg";
					case "png" -> "image/png";
					case "gif" -> "image/gif";
					default -> "application/octet-stream";
				};
			}
		} catch (Exception e) {
			// Use default content type if determination fails
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@PostMapping
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Upload a new image", description = "Upload a new image file with optional description")
	public ResponseEntity<ApiResponse<ImageResponseDto>> uploadImage(
			@RequestParam("file") MultipartFile file,
			@RequestParam(value = "description", required = false) String description) {

		ImageResponseDto uploadedImage = imageService.storeImage(file, description);
		return new ResponseEntity<>(
				ApiResponse.success("Image uploaded successfully", uploadedImage),
				HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Delete image", description = "Delete an image by its ID")
	public ResponseEntity<ApiResponse<?>> deleteImage(@PathVariable Long id) {
		imageService.deleteImage(id);
		return ResponseEntity.ok(ApiResponse.success("Image deleted successfully"));
	}
}
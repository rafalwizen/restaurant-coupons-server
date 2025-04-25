package com.rafalwizen.restaurantcouponsserver.service;

import com.rafalwizen.restaurantcouponsserver.dto.ImageDto;
import com.rafalwizen.restaurantcouponsserver.dto.ImageResponseDto;
import com.rafalwizen.restaurantcouponsserver.exception.BadRequestException;
import com.rafalwizen.restaurantcouponsserver.exception.ResourceNotFoundException;
import com.rafalwizen.restaurantcouponsserver.model.Image;
import com.rafalwizen.restaurantcouponsserver.repository.ImageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageService {

	private final ImageRepository imageRepository;
	private final Path fileStorageLocation;

	@Autowired
	public ImageService(ImageRepository imageRepository,
						@Value("${file.upload-dir:./uploads/images}") String uploadDir) {
		this.imageRepository = imageRepository;
		this.fileStorageLocation = Paths.get(uploadDir)
				.toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (IOException ex) {
			throw new RuntimeException("Could not create the directory where the uploaded files will be stored", ex);
		}
	}

	public List<ImageResponseDto> getAllImages() {
		return imageRepository.findAll().stream()
				.map(this::convertToResponseDto)
				.collect(Collectors.toList());
	}

	public ImageResponseDto getImageById(Long id) {
		Image image = imageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));
		return convertToResponseDto(image);
	}

	public Resource loadImageAsResource(Long id) {
		try {
			Image image = imageRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));

			Path filePath = this.fileStorageLocation.resolve(image.getFilePath()).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				throw new ResourceNotFoundException("File not found: " + image.getFileName());
			}
		} catch (MalformedURLException ex) {
			throw new ResourceNotFoundException("File not found" + ex.getMessage());
		}
	}

	@Transactional
	public ImageResponseDto storeImage(MultipartFile file, String description) {
		// Normalize file name
		String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

		// Check if filename contains invalid characters
		if (originalFilename.contains("..")) {
			throw new BadRequestException("Filename contains invalid path sequence: " + originalFilename);
		}

		// Generate unique filename to prevent overwriting
		String fileExtension = StringUtils.getFilenameExtension(originalFilename);
		String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;

		try {
			// Copy file to the target location
			Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			// Save image metadata to database
			Image image = new Image();
			image.setFileName(originalFilename);
			image.setFilePath(uniqueFilename);
			image.setFileType(file.getContentType());
			image.setFileSize(file.getSize());
			image.setDescription(description);

			Image savedImage = imageRepository.save(image);
			return convertToResponseDto(savedImage);
		} catch (IOException ex) {
			throw new RuntimeException("Could not store file " + originalFilename + ". Please try again!", ex);
		}
	}

	@Transactional
	public void deleteImage(Long id) {
		Image image = imageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));

		// Delete image file from storage
		try {
			Path filePath = this.fileStorageLocation.resolve(image.getFilePath());
			Files.deleteIfExists(filePath);
		} catch (IOException ex) {
			// Log error but continue with deletion from database
			System.err.println("Could not delete file " + image.getFilePath() + ": " + ex.getMessage());
		}

		// Delete image metadata from database
		imageRepository.deleteById(id);
	}

	private ImageResponseDto convertToResponseDto(Image image) {
		ImageResponseDto dto = new ImageResponseDto();
		BeanUtils.copyProperties(image, dto);

		// Generate URL for the image
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/images/")
				.path(image.getId().toString())
				.path("/content")
				.toUriString();

		dto.setUrl(imageUrl);
		return dto;
	}

	public ImageDto convertToDto(Image image) {
		ImageDto dto = new ImageDto();
		BeanUtils.copyProperties(image, dto);
		return dto;
	}
}
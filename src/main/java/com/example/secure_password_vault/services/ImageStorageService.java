package com.example.secure_password_vault.services;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageStorageService {
	public String saveImage(MultipartFile imageFile) throws java.io.IOException {
		 if (imageFile != null && !imageFile.isEmpty()) {
	            String originalFilename = imageFile.getOriginalFilename();
	            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
	            String filename = UUID.randomUUID().toString() + extension;

	            // Caminho para onde as imagens serão salvas
	            String uploadDir = new File("src/main/resources/static/uploads").getAbsolutePath();
	            File dir = new File(uploadDir);
	            if (!dir.exists()) {
	                dir.mkdirs();
	            }
	            
	            File destination = new File(uploadDir + File.separator + filename);
	            imageFile.transferTo(destination);
	            return "/uploads/" + filename;
		 }
		 return null;
	}
}

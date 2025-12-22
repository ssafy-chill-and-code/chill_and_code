package com.ssafy.chillandcode.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;

@Service
@Profile("local")
public class LocalFileStorageService implements FileStorageService {
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@Value("${file.public-base-url}")
	private String publicBaseUrl;

	private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
	        "image/jpeg",
	        "image/png",
	        "image/webp"
	);
	
	@Override
	public String upload(MultipartFile file) {
		
		//MIME 타입 제한
		String contentType = file.getContentType();

		if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
			throw new ApiException(ErrorCode.INVALID_FILE_TYPE);
		}
		
		
		try {
			//디렉토리 생성
			Path uploadPath = Paths.get(uploadDir);
			if(Files.notExists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			//확장자 추출
			String originalFilename = file.getOriginalFilename();
			String extension = "";
			
			if(originalFilename != null && originalFilename.contains(".")) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}
			
			//저장용 파일명 생성 (UUID + 확장자: 공백 등의 에러 방지를 위해 original name 사용 안 함)
			String storedFilename = UUID.randomUUID().toString() + extension;
			Path target = uploadPath.resolve(storedFilename);
			
			//파일 저장
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
			
			//접근 가능한 URL 반환
			return publicBaseUrl + "/" + storedFilename;
			
		} catch (IOException e) { 
			throw new ApiException(ErrorCode.FILE_UPLOAD_FAILED);
		}
	}

}

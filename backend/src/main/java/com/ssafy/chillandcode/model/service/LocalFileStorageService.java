package com.ssafy.chillandcode.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("local")
public class LocalFileStorageService implements FileStorageService {
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@Override
	public String upload(MultipartFile file) {
		try {
			//디렉토리 생성
			Path uploadPath = Paths.get(uploadDir);
			if(Files.notExists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			//파일명 중복 방지
			String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
			Path target = uploadPath.resolve(filename);
			
			//파일 저장
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
			
			//접근 가능한 URL 반환
			return "http://localhost:8080/uploads/" + filename;
			
		} catch (IOException e) { 
			throw new RuntimeException("로컬 파일 업로드 실패", e);
		}
	}

}

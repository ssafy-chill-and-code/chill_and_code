package com.ssafy.chillandcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.chillandcode.model.dto.file.FileUploadResponse;
import com.ssafy.chillandcode.model.service.FileStorageService;

@RestController
@RequestMapping("/api/files")
public class FileController {
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@PostMapping("/upload")
	public FileUploadResponse upload(@RequestParam("file") MultipartFile file) {
		String url = fileStorageService.upload(file);
		return new FileUploadResponse(url);
	}
}

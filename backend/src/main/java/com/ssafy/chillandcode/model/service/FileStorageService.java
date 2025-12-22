package com.ssafy.chillandcode.model.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 파일을 저장하고, 외부에서 접근 가능한 URL을 반환한다.
 */
public interface FileStorageService {
	String upload(MultipartFile file);
}

package com.ssafy.chillandcode.model.dto.file;

public class FileUploadResponse {
	private String url;

	public FileUploadResponse() {

	}

	public FileUploadResponse(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "FileUploadResponse [url=" + url + "]";
	}

}

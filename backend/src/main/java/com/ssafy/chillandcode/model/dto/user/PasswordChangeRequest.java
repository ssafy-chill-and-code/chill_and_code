package com.ssafy.chillandcode.model.dto.user;

public class PasswordChangeRequest {
	private String currentPassword;
	private String newPassword;

	public PasswordChangeRequest() {

	}

	public PasswordChangeRequest(String currentPassword, String newPassword) {
		super();
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "PasswordUpdateRequest [currentPassword=" + currentPassword + ", newPassword=" + newPassword + "]";
	}

}

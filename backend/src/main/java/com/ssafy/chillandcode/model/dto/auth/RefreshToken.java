package com.ssafy.chillandcode.model.dto.auth;

import java.time.LocalDateTime;

public class RefreshToken {
	private Long id;
	private Long userId;
	private String tokenHash;
	private LocalDateTime expiresAt;
	private boolean revoked;
	private LocalDateTime createdAt;
	private LocalDateTime revokedAt;
	
	public RefreshToken() {

	}

	public RefreshToken(Long userId, String tokenHash, LocalDateTime expiresAt) {
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getRevokedAt() {
		return revokedAt;
	}

	public void setRevokedAt(LocalDateTime revokedAt) {
		this.revokedAt = revokedAt;
	}

	@Override
	public String toString() {
		return "RefreshToken [id=" + id + ", userId=" + userId + ", tokenHash=" + tokenHash + ", expiresAt=" + expiresAt
				+ ", revoked=" + revoked + ", createdAt=" + createdAt + ", revokedAt=" + revokedAt + "]";
	}
	
}

package com.ssafy.chillandcode.recommend.dto;

import com.ssafy.chillandcode.recommend.UserStyle;

public class RecommendPeriodRequest {
	UserStyle style;	//null 가능
	Integer minDays;	//null 가능
	Integer maxDays;	//null 가능
	
	boolean remoteWorkAllowed;
	
	public RecommendPeriodRequest() {

	}

	public RecommendPeriodRequest(UserStyle style, Integer minDays, Integer maxDays, boolean remoteWorkAllowed) {
		super();
		this.style = style;
		this.minDays = minDays;
		this.maxDays = maxDays;
		this.remoteWorkAllowed = remoteWorkAllowed;
	}

	public UserStyle getStyle() {
		return style;
	}

	public void setStyle(UserStyle style) {
		this.style = style;
	}

	public Integer getMinDays() {
		return minDays;
	}

	public void setMinDays(Integer minDays) {
		this.minDays = minDays;
	}

	public Integer getMaxDays() {
		return maxDays;
	}

	public void setMaxDays(Integer maxDays) {
		this.maxDays = maxDays;
	}

	public boolean isRemoteWorkAllowed() {
		return remoteWorkAllowed;
	}

	public void setRemoteWorkAllowed(boolean remoteWorkAllowed) {
		this.remoteWorkAllowed = remoteWorkAllowed;
	}

	@Override
	public String toString() {
		return "RecommendPeriodRequest [style=" + style + ", minDays=" + minDays + ", maxDays=" + maxDays
				+ ", remoteWorkAllowed=" + remoteWorkAllowed + "]";
	}

}

package com.ssafy.chillcode.recommend.period;

import java.time.LocalDate;

public class SearchWindow {
	LocalDate start;
	LocalDate end;

	public SearchWindow() {

	}

	public SearchWindow(LocalDate start, LocalDate end) {
		super();
		this.start = start;
		this.end = end;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "SearchWindow [start=" + start + ", end=" + end + "]";
	}

}

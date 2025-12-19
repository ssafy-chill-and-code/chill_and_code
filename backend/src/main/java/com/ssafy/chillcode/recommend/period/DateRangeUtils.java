package com.ssafy.chillcode.recommend.period;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateRangeUtils {
	public static int countWeekend(LocalDate start, LocalDate end) {
		int count = 0;
		for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
			DayOfWeek day = d.getDayOfWeek();
			if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
				count++;
			}
		}

		return count;
	}
}

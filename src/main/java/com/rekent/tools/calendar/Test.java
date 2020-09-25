package com.rekent.tools.calendar;

import java.util.Calendar;

public class Test {
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		ChinaLunarCalendar lunarCalendar = ChinaLunarCalendar.getInstance(calendar);
		System.out.println(lunarCalendar.getChineseYear() + "-" + lunarCalendar.getChineseMonth() + "-" + lunarCalendar.getChineseDate());
		System.out.println(lunarCalendar.isLeapYear());
		System.out.println(lunarCalendar.getLeapMonth());
	}
}

package com.rekent.tools.calendar;

import java.util.Calendar;

public class ChinaLunarCalendar {
	private static final int[] defaultLunarDatas;
	private static final Calendar defaultMinYear;
	private static final Calendar defaultMaxYear;
	private static final String[] monthsName = new String[] { "正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月", "腊月" };
	private static final String[] heavenlyStemArray = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };
	private static final String[] earthlyBranchArray = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };

	static {
		Calendar minCalendar = Calendar.getInstance();
		minCalendar.setTimeInMillis(0);
		minCalendar.add(Calendar.DATE, 31);
		defaultMinYear = minCalendar;
		Calendar maxCalendar = Calendar.getInstance();
		maxCalendar.set(2100, 11, 31, 23, 59, 59);
		defaultMaxYear = maxCalendar;
		defaultLunarDatas = new int[] { 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6, // 1970-1979
				0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, // 1980-1989
				0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, // 1990-1999
				0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, // 2000-2009
				0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, // 2010-2019
				0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530, // 2020-2029
				0x05aa0, 0x076a3, 0x096d0, 0x04afb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, // 2030-2039
				0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0, // 2040-2049
				/** Add By JJonline@JJonline.Cn **/
				0x14b63, 0x09370, 0x049f8, 0x04970, 0x064b0, 0x168a6, 0x0ea50, 0x06b20, 0x1a6c4, 0x0aae0, // 2050-2059
				0x0a2e0, 0x0d2e3, 0x0c960, 0x0d557, 0x0d4a0, 0x0da50, 0x05d55, 0x056a0, 0x0a6d0, 0x055d4, // 2060-2069
				0x052d0, 0x0a9b8, 0x0a950, 0x0b4a0, 0x0b6a6, 0x0ad50, 0x055a0, 0x0aba4, 0x0a5b0, 0x052b0, // 2070-2079
				0x0b273, 0x06930, 0x07337, 0x06aa0, 0x0ad50, 0x14b55, 0x04b60, 0x0a570, 0x054e4, 0x0d160, // 2080-2089
				0x0e968, 0x0d520, 0x0daa0, 0x16aa6, 0x056d0, 0x04ae0, 0x0a9d4, 0x0a2d0, 0x0d150, 0x0f252, // 2090-2099
				0x0d520 };
	}

	/**
	 * 天干
	 */
	private String heavenlyStems;
	/**
	 * 地支
	 */
	private String earthlyBranches;
	/**
	 * 农历年月日
	 */
	private Integer chineseYear;
	private Integer chineseMonth;
	private Integer chineseDate;
	/**
	 * 节气
	 */
	private String solarTerm;
	/**
	 * 是否为润月年
	 */
	private boolean isLeapYear;
	/**
	 * 润月月份
	 */
	private int leapMonth;

	/**
	 * 是否处于润月月份
	 */
	private boolean atLeapMonth;
	/**
	 * 阴历数据
	 */
	private static int[] lunarDatas = defaultLunarDatas;

	public static ChinaLunarCalendar getInstance(Calendar calendar) {
		return createCalendar(calendar);
	}

	private static ChinaLunarCalendar createCalendar(Calendar calendar) {
		ChinaLunarCalendar lunarCalendar = new ChinaLunarCalendar();
		long nowOffset = (calendar.getTimeInMillis() - defaultMinYear.getTimeInMillis()) / (1000 * 3600 * 24);
		int yearIndex = defaultMinYear.get(Calendar.YEAR);
		for (int i = defaultMinYear.get(Calendar.YEAR); i < defaultMaxYear.get(Calendar.YEAR); i++) {
			int singleDays = getChineseYearDay(i);
			if (nowOffset - singleDays < 1) {
				yearIndex = i;
				break;
			}
			nowOffset = nowOffset - singleDays;
		}
		// 设置当前年
		lunarCalendar.chineseYear = yearIndex;
		// 获取当前日月
		int leapMonth = getChineseLeapMonth(yearIndex);
		lunarCalendar.isLeapYear = leapMonth != 0;
		lunarCalendar.leapMonth = leapMonth;
		boolean isInLeapMonth = false;
		for (int i = 1; i <= 12; i++) {
			int elementDays;
			if (lunarCalendar.isLeapYear && i == leapMonth + 1 && isInLeapMonth == false) {
				isInLeapMonth = true;
				elementDays = getChineseLeapMonthDays(yearIndex);
				i--;
			} else {
				elementDays = getChineseMonthDay(yearIndex, i);
				isInLeapMonth = false;
			}
			nowOffset = nowOffset - elementDays;
			if (nowOffset < 0) {
				lunarCalendar.chineseMonth = i;
				lunarCalendar.chineseDate = (int) (nowOffset + elementDays);
				lunarCalendar.atLeapMonth = isInLeapMonth;
				break;
			}
		}
		return lunarCalendar;
	}

	private static int getChineseYearDay(int year) {
		int sum = 29 * 12; // 默认均采用小月计算，后续获取到大月则+1
		int singleLunarYear = lunarDatas[year - defaultMinYear.get(Calendar.YEAR)];

		int months = singleLunarYear & 0x0FFFF;
		int eachMonth = 0x8000;
		for (int i = 0; i < 12; i++) {
			int isBig = months & eachMonth;
			if (isBig != 0) {
				sum++;
			}
			eachMonth = eachMonth >> 1;
		}
		if (getChineseLeapMonth(year) != 0) {
			sum += getChineseLeapMonthDays(year);
		}
		return sum;
	}

	/**
	 * 获取单独年份单独月份下的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private static int getChineseMonthDay(int year, int month) {
		int singleLunarYear = lunarDatas[year - defaultMinYear.get(Calendar.YEAR)];
		int months = singleLunarYear & 0x0FFFF;
		int eachMonth = 0x8000 >> (month - 1);
//		return (singleLunarYear &(0x10000>>month)) !=0 ? 30:29;
		return (months & eachMonth) != 0 ? 30 : 29;
	}

	/**
	 * 后4位，润月月份，为0则不润
	 * 
	 * @param year
	 * @return
	 */
	private static int getChineseLeapMonth(int year) {
		int singleLunarYear = lunarDatas[year - defaultMinYear.get(Calendar.YEAR)];
		return singleLunarYear & 0xF;
	}

	/**
	 * 前4位，1：润大月（30天）、0：润小月（29天）
	 * 
	 * @param year
	 * @return
	 */
	private static int getChineseLeapMonthDays(int year) {
		int leapMoth = getChineseLeapMonth(year);
		if (leapMoth == 0) {
			return 0;
		}
		int singleLunarYear = lunarDatas[year - defaultMinYear.get(Calendar.YEAR)];
		int isBigLeapMonth = singleLunarYear & 0x10000;
		return isBigLeapMonth != 0 ? 30 : 29;
	}

	public String getHeavenlyStems() {
		int stemsIndex = (this.chineseYear - 3) % 10;
		return heavenlyStemArray[stemsIndex - 1];
	}

	public String getEarthlyBranches() {
		int branchIndex = (this.chineseYear - 3) % 12;
		return earthlyBranchArray[branchIndex - 1];
	}

	public Integer getChineseYear() {
		return chineseYear;
	}

	public Integer getChineseMonth() {
		return chineseMonth;
	}

	public Integer getChineseDate() {
		return chineseDate;
	}

	public String getSolarTerm() {
		return solarTerm;
	}

	public boolean isLeapYear() {
		return isLeapYear;
	}

	public int getLeapMonth() {
		return leapMonth;
	}
}

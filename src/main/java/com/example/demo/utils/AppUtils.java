package com.example.demo.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AppUtils {
	
	public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static final String newPublishFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";




	public String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public String decryptedString(String inputEncryptedString) {

		return inputEncryptedString;
	}

	public boolean isNull(String input) {

		if (Objects.isNull(input) || ObjectUtils.isEmpty(input)) {
			return true;
		} else {
			return false;
		}

	}

	public Date formattedDate(Date date) {
		Date finalDate = null;
		try {
			DateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
			String strDate = simpleDateFormat.format(date);
			finalDate = simpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return finalDate;
	}


	public Date convertStringToDate(String date) {
		Date finalDate = null;
		try {
			
			DateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
			finalDate = simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return finalDate;
	}
	
	public  String convertDateToString(Date date) {
	  DateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
	  String strDate = simpleDateFormat.format(date);
	  return strDate;
	}

	public  String getCurrentDateTime() {
		DateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		String strDate = simpleDateFormat.format(new Date());
		return strDate;
	}

	
	
	public String formatStringDate(String date) throws ParseException {
		Date finalDate = null;
		DateFormat simpleDateFormat = new SimpleDateFormat(newPublishFormat);
		try {
			finalDate = simpleDateFormat.parse(date);
			return convertDateToString(finalDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	

	public int otpIdGen() {
		Random rand = new Random();
		int rand_int1 = rand.nextInt(100000000);
		return rand_int1 + 89;
	}
	
	
	
	  public Date yearsIncreasesFromCurrDate(Date date , int yearsToIncrease){
			DateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
			simpleDateFormat.format(date);
	        // convert date to calendar
	        Calendar c = Calendar.getInstance();
	        c.setTime(date);
	        c.add(Calendar.YEAR, yearsToIncrease);
	        // convert calendar to date
	        Date currentDatePlusOne = c.getTime();
	        return formattedDate(currentDatePlusOne);
	    }
	  
	  
	  public Date daysIncreasesFromCurrDate(Date date , int noOfDays){
			DateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
			simpleDateFormat.format(date);
	        // convert date to calendar
	        Calendar c = Calendar.getInstance();
	        c.setTime(date);
	        c.add(Calendar.DATE, noOfDays);
	        // convert calendar to date
	        Date currentDatePlusOne = c.getTime();
	        Date dates = formattedDate(currentDatePlusOne);
	        System.out.println("dates"+dates);
	        return dates;
	    }

	public  Map<String, Integer> convertToHashMap(ArrayList<Map<String, Integer>> arrayList) {
		Map<String, Integer> resultMap = new HashMap<>();
		for (Map<String, Integer> map : arrayList) {
			resultMap.putAll(map);
		}
		return resultMap;
	}


}

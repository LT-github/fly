package com.lt.fly.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CheckBankCardUtil {

	/**
	 * 校验银行卡卡号
	 * 
	 * @param cardId
	 * @return
	 */
	public static boolean checkBankCard(String cardId) {
		char bit = getBankCardCheckCode(cardId
				.substring(0, cardId.length() - 1));
		if (bit == 'N') {
			return false;
		}
		return cardId.charAt(cardId.length() - 1) == bit;
	}
 
	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param nonCheckCodeCardId
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null
				|| nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			// 如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

	/**
	 * TODO
	 * @param cardNo 银行卡卡号
	 * @return {"bank":"CMB","validated":true,"cardType":"DC","key":"(卡号)","messages":[],"stat":"ok"}
	 * 
	 */
	public static String getCardDetail(String cardNo) {
	    // 创建HttpClient实例
	    String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=";
	    url+=cardNo;
	    url+="&cardBinCheck=true";
	    StringBuilder sb = new StringBuilder();
	try {
	  URL urlObject = new URL(url);
	  URLConnection uc = urlObject.openConnection();
	  BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	  String inputLine = null;
	  while ( (inputLine = in.readLine()) != null) {
	    sb.append(inputLine);
	  }
	  in.close();
	} catch (MalformedURLException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	}
	return sb.toString();
	}
	
}

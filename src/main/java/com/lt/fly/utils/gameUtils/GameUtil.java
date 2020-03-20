package com.lt.fly.utils.gameUtils;

import com.lt.fly.utils.Arith;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameUtil {

	public static final Map<String,String> REGEXMAP = new HashMap<>();
	static {
		REGEXMAP.put("ZongHeDaXiaoTemplate", "^[大|小|单|双|和][1-9]\\d*$");
		REGEXMAP.put("LongHuHeTemplate", "^[龙|虎|合][1-9]\\d*$");
		REGEXMAP.put("LianMaTemplate", "^(\\d{2,6}/[1-9]\\d)|(\\d{1,10}复[二|三|四|五|六][1-9]\\d*)$");
		REGEXMAP.put("DinWeiShuZiTemplate", "^[1-6]+/\\d{1,10}/[1-9]\\d*$");
		REGEXMAP.put("SanQuTemplate", "^([前|后][半]?[豹|对|顺|杂][1-9]\\d*)$");
		REGEXMAP.put("DinWeiDaXiaoTemplate", "^(([1-6]{1}[大|小|单|双][1-9]\\d*,?)+)|(([1-6]{1}[大|小][单|双][1-9]\\d*,?)+)$");
		REGEXMAP.put("DanMaTemplate", "^(\\d{1}/[1-9]\\d*,?)+$");
		REGEXMAP.put("TwoCombined", "^([大|小][单|双|龙|虎])|([单|双][龙|虎])[1-9]\\d*$");
		REGEXMAP.put("ThreeCombined", "[大|小][单|双][龙|虎][1-9]\\d*");
		REGEXMAP.put("NiuNiuTemplate", "^([前|后|单|双][超|翻|平]牛[1-9]\\d*)|([1-6]{3}[超|翻|平]牛[1-9]\\d*)$");
		REGEXMAP.put("SanChongTemplate", "^[散|重][1-9]\\d*$");
	}
	
	public static GameProperty getTotalMoney(String gameContent){
		String[] stArr= gameContent.split(",");
		int count = 1;
		double money = 0.0;
		double single =check(gameContent);
		if(gameContent.contains("超")) {
			if(isStartWithNumber(gameContent)) {
				single = check(gameContent.substring(3));
			}
			money = Arith.mul(single,10);
		}
		else if(gameContent.contains("翻")) {
			if(isStartWithNumber(gameContent)) {
				single = check(gameContent.substring(3));
			}
			money = Arith.mul(single,3);
		}else if(gameContent.contains("平")) {
			if(isStartWithNumber(gameContent)) {
				single = check(gameContent.substring(3));
			}
			money = single;
		}else if(gameContent.contains("复")) {
			//截取复中间的汉字数字
			String s = gameContent.substring(gameContent.indexOf("复")+1, gameContent.indexOf("复")+2);
			//截取复前面的数字字符串
			String number = StringUtils.substringBefore(gameContent, "复");
			//截取复后面的金额
			gameContent = StringUtils.substringAfterLast(gameContent, "复");
			count = CombineAndArrangement.getCount(s, number);
			single =check(gameContent);
			money = single*count;
		}else if(gameContent.contains("/")) {
			count = getCount(stArr[0])*stArr.length;
			single = Double.parseDouble(StringUtils.substringAfterLast(stArr[0], "/"));
			money = single*count;
		}else if(gameContent.contains(",")) {
			count = stArr.length;
			single = check(stArr[0]);
			money = single*count;
		}else
			money = single;
		return new GameProperty(count,money,single);
	}
	
	
	public static void main(String[] args) {
		String str  = null;
		while(str != "exit") {
			//总点测试
			System.out.println("请写出玩法内容");
			Scanner scan = new Scanner(System.in);
			str = scan.next();
			GameProperty cm = getTotalMoney(str);
			System.out.println("该玩法的总点为:"+cm.getMoney()+"元");
			System.out.println("该玩法总共:"+cm.getCount()+"注");
			System.out.println("该玩法的单注金额为:"+cm.getSingle()+"元");
		}
		
	}
	
	/**
	 * 提取字符串中的最后的数字
	 * @param x
	 * @return
	 */
	private static double check(String x){
		if(isStartWithNumber(x)) {
			x = x.substring(1);
		}
		char text[]=x.toCharArray();
		x.trim();
		String number="";
		for(int i=0;i<x.length();i++){
			try{
				number+=Integer.parseInt(""+text[i]);
			}catch (NumberFormatException e){
				if(text[i]=='-'||text[i]=='.')
					number+=text[i];
				continue;
			}
		}
		double result=0;
		try{
			result=Double.parseDouble(""+number);}
		catch (Exception e){
			result=0;
		}
		return  result;
	}
	
	/**
	 * 针对连码和定位球求总点的方法
	 * @param str
	 * @return
	 */
	public static Integer getCount(String str) {
		String[] arr = str.split("/");//按照"/"拆分
		if(arr.length == 3) {
			//定位球
			return arr[0].toCharArray().length*arr[1].toCharArray().length;
		}
		return 1;
	}
	
	//判断字符串是不是以数字开头
	public static boolean isStartWithNumber(String str) {
	   Pattern pattern = Pattern.compile("[0-9]*");
	   Matcher isNum = pattern.matcher(str.charAt(0)+"");
	   if (!isNum.matches()) {
	       return false;
	   }
	   return true;
	}
	
	/**
	 * 正则匹配下注格式
	 * @param gameName
	 * @param betsContent
	 * @return
	 */
	public static boolean isRegex(String gameName,String betsContent) {
		Pattern pattern = Pattern.compile(REGEXMAP.get(gameName));
		Matcher matcher = pattern.matcher(betsContent);
		return matcher.find();
	}
	
}

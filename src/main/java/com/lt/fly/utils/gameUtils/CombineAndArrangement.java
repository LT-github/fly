package com.lt.fly.utils.gameUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdwang on 2017-10-25.
 * 组合、排列、可重复排列
 */
public class CombineAndArrangement {

	public static void main(String[] args) {
//		ArrayList<Integer> tmpArr  = new ArrayList<Integer>();
//		int [] com = {1,2,3,4,5,6};
//		int k = 2;
//		if(k > com.length || com.length <= 0){
//			return ;
//		}
//		System.out.println("组合结果：");
//		List<String> result = new ArrayList<>();
//		for (String i : combine(0,k ,com,tmpArr,result)) {
//			System.out.println(i);
//		}
//		System.out.println("总次数:"+result.size()+"次");
	}

	public static Integer getCount (String s ,String number) {
		int k = ChineseNumToArabicNumUtil.chineseNumToArabicNum(s);
		int[] arr = new int[number.length()];
		for (int i = 0; i < number.length(); i++) {
			arr[i] = Integer.parseInt(number.substring(i, i + 1));//substring是找出包含起始位置，不包含结束位置，到结束位置的前一位的子串
		}
		int index = 0;
		ArrayList<Integer> tmpArr  = new ArrayList<Integer>();
		List<String> result = new ArrayList<String>();
		List<String> list = combine(index,k,arr,tmpArr,result);
		return list.size();
	}
	
	/**
	 * 组合
	 * 按一定的顺序取出元素，就是组合,元素个数[C arr.len 3]
	 * @param index 元素位置
	 * @param k 选取的元素个数
	 * @param arr 数组
	 */
	public static List<String> combine(int index,int k,int []arr,ArrayList<Integer> tmpArr,List<String> result) {
		if(k == 1){
			for (int i = index; i < arr.length; i++) {
				tmpArr.add(arr[i]);
				result.add(tmpArr.toString());
				tmpArr.remove((Object)arr[i]);
			}
		}else if(k > 1){
			for (int i = index; i <= arr.length - k; i++) {
				tmpArr.add(arr[i]); //tmpArr都是临时性存储一下
				combine(i + 1,k - 1, arr,tmpArr,result); //索引右移，内部循环，自然排除已经选择的元素
				tmpArr.remove((Object)arr[i]); //tmpArr因为是临时存储的，上一个组合找出后就该释放空间，存储下一个元素继续拼接组合了
			}
		}else{
			return null;
		}
		return result;
	}
	
}

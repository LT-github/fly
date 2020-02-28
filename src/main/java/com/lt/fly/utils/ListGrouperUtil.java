package com.lt.fly.utils;

import java.util.ArrayList;
import java.util.List;

public class ListGrouperUtil {
    /**
     * 将集合按指定数量分组
     * @param list 数据集合
     * @param quantity 分组数量
     * @return 分组结果
     */
    public static <T> List<List<T>> groupListByQuantity(List<T> list, int quantity) {
        if (list == null || list.size() == 0) {
            return null;
        }

        if (quantity <= 0) {
            new IllegalArgumentException("Wrong quantity.");
        }

        List<List<T>> wrapList = new ArrayList<List<T>>();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(new ArrayList<T>(list.subList(count, (count + quantity) > list.size() ? list.size() : count + quantity)));
            count += quantity;
        }

        return wrapList;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 11; i++) {
            list.add(i);
        }

        List<List<Integer>> resultList = ListGrouperUtil.groupListByQuantity(list, 5);
        for (List<Integer> l : resultList) {
            System.out.println(l);
        }
    }

}

package com.lt.fly;

import com.lt.fly.utils.DateUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class test{
    public static void main(String[] args) {
        List<ObjectDto> list = new ArrayList<ObjectDto>();
        ObjectDto on1 = new ObjectDto();
        on1.setDate("2018-05-17");

        ObjectDto on2 = new ObjectDto();
        on2.setDate("2018-05-16");

        ObjectDto on3 = new ObjectDto();
        on3.setDate("2018-05-18");

        ObjectDto on4 = new ObjectDto();
        on4.setDate("2018-05-15");

        list.add(on1);
        list.add(on2);
        list.add(on3);
        list.add(on4);

        System.out.println("排序前"+list.toString());
//升序
        List<ObjectDto> collect = list.stream().sorted(new Comparator<ObjectDto>() {
            @Override
            public int compare(ObjectDto o1, ObjectDto o2) {
                try {
                    Date d1 = DateUtil.parseDate(o1.getDate(), "yyyy-MM-dd");
                    Date d2 = DateUtil.parseDate(o2.getDate(), "yyyy-MM-dd");

                    return d2.compareTo(d1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }

        }).collect(Collectors.toList());


        System.out.println("排序后"+collect.toString());
    }
}

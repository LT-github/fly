package com.lt.fly.utils;

import java.util.List;

public class ListFenUtils<T> {

	public PagingList fen(PagingList page,List<T> showdata){

    	
    	   
    	    //刚开始的页面为第一页
    	    if (page.getCurrentPage() == null){
    	        page.setCurrentPage(1);
    	    } else {
    	        page.setCurrentPage(page.getCurrentPage());
    	    }
    	    //设置每页数据为十条
    	   
    	    page.setPageSize(10);
    	    //每页的开始数
    	    page.setStar((page.getCurrentPage() - 1) * page.getPageSize());
    	    //list的大小
    	    int count = showdata.size();
    	    //设置总页数
    	    page.setTotalPage(count % 10 == 0 ? count / 10 : count / 10 + 1);
    	    //对list进行截取
    	    page.setDataList(showdata.subList(page.getStar(),count-page.getStar()>page.getPageSize()?page.getStar()+page.getPageSize():count));
    	    //数据总条数
    	    page.setTotalNum(showdata.size());
    	    return page;
    	    
    }	
}

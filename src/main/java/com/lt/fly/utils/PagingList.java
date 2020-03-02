package com.lt.fly.utils;

import java.util.ArrayList;
import java.util.List;



public class PagingList {

	
	private Integer currentPage;//当前页
    private int pageSize;//每页显示记录条数
    private int totalPage;//总页数
    private int star;//开始数据
    private int totalNum;//总共多少个元素
    private List<?> dataList;//每页显示的数据
    private List<?> voTotal;
    

   
	
	
	public List<?> getVoTotal() {
		return voTotal;
	}
	public void setVoTotal(List<?> voTotal) {
		this.voTotal = voTotal;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public List<?> getDataList() {
        return dataList;
    }
    public void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }
    public int getStar() {
        return star;
    }
    public void setStar(int star) {
        this.star = star;
    }

    
}

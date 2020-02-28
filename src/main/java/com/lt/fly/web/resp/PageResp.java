package com.lt.fly.web.resp;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageResp<T, L> {

	private Integer pageNum;
	private Integer pageSize;
	private Integer totalPage;
	private Long eleTotalNum;
	private List<T> data = new ArrayList<>();
	private List<L> data2 = new ArrayList<L>();
	
	public PageResp() {
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Long getEleTotalNum() {
		return eleTotalNum;
	}

	public void setEleTotalNum(Long eleTotalNum) {
		this.eleTotalNum = eleTotalNum;
	}

	

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public PageResp(Integer pageNum, Integer pageSize, Integer totalPage, Long eleTotalNum, List<T> data) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.eleTotalNum = eleTotalNum;
		this.data = data;
	}
	
	
	
	public PageResp (Page<L> page){
		this.setEleTotalNum(page.getTotalElements());
		this.setPageNum(page.getNumber());
		this.setPageSize(page.getSize());
		this.setTotalPage(page.getTotalPages());
		this.data2 = page.getContent();
	}
	
	public PageResp<T, L> getPageVo(PageGenerator<T,L> generator){
		
		List<T> generator2 = generator.generator(this.data2);
		this.setData(generator2);
		this.data2 = null;
		return this;
	}
	public interface PageGenerator<T, L> {
		 List<T> generator(List<L> content);
	}
}

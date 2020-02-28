package com.lt.fly.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public class HttpResult<T> implements Serializable {

    /**
     * 返回状态码
     */
	public int code;
    /**
     * 返回对象
     */
    public T data;
    /**
     * 返回错误信息
     */
    public String message;

    // 构造器开始
    /**
     * 无参构造器
     */
    public HttpResult() {
        this.code = 200;
    }
    /**
     * 返回成功的实体
     * @param obj
     */
    public HttpResult(T obj) {
        this.code = 200;
        this.data = obj;
    }
    public HttpResult(T obj,String msg) {
        this.code = 200;
        this.data = obj;
        this.message = msg;
    }

    /**
     * 返回错误信息
     * @param resultCode
     */
    public HttpResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /**
     * 返回错误信息
     * @param code
     * @param message
     */
    public HttpResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // 构造器结束

    /**
     * 成功直接返回数据和状态
     * @param data
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> success(T data){
        return new HttpResult<T>(data);
    }
    public static<T> HttpResult<T> success(T data,String message){
        return new HttpResult<T>(data,message);
    }
    
    /**
     * 成功直接返回数据和状态
     * @param data
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> success(Page<T> page){
    	
    	if(null != page) {
    		List<T> content = page.getContent();
    		int number = page.getNumber();
    		int size = page.getSize();
    		int totalPages = page.getTotalPages();
    		
    		JSONObject json = new JSONObject();
    		json.put("content", content);
    		json.put("pageNum",number);
    		json.put("pageSize",size);
    		json.put("totalPages",totalPages);
    		
    		return new HttpResult(json);
    	}
    	return new HttpResult<T>();
    }
    
    

    /**
     *
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> success(){
        return new HttpResult();
    }
    /**
     * 失败的时候调用
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> failure(int code, String msg){
        return  new HttpResult<T>(code,msg);
    }

    /**
     * 失败的时候调用
     * @param resultCode
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> failure(ResultCode resultCode){
        return  new HttpResult<T>(resultCode);
    }
}

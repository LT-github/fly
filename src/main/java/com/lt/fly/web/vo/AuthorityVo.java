package com.lt.fly.web.vo;

import com.lt.fly.entity.Authority;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AuthorityVo {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private String identifier;
	
	private Long parentId;
	
	
	public AuthorityVo() {
	}
	public AuthorityVo(Authority obj) {
		this.id = obj.getId();
		this.name = obj.getName();
		this.identifier = obj.getIdentifier();
		this.description = obj.getDescription();
		this.parentId = obj.getParent()!= null?obj.getParent().getId():0;
	}
	
	public static List<AuthorityVo> toVo(List<Authority> list){
		List<AuthorityVo> resp = new ArrayList<AuthorityVo>();
		for(Authority item : list) {
			resp.add(new AuthorityVo(item));
		}
		
		return resp;
	}

}


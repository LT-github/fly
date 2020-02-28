package com.lt.fly.web.vo;
import com.lt.fly.entity.Authority;
import com.lt.fly.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleVo {
	private Long id;
	private String name;
	private String description;
	private List<AuthorityVo> authoritys;
	
	public RoleVo() {}
	
	public RoleVo(Role obj) {
		this.name = obj.getName();
		this.id = obj.getId();
		this.description = obj.getDescription();
		this.authoritys = AuthorityVo.toVo(new ArrayList<Authority>(obj.getAuthoritys()));
	}
	
	public static List<RoleVo> toVo(List<Role> list){
		List<RoleVo> resp = new ArrayList<RoleVo>();
		for(Role item : list) {
			resp.add(new RoleVo(item));
		}
		return resp;
	}
	
}

package com.lt.fly.web.vo;

import com.lt.fly.entity.Admin;
import com.lt.fly.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AdminVo {
	
	private Long id;
	private String username;
	private String nickname;
	private String realname;
	private String mobile;
	private Integer status;
	private Long lastLoginTime;
	private List<RoleVo> roles;
	
	public AdminVo() {}
	
	public AdminVo(Admin admin) {
		this.id = admin.getId();
		this.username = admin.getUsername();
		this.nickname = admin.getNickname();
		this.mobile = admin.getMobile();
		this.status = admin.getStatus();
		this.lastLoginTime = admin.getLastLoginTime();
		this.roles = RoleVo.toVo(new ArrayList<Role>(admin.getRoles()));
	}
	
	public static List<AdminVo> toVo(List<Admin> list){
		List<AdminVo> resp = new ArrayList<AdminVo>();
		for (Admin item : list) {
			resp.add(new AdminVo(item));
		}
		return resp;
	}
	
}

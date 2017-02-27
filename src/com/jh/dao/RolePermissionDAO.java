package com.jh.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionDAO {

	/**
	 * 根据角色名获取角色对应的权限信息
	 * @param roleName
	 * @return
	 */
	public List<String> queryAllPermissionByRoleName(String roleName);
}

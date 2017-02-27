package com.jh.service;

import java.util.Collection;

import org.apache.shiro.authz.Permission;

public interface RolePermissionService {

	/**
	 * 根据角色名获取角色对应的权限信息
	 * @param roleName
	 * @return
	 */
	public Collection<Permission> queryAllPermissionByRoleName(String roleName);
}

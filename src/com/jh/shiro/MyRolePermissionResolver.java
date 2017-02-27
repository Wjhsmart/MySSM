package com.jh.shiro;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.springframework.stereotype.Component;

import com.jh.service.RolePermissionService;

@Component(value="rolePermissionResolver")
public class MyRolePermissionResolver implements RolePermissionResolver {
	
	@Resource
	private RolePermissionService rolePermissionService;

	@Override
	public Collection<Permission> resolvePermissionsInRole(String roleName) {
		System.out.println(roleName);
		return rolePermissionService.queryAllPermissionByRoleName(roleName);
	}

}

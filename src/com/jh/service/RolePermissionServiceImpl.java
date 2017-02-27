package com.jh.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.stereotype.Service;

import com.jh.dao.RolePermissionDAO;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
	
	@Resource
	private RolePermissionDAO rolePermissionDAO;

	@Override
	public Collection<Permission> queryAllPermissionByRoleName(String roleName) {
		List<String> p = rolePermissionDAO.queryAllPermissionByRoleName(roleName);
		Collection<Permission> permissions = new ArrayList<Permission>();
		for (String s : p) {
			Permission permission = new WildcardPermission(s);
			permissions.add(permission);
		}
		return permissions;
	}

}

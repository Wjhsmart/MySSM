package com.jh.dao;

import java.io.Serializable;
import java.util.List;

import com.jh.common.bean.Pager4EasyUI;

public interface BaseDAO<T> {
	
	public List<T> queryAll();
	
	public T queryById(Serializable id);
	
	public void add(T t);
	
	public void update(T t);
	
	public void deleteById(Serializable id);
	
	public List<T> queryByPagerAndCriteria(Pager4EasyUI<T> pager, T t);
	
	public int countByCriteria(T t);

}

package com.jh.common.bean;

import java.util.List;

/**
 * EasyUI的分页组件
 * @author Administrator
 *
 * @param <T>
 */
public class Pager4EasyUI<T> {
    private int total;
    private List<T> rows;

    public Pager4EasyUI() {

    }

    public Pager4EasyUI(int total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}

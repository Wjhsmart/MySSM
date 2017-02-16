package com.jh.common.bean;

/**
 * 下拉框的公共类
 * @author Administrator
 *
 */
public class ComboBox4EasyUI {

	private String id; // id
    private String text; // 文本内容
    private boolean selected; // 是否选中

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

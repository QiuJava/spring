package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.List;

public class ComboTree implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ComboTree(){
		
	}
	private String id;
	private String text;
	private String state;
	private List<ComboTree> children;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<ComboTree> getChildren() {
		return children;
	}
	public void setChildren(List<ComboTree> children) {
		this.children = children;
	}

	

	
	
	
}

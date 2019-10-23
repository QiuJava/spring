package cn.eeepay.framework.model;

public class State {
	private Boolean opened;
	private Boolean selected;
	public Boolean getOpened() {
		return opened;
	}
	public void setOpened(Boolean opened) {
		this.opened = opened;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public State(Boolean opened, Boolean selected) {
		super();
		this.opened = opened;
		this.selected = selected;
	}
	
}

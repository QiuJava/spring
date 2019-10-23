package cn.eeepay.framework.model.bill;

import java.io.Serializable;

public class Node implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	
	public Node(String id, String parent, String text, State state, String rigthCode) {
		super();
		this.id = id;
		this.parent = parent;
		this.text = text;
		this.state = state;
		this.rigthCode = rigthCode;
	}

	public Node(){
		
	}
	private String id;
	private String parent;
	private String text;
	private State state;
 	private String rigthCode;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getRigthCode() {
		return rigthCode;
	}

	public void setRigthCode(String rigthCode) {
		this.rigthCode = rigthCode;
	}
	
	
	
}

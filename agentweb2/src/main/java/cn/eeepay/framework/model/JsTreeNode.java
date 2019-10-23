/**
 * 
 */
package cn.eeepay.framework.model;

/**
 * @author xyf1
 *
 */
public class JsTreeNode {
	private String id;
	private String parent;
	private String text;
	private State state;

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

}

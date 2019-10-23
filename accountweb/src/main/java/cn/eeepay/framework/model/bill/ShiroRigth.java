package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShiroRigth  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String rigthCode;

    private String rigthName;

    private String rigthComment;

    private Integer rigthType;
    
    private List<ShiroRole> roles = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRigthCode() {
        return rigthCode;
    }

    public void setRigthCode(String rigthCode) {
        this.rigthCode = rigthCode == null ? null : rigthCode.trim();
    }

    public String getRigthName() {
        return rigthName;
    }

    public void setRigthName(String rigthName) {
        this.rigthName = rigthName == null ? null : rigthName.trim();
    }

    public String getRigthComment() {
        return rigthComment;
    }

    public void setRigthComment(String rigthComment) {
        this.rigthComment = rigthComment == null ? null : rigthComment.trim();
    }

    public Integer getRigthType() {
        return rigthType;
    }

    public void setRigthType(Integer rigthType) {
        this.rigthType = rigthType;
    }

	public List<ShiroRole> getRoles() {
		return roles;
	}

	public void setRoles(List<ShiroRole> roles) {
		this.roles = roles;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rigthCode == null) ? 0 : rigthCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShiroRigth other = (ShiroRigth) obj;
		if (rigthCode == null) {
			if (other.rigthCode != null)
				return false;
		} else if (!rigthCode.equals(other.rigthCode))
			return false;
		return true;
	}
	
    
}
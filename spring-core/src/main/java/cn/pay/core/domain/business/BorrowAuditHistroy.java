package cn.pay.core.domain.business;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.pay.core.domain.base.BaseAuthDomain;
import cn.pay.core.domain.sys.LoginInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 借款审核历史
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "borrow_audit_histroy")
public class BorrowAuditHistroy extends BaseAuthDomain {
	private static final long serialVersionUID = 1L;
	/** 发标审核 */
	public static final int PUSH_AUDIT = 0;
	/** 满标一审 */
	public static final int FULL_AUDIT1 = 1;
	/** 满标二审 */
	public static final int FULL_AUDIT2 = 2;

	@Column(name = "audit_type")
	private Integer auditType;
	@Column(name = "borrow_id")
	private Long borrowId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Column(name = "state")
	public Integer getState() {
		return state;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "auditor_id")
	public LoginInfo getAuditor() {
		return auditor;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "applier_id")
	public LoginInfo getApplier() {
		return applier;
	}

	@Column(name = "apply_time")
	public Date getApplyTime() {
		return applyTime;
	}

	@Column(name = "audit_time")
	public Date getAuditTime() {
		return auditTime;
	}

	@Transient
	public String getAuditTypeDisplay() {
		switch (auditType) {
		case PUSH_AUDIT:
			return "发标审核";
		case FULL_AUDIT1:
			return "满标一审";
		case FULL_AUDIT2:
			return "满标二审";
		default:
			return "异常状态";
		}
	}
}

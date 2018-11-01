package cn.qj.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 借款审核历史
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Getter
@Setter
@ToString
@Entity
public class BorrowAuditHistroy extends AuthComponent {
	private static final long serialVersionUID = 1L;
	/** 发标审核 */
	public static final int PUSH_AUDIT = 0;
	/** 满标一审 */
	public static final int FULL_AUDIT1 = 1;
	/** 满标二审 */
	public static final int FULL_AUDIT2 = 2;

	private Long id;
	private int auditType;
	private Long borrowId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Override
	public int getState() {
		return state;
	}

	@Override
	public String getRemark() {
		return remark;
	}

	@Override
	@OneToOne
	public LoginInfo getAuditor() {
		return auditor;
	}

	@Override
	@OneToOne
	public LoginInfo getApplier() {
		return applier;
	}

	@Override
	public Date getApplyTime() {
		return applyTime;
	}

	@Override
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

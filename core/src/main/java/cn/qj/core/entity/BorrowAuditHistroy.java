package cn.qj.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import cn.qj.core.consts.StatusConst;
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
		case StatusConst.PUSH_AUDIT:
			return "发标审核";
		case StatusConst.FULL_AUDIT1:
			return "满标一审";
		case StatusConst.FULL_AUDIT2:
			return "满标二审";
		default:
			return "异常状态";
		}
	}
}

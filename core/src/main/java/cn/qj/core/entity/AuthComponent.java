package cn.qj.core.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 审核组件
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Getter
@Setter
public class AuthComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 审核中 */
	public static final int AUTH_NORMAL = 0;
	/** 通过 */
	public static final int AUTH_PASS = 1;
	/** 拒绝 */
	public static final int AUTH_REJECT = 2;

	/** 状态 */
	protected int state;
	/** 备注 */
	protected String remark;
	/** 审核人 */
	protected LoginInfo auditor;
	/** 申请人 */
	protected LoginInfo applier;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected Date auditTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected Date applyTime;

	public String getStateDisplay() {
		switch (state) {
		case AuthComponent.AUTH_NORMAL:
			return "审核中";
		case AuthComponent.AUTH_PASS:
			return "审核通过";
		case AuthComponent.AUTH_REJECT:
			return "审核拒绝";
		default:
			return "异常状态";
		}
	}
}

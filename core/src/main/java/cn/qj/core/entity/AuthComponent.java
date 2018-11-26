package cn.qj.core.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import cn.qj.core.consts.StatusConst;
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
		case StatusConst.AUTH_NORMAL:
			return "审核中";
		case StatusConst.AUTH_PASS:
			return "审核通过";
		case StatusConst.AUTH_REJECT:
			return "审核拒绝";
		default:
			return "异常状态";
		}
	}
}

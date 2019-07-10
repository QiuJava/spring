package cn.loan.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import cn.loan.core.util.UserInfoStatusUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息
 * 
 * @author Qiujian
 * 
 */
@Setter
@Getter
@Entity
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	@Version
	private Integer version;
	private Long statusValue;
	private String realName;
	private String idNumber;
	private String phoneNumber;
	private String email;
	private Integer authScore;
	/** 收入 级别 */
	@OneToOne
	private SystemDictionaryItem incomeGrade;
	/** 学历 */
	@OneToOne
	private SystemDictionaryItem educationBackground;
	private Long realAuthId;

	public void addStatus(Long status) {
		statusValue = UserInfoStatusUtil.addStatus(statusValue, status);
	}

	public void deleteStatus(Long status) {
		statusValue = UserInfoStatusUtil.removeStatus(statusValue, status);
	}

	/**
	 * 判断当前用户是否已经绑定手机
	 */
	public boolean isBindPhone() {
		return UserInfoStatusUtil.hasStatus(statusValue, UserInfoStatusUtil.OP_BIND_PHONE);
	}

	/**
	 * 判断当前用户是否已经绑定邮箱
	 */
	public boolean isBindEmail() {
		return UserInfoStatusUtil.hasStatus(statusValue, UserInfoStatusUtil.OP_BIND_EMAIL);
	}

	/**
	 * 判断当前用户是否填写了基本资料
	 */
	public boolean isBasicInfo() {
		return UserInfoStatusUtil.hasStatus(statusValue, UserInfoStatusUtil.OP_BASIC_INFO);
	}

	/**
	 * 判断当前用户是否通过了实名认证
	 */
	public boolean isRealAuth() {
		return UserInfoStatusUtil.hasStatus(statusValue, UserInfoStatusUtil.OP_REAL_AUTH);
	}

	/**
	 * 判断当前用户是否通过了视瓶认证认证
	 */
	public boolean isVedioAuth() {
		return UserInfoStatusUtil.hasStatus(statusValue, UserInfoStatusUtil.OP_VEDIO_AUTH);
	}

	/**
	 * 判断用户是否有借款在审核流程中
	 */
	public boolean isBorrowProcess() {
		return UserInfoStatusUtil.hasStatus(statusValue, UserInfoStatusUtil.OP_BORROW_PROCESS);
	}

	/**
	 * 判断当前用户是否绑定银行卡
	 */
	public boolean isBankCardBind() {
		return UserInfoStatusUtil.hasStatus(statusValue, UserInfoStatusUtil.OP_BANK_CARD_BIND);
	}

	/**
	 * 判断当前用户是否有提现申请在审核中
	 */
	public boolean isWithdrawProcess() {
		return UserInfoStatusUtil.hasStatus(statusValue, UserInfoStatusUtil.OP_WITHDRAW_PROCESS);
	}
}
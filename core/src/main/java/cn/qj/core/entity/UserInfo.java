package cn.qj.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import cn.qj.core.util.BidStateUtil;
import lombok.Data;

/**
 * 用户信息
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@Entity
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer version;
	private Long bitState = 0L;
	private String realName;
	private String idNumber;
	private String phoneNumber;
	private String email;
	private Integer authScore = 0;
	/** 月收入 */
	private SystemDictionaryItem incomeGrade;
	/** 婚姻情况 */
	private SystemDictionaryItem marriage;
	/** 小孩数量 */
	private SystemDictionaryItem kidCount;
	/** 学历 */
	private SystemDictionaryItem educationBackground;
	/** 住房条件 */
	private SystemDictionaryItem houseCondition;
	private Long realAuthId;

	@Id
	public Long getId() {
		return id;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	@OneToOne
	public SystemDictionaryItem getIncomeGrade() {
		return incomeGrade;
	}

	@OneToOne
	public SystemDictionaryItem getMarriage() {
		return marriage;
	}

	@OneToOne
	public SystemDictionaryItem getKidCount() {
		return kidCount;
	}

	@OneToOne
	public SystemDictionaryItem getEducationBackground() {
		return educationBackground;
	}

	@OneToOne
	public SystemDictionaryItem getHouseCondition() {
		return houseCondition;
	}

	public void addState(Long state) {
		bitState = BidStateUtil.addState(bitState, state);
	}

	public void deleteState(Long state) {
		bitState = BidStateUtil.removeState(bitState, state);
	}

	/**
	 * 判断当前用户是否已经绑定手机
	 */
	@Transient
	public boolean getIsBindPhone() {
		return BidStateUtil.hasState(bitState, BidStateUtil.OP_BIND_PHONE);
	}

	/**
	 * 判断当前用户是否已经绑定邮箱
	 */
	@Transient
	public boolean getIsBindEmail() {
		return BidStateUtil.hasState(bitState, BidStateUtil.OP_BIND_EMAIL);
	}

	/**
	 * 判断当前用户是否填写了基本资料
	 */
	@Transient
	public boolean getIsBasicInfo() {
		return BidStateUtil.hasState(bitState, BidStateUtil.OP_BASIC_INFO);
	}

	/**
	 * 判断当前用户是否通过了实名认证
	 */
	@Transient
	public boolean getIsRealAuth() {
		return BidStateUtil.hasState(bitState, BidStateUtil.OP_REAL_AUTH);
	}

	/**
	 * 判断当前用户是否通过了视瓶认证认证
	 */
	@Transient
	public boolean getIsVedioAuth() {
		return BidStateUtil.hasState(bitState, BidStateUtil.OP_VEDIO_AUTH);
	}

	/**
	 * 判断用户是否有借款在审核流程中
	 */
	@Transient
	public boolean getHasBorrow() {
		return BidStateUtil.hasState(bitState, BidStateUtil.OP_BORROW_PROCESS);
	}

	/**
	 * 判断当前用户是否绑定银行卡
	 */
	@Transient
	public boolean getIsBankBind() {
		return BidStateUtil.hasState(bitState, BidStateUtil.OP_USERBANKINFO_BIND);
	}

	/**
	 * 判断当前用户是否有提现申请在审核中
	 */
	@Transient
	public boolean getIsWithdraw() {
		return BidStateUtil.hasState(bitState, BidStateUtil.OP_WITHDRAW_PROCESS);
	}
}
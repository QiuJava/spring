package cn.pay.core.domain.business;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import cn.pay.core.domain.base.BaseDomain;
import cn.pay.core.domain.sys.SystemDictionaryItem;
import cn.pay.core.util.BidStateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户基本信息
 * 
 * @author Administrator
 *
 */
@Getter
@Setter
@ToString
@Entity
// @Table(name = "user_info")
public class UserInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	// @Version
	private Integer version;
	// @Column(name = "bit_state")
	private Long bitState = 0L;
	// @Column(name = "real_name")
	private String realName;
	// @Column(name = "id_number")
	private String idNumber;
	// @Column(name = "phone_number")
	private String phoneNumber;
	// @Column(name = "email")
	private String email;
	// @Column(name = "auth_score")
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
	// @Column(name = "real_auth_id")
	private Long realAuthId;

	@Id
	// @Column(name = "id")
	public Long getId() {
		return id;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "income_grade_id")
	public SystemDictionaryItem getIncomeGrade() {
		return incomeGrade;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "marriage_id")
	public SystemDictionaryItem getMarriage() {
		return marriage;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "kid_count_id")
	public SystemDictionaryItem getKidCount() {
		return kidCount;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "education_background_id")
	public SystemDictionaryItem getEducationBackground() {
		return educationBackground;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "house_condition_id")
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
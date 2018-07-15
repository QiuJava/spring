package cn.pay.core.domain.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cn.pay.core.domain.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户银行卡信息
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
@Entity
// @Table(name = "user_bank_info")
public class UserBankInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;
	// @Column(name = "bank_fork_name")
	private String bankForkName;
	// @Column(name = "bank_name")
	private String bankName;
	// @Column(name = "account_number")
	private String accountNumber;
	// @Column(name = "login_info_id")
	private Long loginInfoId;
	// @Column(name = "account_name")
	private String accountName;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
}

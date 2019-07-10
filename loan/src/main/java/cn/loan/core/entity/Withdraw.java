package cn.loan.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;

import cn.loan.core.entity.vo.WithdrawVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 提现
 * 
 * @author Qiujian
 * 
 */
@Setter
@Getter
@Entity
public class Withdraw implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String cardNumber;
	private String bankForkName;
	private String bankName;
	private String realName;
	private BigDecimal amount;
	/**
	 * 到账金额
	 */
	private BigDecimal arrivalAmount;

	@OneToOne
	private LoginUser auditor;
	private Integer auditStatus;
	private String remark;
	@OneToOne
	private LoginUser submitter;
	private Date auditTime;
	private Date submissionTime;
	@Transient
	private String auditStatusDisplay;

	@Transient
	public String getJsonString() {
		WithdrawVo vo = new WithdrawVo();
		vo.setId(id);
		vo.setUsername(submitter.getUsername());
		vo.setBankForkName(bankForkName);
		vo.setCardNumber(cardNumber);
		vo.setRealName(realName);
		vo.setAmount(amount);
		vo.setBankName(bankName);
		return JSONObject.toJSONString(vo);
	}
}

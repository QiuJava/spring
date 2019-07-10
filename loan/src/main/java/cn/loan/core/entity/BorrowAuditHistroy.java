package cn.loan.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * 借款审核历史
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@Entity
public class BorrowAuditHistroy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer auditType;
	private Long borrowId;

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
	private String auditTypeDisplay;

}

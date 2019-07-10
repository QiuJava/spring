package cn.loan.core.entity.qo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import cn.loan.core.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础条件
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Getter
@Setter
public class BaseQo implements Serializable {
	private static final long serialVersionUID = 1L;

	@DateTimeFormat(pattern = DateUtil.DATA_PATTERN)
	private Date beginTime;
	@DateTimeFormat(pattern = DateUtil.DATA_PATTERN)
	private Date endTime;

	private Integer currentPage = 1;
	private Integer size = 5;

	public Integer getPage() {
		return currentPage - 1;
	}

	public Date getEndTime() {
		// 加一天减一秒
		return endTime != null ? DateUtil.getEndTime(endTime) : null;
	}
}

package cn.qj.core.pojo.qo;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import lombok.Getter;
import lombok.Setter;

/**
 * 借款查询
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
public class BorrowQo extends PageConditionQo {
	private static final long serialVersionUID = 1L;
	/** 根据借款状态来进行查询 */
	private Integer borrowState = -1;
	/** 根据借款的某些状态查询 */
	private List<Integer> status;
	private String orderBy;
	private Direction orderType = Direction.DESC;

	@Override
	public String toString() {
		return "BorrowQo [borrowState=" + borrowState + ", status=" + status + ", orderBy=" + orderBy + ", orderType="
				+ orderType + ", currentPage=" + currentPage + ", pageSize=" + pageSize + "]";
	}

}

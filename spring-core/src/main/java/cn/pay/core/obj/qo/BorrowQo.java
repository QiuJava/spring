package cn.pay.core.obj.qo;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 借款查询对象
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
public class BorrowQo extends BaseQo {
	private static final long serialVersionUID = 1L;
	/** 根据借款状态来进行查询 */
	private Integer borrowState = -1;
	/** 根据借款的某些状态查询 */
	private List<Integer> status;
	private String orderBy;
	private Direction orderType = Direction.DESC;
}

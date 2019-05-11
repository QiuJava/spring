package cn.qj.entity.qo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础分页查询对象
 * 
 * @author Qiujian
 * @date 2019年5月10日
 *
 */
@Getter
@Setter
public class BasePageQo implements Serializable {
	private static final long serialVersionUID = 1L;

	private int page = 1;
	private int rows = 10;

	public int getPage() {
		return page - 1;
	}
}

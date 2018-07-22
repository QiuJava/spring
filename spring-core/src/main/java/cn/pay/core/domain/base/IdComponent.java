package cn.pay.core.domain.base;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 所有实体的Id组件
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
public class IdComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Long id;
}

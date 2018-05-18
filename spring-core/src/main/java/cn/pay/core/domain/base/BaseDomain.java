package cn.pay.core.domain.base;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础Id对象
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
public class BaseDomain implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Long id;
}

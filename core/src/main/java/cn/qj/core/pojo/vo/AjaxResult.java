package cn.qj.core.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Ajax请求返回
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AjaxResult {

	private boolean success;
	private String msg;
	private Integer errCode;

}

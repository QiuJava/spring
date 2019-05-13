package cn.qj.entity.vo;

import java.util.List;

import cn.qj.entity.Permission;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单列表
 * 
 * @author Qiujian
 * @date 2019年5月10日
 *
 */
@Getter
@Setter
public class MenuListVo {
	private long total;
	private List<Permission> rows;

}

package cn.loan.core.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 实名认证视图
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class RealAuthVo {
	private Long id;
	private String username;
	private String realName;
	private String idNumber;
	private String displayGender;
	private String birthday;
	private String address;
	private String frontImage;
	private String reverseImage;
}

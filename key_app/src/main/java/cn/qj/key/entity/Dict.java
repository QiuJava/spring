package cn.qj.key.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
@Getter
@Setter
@ToString
@Entity
public class Dict {

	public static final Integer VALID = 1;
	public static final Integer INVALID = -1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String dictName;
	private String dictValue;
	private Integer sequence;
	private Long parentId;
	private String intro;
	private Integer state;

}

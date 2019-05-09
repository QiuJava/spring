package cn.qj.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典条目
 * 
 * @author Qiujian
 * @date 2019年5月9日
 *
 */
@Getter
@Setter
@Entity
public class DictItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String value;
	private String description;
	private Date createTime;
	private Date updateTime;
	@ManyToOne
	private Dict dict;

}

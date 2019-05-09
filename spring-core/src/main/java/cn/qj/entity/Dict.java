package cn.qj.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Entity
@Setter
@Getter
public class Dict implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String code;
	private String value;
	private String description;
	private Date createTime;
	private Date updateTime;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dict")
	private List<DictItem> items;
}

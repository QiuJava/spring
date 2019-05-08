package cn.qj.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class DataDict implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String dictName;
	private String dictKey;
	private String dictValue;
	private String description;
}

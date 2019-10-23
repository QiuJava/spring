package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.model.AreaInfo;
import cn.eeepay.framework.model.JoinTable;
import cn.eeepay.framework.service.AreaInfoService;

/**
 * 地区信息action
 * 
 * @author junhu
 *
 */
@Controller
@RequestMapping("areaInfo")
public class AreaInfoAction {
	private static final Logger log = LoggerFactory.getLogger(AreaInfoAction.class);
	@Resource
	private AreaInfoService areaInfoService;

	@RequestMapping(value = "/provinceSelectBox")
	public @ResponseBody List<AreaInfo> provinceSelectBox() {
		return areaInfoService.provinceSelectBox();
	}

	@RequestMapping(value = "/citySelectBox")
	public @ResponseBody List<AreaInfo> citySelectBox(@RequestBody String param) {
		JSONObject jsonObject = JSON.parseObject(param);
		return areaInfoService.citySelectBox(jsonObject.getString("province"));
	}

	@RequestMapping(value = "/getAreaByParentId")
	@ResponseBody
	public List<Map<String,Object>> getAreaByParentId(@RequestParam Integer id) {
		try{
			return areaInfoService.getItemByParentId(id);
		}catch(Exception e){
			log.error("获取城市列表异常",e);
		}
		return new ArrayList<Map<String,Object>>();
	}
	
	@RequestMapping(value = "/getAreaByNames")
	@ResponseBody
	public Map<String,Object> getAreaByNames(@RequestParam String province,@RequestParam String city,
			@RequestParam String area) {
		try{
			return areaInfoService.getAreaByNames(province,city,area);
		}catch(Exception e){
			log.error("获取城市列表异常",e);
		}
		return null;
	}
	
	@RequestMapping(value = "/getAreaByName")
	@ResponseBody
	public List<Map<String,Object>> getAreaByName(@RequestParam("type") String type,@RequestParam("name") String name) {
		try{
			return areaInfoService.getAreaByName(type,name);
		}catch(Exception e){
			log.error("获取城市列表异常",e);
		}
		return new ArrayList<Map<String,Object>>();
	}
}

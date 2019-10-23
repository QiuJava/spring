package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.SysDict;

public interface SysDictService {
	
		void init()  throws Exception;
		
		/**
		 * 获取科目类别文本
		 * @param sysKey 类别Key
		 * @param sysValue 数值
		 * @return
		 * @throws Exception
		 */
		SysDict findSysDictByKeyValue(String sysKey,String sysValue) throws Exception;
		
		SysDict findSysDictByKeyName(String sysKey,String sysName) throws Exception;
		SysDict selectRestPwd();
		
		/**
		 * 获取字典类别集合
		 * @param sysKey 类别Key
		 * @return
		 * @throws Exception
		 */
		List<SysDict> findSysDictGroup(String sysKey) throws Exception;


//		List<SysDict> findSysDictGroupContainAll(String sysKey) throws Exception;
		
//		int insertSysDict(SysDict sysDict) throws Exception;
//		
//		int updateSysDict(SysDict oldSysDict,SysDict newSysDict) throws Exception;
//		
//		int deleteSysDict(Integer id) throws Exception;
		
//		int deleteSysDictByParams(String sysKey,String sysValue) throws Exception;
		
		Map<String, String> selectMapByKey(String string);
		
		/**
		 * 获取到所有的   数据字典  信息（包括id）
		 * @param sysDict
		 * @param sort
		 * @param page
		 * @return
		 * @throws Exception
		 */
		List<SysDict> findSysDicts(SysDict sysDict,Sort sort,Page<SysDict> page) throws Exception;
		
		boolean findSysDictExist(SysDict sysDict) throws Exception ;
		
		/*boolean addSysDict(SysDict sysDict)  throws Exception;*/
		
		SysDict findSysDictById(Integer id) ;
//		public List<SysDict> getAllDictListByRedis() throws Exception;
		
		List<SysDict> selectTranStatusAllDict();
		
		List<SysDict> selectTransTypeAllDict();

		List<SysDict> selectServiceTypeAllDict();
		
		List<SysDict> selectPayMethodTypeAllDict();
		
		List<SysDict> selectCardTypeAllDict();
		
		//获取所有的数据字典，并在controller.js初始化
		JSONObject selectDictAndChildren();

		/**
		 * 数据字典中查询出分润提现服务ID
		 * @param sysKey
		 * @return
		 */
		String SelectServiceId(String sysKey);

	/**
	 * 查询代理商分润日结的总开关
	 * @return
	 */
	SysDict findAgentWebShareSwitch();
	/**
	 * 查询代理商推广的总开关
	 * @return
	 */
	SysDict findAgentWebPromotionSwitch();
	/**
	 * 查询代理商欢乐返返现的总开关
	 * @return
	 */
	SysDict findAgentWebCashBackSwitch();


    List<SysDict> listSysDictGroup(String keyName);


	/**
	 * 获取微创业的业务产品id
	 * @return
	 */
	String getSuperPushBpId();

	/**
	 * 获取机具活动类型对应的中文
	 * @param types
	 * @return
	 */
	List<String> findActivityTypeBy(String[] types);

	List<SysDict> selectByKey(String sysKey);

	SysDict getByKey(String sysKey);
	String getStringValueByKey(String sysKey);

	String getTradeGroupByAgentInfo(String agentType, String agentOem);

}

package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BankAccount;
import cn.eeepay.framework.model.bill.SysDict;

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
		
		List<SysDict> findAllSysDict() throws Exception;
		/**
		 * 获取字典类别集合
		 * @param sysKey 类别Key
		 * @return
		 * @throws Exception
		 */
		List<SysDict> findSysDictGroup(String sysKey) throws Exception;
		
//		List<SysDict> findSysDictGroupContainAll(String sysKey) throws Exception;
		
		int insertSysDict(SysDict sysDict) throws Exception;
		
		int updateSysDict(SysDict oldSysDict,SysDict newSysDict) throws Exception;
		
		int deleteSysDict(Integer id) throws Exception;
		
//		int deleteSysDictByParams(String sysKey,String sysValue) throws Exception;
		
		
		
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

		SysDict findSysDuiAccountGroup(String string);
		
}

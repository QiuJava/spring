package cn.eeepay.framework.service.nposp;

import java.util.List;

import cn.eeepay.framework.model.nposp.SysDict;





public interface SysDictNpospService {
	


		/**
		 * 查询财务进行分润入账开关
		 * @return
		 */
		SysDict findAccountantShareAccounting();


		int updateSysDictShareAccounting(SysDict sysDict);

		/**
		 * 获取字典类别集合
		 * @param sysKey 类别Key
		 * @return
		 * @throws Exception
		 */
		List<SysDict> findSysDictGroup(String sysKey) throws Exception;
}

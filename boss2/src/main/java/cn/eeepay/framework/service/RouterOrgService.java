package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * 集群中收单商户service
 * 
 * @author junhu
 *
 */
public interface RouterOrgService {

	List<Map> listRouterAcqMerchantByCon(Map<String, Object> param, Page<Map> page);

	int deleteRouterAcqMerchantById(Long id);

	int updateAcqMerchantQuota(Map<String, Object> param);

	//集群中收单商户导出  tgh
	List<Map<String,Object>> selecrAllInfoRecordInfo(Map<String, Object> jsonMap);

    Map<String,Object> routerOrgBatchDelete(MultipartFile file) throws Exception;
}

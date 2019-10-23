package cn.eeepay.framework.service.peragent;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.peragent.PaBrand;
import cn.eeepay.framework.model.peragent.PaShareDetail;


public interface PaShareDetailService {

	List<PaShareDetail> findPeragentList(PaShareDetail paShareDetail, Sort sort, Page<PaShareDetail> page);

	List<PaBrand> findPaBrandList();

	Map<String, Object> findPeragentListCollection(PaShareDetail paShareDetail);

	List<PaShareDetail> peragentExport(PaShareDetail paShareDetail);

	PaBrand findByBrandCode(String brandCode);

	PaShareDetail findPaShareDetailById(Integer id);

	Map<String, Object> comfirmBacthAccount(String accountMonth, String username);

}

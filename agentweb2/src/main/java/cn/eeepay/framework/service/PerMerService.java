package cn.eeepay.framework.service;

import cn.eeepay.framework.model.PaMerInfo;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author RPC
 * create 2018/09/11
 */
public interface PerMerService {

	/**
     * 导出
     * @param info
     * @param response
     * @param request
     */
	void exportMerInfo(PaMerInfo info, HttpServletResponse response, HttpServletRequest request)  throws Exception ;

    /**
     * 根据userCode编号查询
     * @param userCode
     * @return
     */
    Map<String, Object> selectRealNameByUserCode(String userCode);



    /**
     * 查询所有下级盟主
     * @param info
     * @param pageNo
     * @param pageSize
     * @return
     */
    Map<String, Object> queryUserByParam(PaMerInfo info, int pageNo, int pageSize);






}

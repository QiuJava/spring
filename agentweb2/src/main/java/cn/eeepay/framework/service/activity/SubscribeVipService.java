package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.model.activity.SubscribeVip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author MXG
 * create 2019/03/25
 */
public interface SubscribeVipService {

    Map<String,Object> selectByParam(SubscribeVip order, int pageNo, int pageSize);

    void export(SubscribeVip order, HttpServletResponse response, HttpServletRequest request) throws Exception;
}

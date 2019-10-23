package cn.eeepay.framework.service.impl.activity;

import cn.eeepay.boss.action.activity.SubscribeVipAction;
import cn.eeepay.framework.dao.activity.SubscribeVipDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.activity.SubscribeVip;
import cn.eeepay.framework.model.surveyOrder.SurveyOrderInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.activity.SubscribeVipService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MXG
 * create 2019/03/25
 */
@Service
public class SubscribeVipServiceImpl implements SubscribeVipService{

    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private SubscribeVipDao subscribeVipDao;

    private static final Logger log = LoggerFactory.getLogger(SubscribeVipServiceImpl.class);

    @Override
    public Map<String, Object> selectByParam(SubscribeVip order, int pageNo, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        order.setCurrentAgentNode(loginAgent.getAgentNode());
        Page<SubscribeVip> page = new Page<>(pageNo, pageSize);
        subscribeVipDao.selectByParamWithPage(order, page);
        if(loginAgent.getAgentLevel() != 1){
            List<SubscribeVip> list = page.getResult();
            for (SubscribeVip subscribeVip : list) {
                subscribeVip.setMobilephone(subscribeVip.getMobilephone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
        }
        result.put("status", true);
        result.put("page", page);

        return result;
    }

    @Override
    public void export(SubscribeVip order, HttpServletResponse response, HttpServletRequest request) throws Exception{

        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        order.setCurrentAgentNode(loginAgent.getAgentNode());
        int level = loginAgent.getAgentLevel();
        List<SubscribeVip> list = subscribeVipDao.selectByParam(order);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "VIP优享订单"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo",null);
            maps.put("merchantName",null);
            maps.put("merchantNo",null);
            maps.put("mobilephone",null);
            maps.put("agentName",null);
            maps.put("name", null);
            maps.put("amount", null);
            maps.put("time",null);
            maps.put("paymentType",null);
            maps.put("paymentOrderNo",null);
            maps.put("createTime",null);
            data.add(maps);
        }else{
            for (SubscribeVip subscribe : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo", subscribe.getOrderNo()==null?"":subscribe.getOrderNo());
                maps.put("merchantName", subscribe.getMerchantName()==null?"":subscribe.getMerchantName());
                maps.put("merchantNo", subscribe.getMerchantNo()==null?"":subscribe.getMerchantNo());
                String mobilephone = subscribe.getMobilephone();
                maps.put("mobilephone", mobilephone==null?"":(level==1?mobilephone:mobilephone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2")));
                maps.put("agentName", subscribe.getAgentName()==null?"":subscribe.getAgentName());
                maps.put("name", subscribe.getName()==null?"":subscribe.getName());
                maps.put("amount", subscribe.getAmount()==null?"":subscribe.getAmount().toString());
                maps.put("time", subscribe.getTime()==null?"":subscribe.getTime().toString());
                String paymentType = subscribe.getPaymentType();
                String paymentTypeValue = "";
                if(paymentType.equalsIgnoreCase("bycard")){
                    paymentTypeValue = "刷卡";
                }else if(paymentType.equalsIgnoreCase("alipay")){
                    paymentTypeValue = "支付宝";
                }else if(paymentType.equalsIgnoreCase("wechat")){
                    paymentTypeValue = "微信";
                }
                maps.put("paymentType", paymentTypeValue);
                maps.put("paymentOrderNo", subscribe.getPaymentOrderNo()==null?"":subscribe.getPaymentOrderNo());
                maps.put("createTime", subscribe.getCreateTime()==null?"":subscribe.getCreateTime());
                data.add(maps);
            }
        }

        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","merchantName","merchantNo","mobilephone","agentName",
                "name","amount","time","paymentType","paymentOrderNo","createTime"};
        String[] colsName = new String[]{"业务订单编号","商户名称","商户编号","商户手机号","所属代理商","服务名称","交易金额","有效期（天）",
                "支付方式","支付订单号","交易时间"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("VIP优享订单导出失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}

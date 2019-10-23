package cn.eeepay.framework.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.daoPerAgent.PaOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.PaOrder;
import cn.eeepay.framework.model.PaUserInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.PaOrderService;
import cn.eeepay.framework.service.PerAgentService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TerminalInfoService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * @author MXG
 * create 2018/07/14
 */
@Service
public class PaOrderServiceImpl implements PaOrderService {

    @Resource
    private PaOrderDao paOrderDao;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private TerminalInfoService terminalInfoService;
    @Resource
    private PerAgentService perAgentService;


    //接口调用成功
    private static final String SUCCESS = "200";
    private static final Logger log = LoggerFactory.getLogger(PaOrderService.class);

    @Override
    public Map<String, Object> selectPaOrderByParam(Page<PaOrder> page, PaOrder orderInfo) {
        Map<String, Object> msg = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (loginAgent != null) {
            orderInfo.setAgentNode(loginAgent.getAgentNode());
        }
        paOrderDao.selectPaOrderByParam(page, orderInfo);
        List<PaOrder> paOrders = page.getResult();
        if (paOrders.size() != 0) {
            //获取图片地址
            for (PaOrder paOrder : paOrders) {
                paOrder.setImg(CommonUtil.getImgUrlAgent(paOrder.getImg()));
            }
        }
        //统计已收货、待发货
        Map<String, Object> receivedTotal = paOrderDao.countReceived(orderInfo);
        Map<String, Object> waitSendTotal = paOrderDao.countWaitSend(orderInfo);
        BigDecimal accountedTotal = paOrderDao.selectGoodsTotal(orderInfo,"1");// 机具款项已入账
        BigDecimal unAccountTotal = paOrderDao.selectGoodsTotal(orderInfo,"0");//机具款项未入账
        BigDecimal shareAccountedTotal = paOrderDao.selectShareAmountTotal(orderInfo,"ENTERACCOUNTED");// 机具分润已入账
        BigDecimal shareUnAccountTotal = paOrderDao.selectShareAmountTotal(orderInfo,"NOENTERACCOUNT");//机具分润未入账
        Integer terminalTotal = paOrderDao.selectTerminalCount(orderInfo);//已发货机具总数
        //当前登录的用户类型（只有机构才能发货）
        PaUserInfo user = perAgentService.selectUserByAgentNo(loginAgent.getAgentNo());
        msg.put("loginUserType", user.getUserType());
        msg.put("status", true);
        msg.put("page", page);
        msg.put("receivedTotal", receivedTotal);
        msg.put("waitSendTotal", waitSendTotal);
        msg.put("accountedTotal", accountedTotal);
        msg.put("unAccountTotal", unAccountTotal);
        msg.put("shareAccountedTotal", shareAccountedTotal);
        msg.put("shareUnAccountTotal", shareUnAccountTotal);
        msg.put("terminalTotal", terminalTotal);
        return msg;
    }

    @Override
    public void exportOrder(PaOrder info, HttpServletResponse response, HttpServletRequest request) throws Exception {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (loginAgent != null) {
            if (info != null) {
                if (info.getAgentNode() == null) {
                    info.setAgentNode(loginAgent.getAgentNode());
                } else {
                }
            } else {
                info.setAgentNode(loginAgent.getAgentNode());
            }
        } else {
        }
        List<PaOrder> list = paOrderDao.selectAllList(info);
        export(list, response, request);
    }

    /**
     * 生成表格
     *
     * @param list
     * @param response
     * @param request
     * @throws Exception
     */
    private void export(List<PaOrder> list, HttpServletResponse response, HttpServletRequest request) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = "机具物料申购订单" + sdf.format(new Date()) + ".xls";
        String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        if (list.size() < 1) {
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo", null);
            maps.put("gName", null);
            maps.put("color", null);
            maps.put("size", null);
            maps.put("price", null);
            maps.put("num", null);
            maps.put("yunfei", null);
            maps.put("totalAmount", null);
            maps.put("goodsTotal", null);
            maps.put("entryStatus", null);
            maps.put("entryTime", null);
            maps.put("shareAmount", null);
            maps.put("accStatus", null);
            maps.put("accTime", null);
            maps.put("userName", null);
            maps.put("userCode", null);
            maps.put("orderStatus", null);
            maps.put("sendType", null);

            maps.put("isPlatform", null);
            maps.put("receiver", null);
            maps.put("receiverMobile", null);
            maps.put("receiverAddress", null);
            maps.put("transChannel", null);
            maps.put("createTime", null);
            maps.put("transTime", null);
            maps.put("sendTime", null);
            maps.put("remark", null);
//            maps.put("receiptDate", null);
            data.add(maps);
        } else {
            for (PaOrder order : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo", order.getOrderNo()== null ? "" : order.getOrderNo());
                maps.put("gName", order.getgName()== null ? "" : order.getgName());
                maps.put("color", order.getColor());
                maps.put("size", order.getSize());
                maps.put("price", order.getPrice() == null ? "" : order.getPrice().toString());
                maps.put("num", order.getNum()== null ? "" : order.getNum()+"");
                maps.put("yunfei", "包邮");
                maps.put("totalAmount", order.getTotalAmount() == null ? "0" : order.getTotalAmount().toString());
                maps.put("goodsTotal", order.getGoodsTotal() == null ? "0" : order.getGoodsTotal().toString());
                maps.put("entryStatus", getSelectStr(order.getEntryStatus(), "entryStatus"));
                maps.put("entryTime", getFormatDate("yyyy-MM-dd HH:mm:ss", order.getEntryTime()));
                maps.put("shareAmount", order.getShareAmount() == null ? "0" : order.getShareAmount().toString());
                maps.put("accStatus", getSelectStr(order.getAccStatus(), "accStatus"));
                maps.put("accTime", getFormatDate("yyyy-MM-dd HH:mm:ss", order.getAccTime()));
                maps.put("userName", order.getUserName()== null ? "" : order.getUserName());
                maps.put("userCode", order.getUserCode()== null ? "" : order.getUserCode());
                maps.put("orderStatus", getSelectStr(order.getOrderStatus(), "orderStatus"));
                maps.put("sendType", getSelectStr(order.getSendType(), "sendType"));

                maps.put("isPlatform", getSelectStr(order.getIsPlatform(), "isPlatform"));
                maps.put("receiver", order.getReceiver()== null ? "" : order.getReceiver());
                maps.put("receiverMobile", order.getReceiverMobile()== null ? "" : order.getReceiverMobile());
                maps.put("receiverAddress", order.getReceiverAddress()== null ? "" : order.getReceiverAddress());
                maps.put("transChannel", getSelectStr(order.getTransChannel(), "transChannel"));
                maps.put("createTime", getFormatDate("yyyy-MM-dd HH:mm:ss", order.getCreateTime()));
                maps.put("transTime", getFormatDate("yyyy-MM-dd HH:mm:ss", order.getTransTime()));
                maps.put("sendTime", getFormatDate("yyyy-MM-dd HH:mm:ss", order.getSendTime()));
                maps.put("remark", order.getRemark());
//                maps.put("receiptDate", getFormatDate("yyyy-MM-dd HH:mm:ss", order.getReceiptDate()));

                data.add(maps);
                /*Map<String, String> maps = new HashMap<String, String>();
                maps.put("userCode", user.getUserCode() == null ? "" : user.getUserCode());
                maps.put("realName", user.getRealName() == null ? "" : user.getRealName());
                maps.put("userType", "2".equals(user.getUserType()) ? "大盟主" : "盟主");
                maps.put("merTotal", user.getMerTotal() == null ? "0" : user.getMerTotal().toString());
                maps.put("allyTotal", user.getAllyTotal() == null ? "0" : user.getAllyTotal().toString());
                maps.put("mobile", user.getMobile() == null ? "" : user.getMobile());
                //盟主称号
                String grade = user.getGrade();

                maps.put("levelShow", user.getLevelShow() == null ? "" : user.getLevelShow());
                maps.put("parentId", user.getAgentNo() == null ? "" : user.getParentId());
                maps.put("parentName", user.getParentName() == null ? "" : user.getParentName());
                //所属上级类型
                String parentType = user.getParentType();
                maps.put("parentType", "1".equals(parentType) ? "机构" : ("2".equals(parentType) ? "大盟主" : "盟主"));
                String isBindCard = user.getIsBindCard();
                maps.put("isBindCard", isBindCard == null ? "" : ("1".equals(isBindCard) ? "已认证" : "未认证"));
                maps.put("createTime", user.getCreateTime() == null ? "" : sdf1.format(user.getCreateTime()));
                data.add(maps);*/
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo", "gName","color","size","price", "num", "yunfei", "totalAmount", "goodsTotal", "entryStatus","entryTime",
        		"shareAmount", "accStatus","accTime", "userName", "userCode", "orderStatus", "sendType", "isPlatform", "receiver", "receiverMobile",
        		"receiverAddress", "transChannel", "createTime", "transTime", "sendTime","remark"
//        		, "receiptDate"
        		};
        String[] colsName = new String[]{"订单编号", "申购物品名称", "颜色","尺码","销售单价(元)","购买数量", "运费", "订单金额", "机具款项金额", "机具款项入账状态","机具款项入账日期",
                "机具分润金额", "机具分润入账状态", "机具分润入账日期","申购盟主姓名", "申购盟主编号", "订单状态", "订单类型", "发货方", "收件人", "联系方式",
                "收货地址", "支付方式", "下单日期", "支付日期", "发货日期","备注"
//                , "确认收货日期"
                };
        OutputStream ouputStream = null;
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("机具申购订单导出失败!", e);
        } finally {
            if (ouputStream != null) {
                ouputStream.close();
            }
        }
    }

    public static String getFormatDate(String format1, Date date) {
        if (date == null) {
            return "";
        }
        String result = null;
        try {
            DateFormat df = new SimpleDateFormat(format1);
            result = df.format(date);
        } catch (Exception e) {
            log.error("异常:", e);
        }
        return result;
    }

    @Override
    public List<PaOrder> selectAllList(PaOrder info) {
        return paOrderDao.selectAllList(info);
    }


    @Override
    public String selectUserCodeByAgentNo(String agentNo) {
        return paOrderDao.selectUserCodeByAgentNo(agentNo);
    }

    @Override
	public Map<String,Object> selectPaOrderByOrderNo(String orderNo) {
		return paOrderDao.selectPaOrderByOrderNo(orderNo);
	}

	public String getSelectStr(String value, String typeName) {
        String selectStr = "";
        if ("status".equals(typeName)) {
            if ("0".equals(value)) {
                selectStr = "未认证";
            } else if ("1".equals(value)) {
                selectStr = "已认证";
            }
        } else if ("sendType".equals(typeName)) {
            if ("1".equals(value)) {
                selectStr = "快递配送";
            } else if ("2".equals(value)) {
                selectStr = "线下自提";
            }
        } else if ("accStatus".equals(typeName)) {
            if ("NOENTERACCOUNT".equals(value)) {
                selectStr = "未入账";
            } else if ("ENTERACCOUNTED".equals(value)) {
                selectStr = "已入账";
            }
        } else if ("entryStatus".equals(typeName)) {
            if ("0".equals(value)) {
                selectStr = "未入账";
            } else if ("1".equals(value)) {
                selectStr = "已入账";
            }
        } else if ("isPlatform".equals(typeName)) {
            if ("0".equals(value)) {
                selectStr = "机构发货";
            } else if ("1".equals(value)) {
                selectStr = "平台发货";
            } else if ("2".equals(value)) {
                selectStr = "大盟主发货";
            } else if ("3".equals(value)) {
                selectStr = "盟主发货";
            }
        } else if ("transChannel".equals(typeName)) {
            if ("wx".equals(value)) {
                selectStr = "微信";
            } else if ("zfb".equals(value)) {
                selectStr = "支付宝";
            } else if ("kj".equals(value)) {
                selectStr = "快捷支付";
            }
        } else if ("orderStatus".equals(typeName)) {
            if ("0".equals(value)) {
                selectStr = "待发货";
            } else if ("1".equals(value)) {
                selectStr = "待付款";
            } else if ("2".equals(value)) {
                selectStr = "已发货";
            }else if ("4".equals(value)) {
                selectStr = "已关闭";
            }
        } else {

        }
        return selectStr;
    }

}

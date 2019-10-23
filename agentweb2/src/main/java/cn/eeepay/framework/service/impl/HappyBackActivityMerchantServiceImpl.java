package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.HappyBackActivityMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.HappyBackActivityMerchant;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.HappyBackActivityMerchantService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author tgh
 * @description 欢乐返活跃商户活动查询
 * @date 2019/8/20
 */
@Service
public class HappyBackActivityMerchantServiceImpl implements HappyBackActivityMerchantService {

    @Resource
    private HappyBackActivityMerchantDao happyBackActivityMerchantDao;

    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public void selectHappyBackActivityMerchant(Page<HappyBackActivityMerchant> page,
                                                HappyBackActivityMerchant happyBackActivityMerchant) {
        setAgentNode(happyBackActivityMerchant);
        happyBackActivityMerchantDao.selectHappyBackActivityMerchant(page,happyBackActivityMerchant);
        List<HappyBackActivityMerchant> list = page.getResult();
        for (HappyBackActivityMerchant item : list) {
            setAgentInfomation(item);
        }
    }

    @Override
    public Map<String, Object> countMoney(HappyBackActivityMerchant happyBackActivityMerchant) {
        Map<String, Object> map = new HashMap<>();
        setAgentNode(happyBackActivityMerchant);
        map.put("rewardAmountTotal",happyBackActivityMerchantDao.countRewardAmount(happyBackActivityMerchant,"0"));//0:未入账
        map.put("rewardAmountTotalEd",happyBackActivityMerchantDao.countRewardAmount(happyBackActivityMerchant,"1"));//1:已入账
        map.put("deductAmountTotalEd",happyBackActivityMerchantDao.countDeductAmount(happyBackActivityMerchant,"1"));//1:已扣款
        map.put("deductAmountTotalAdvance",happyBackActivityMerchantDao.countDeductAmount(happyBackActivityMerchant,"2"));//2:已发起预调账
        map.put("deductAmountTotal",happyBackActivityMerchantDao.countDeductAmount(happyBackActivityMerchant,"3"));//3:待扣款
        return map;
    }

    @Override
    public void exportExcel(HappyBackActivityMerchant happyBackActivityMerchant,HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        setAgentNode(happyBackActivityMerchant);
        List<HappyBackActivityMerchant> list = happyBackActivityMerchantDao.exportExcel(happyBackActivityMerchant);
        String fileName = "欢乐返活跃商户活动" + sdf.format(new Date()) + ".xls" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
        List<Map<String, String>> data = new ArrayList<>() ;
        for(HappyBackActivityMerchant item: list){
            setAgentInfomation(item);
            Map<String, String> map = new HashMap<>();
            map.put("activeOrder", item.getActiveOrder());
            map.put("activeTime", item.getActiveTime() == null ? "" : sdfTime.format(item.getActiveTime()));
            String targetStatus = item.getTargetStatus();
            switch(targetStatus){
                case "0":
                    targetStatus = "未达标";
                    break;
                case "1":
                    targetStatus = "已达标";
                    break;
                default:
                    targetStatus = "";
                    break;
            }
            map.put("targetStatus", targetStatus);
            map.put("rewardAmount", item.getRewardAmount() == null ? "" : item.getRewardAmount().toString());
            String rewardAccountStatus = item.getRewardAccountStatus();
            if (StringUtils.isNotBlank(rewardAccountStatus)){
                switch(rewardAccountStatus){
                    case "0":
                        rewardAccountStatus = "未入账";
                        break;
                    case "1":
                        rewardAccountStatus = "已入账";
                        break;
                    default:
                        break;
                }
            }
            map.put("rewardAccountStatus", rewardAccountStatus);
            map.put("targetTime", item.getTargetTime() == null ? "" : sdfTime.format(item.getTargetTime()));
            map.put("rewardAccountTime", item.getRewardAccountTime() == null ? "" : sdfTime.format(item.getRewardAccountTime()));
            map.put("deductAmount", item.getDeductAmount() == null ? "" : item.getDeductAmount().toString());
            String deductStatus = item.getDeductStatus();
            switch(deductStatus){
                case "0":
                    deductStatus = "未扣款";
                    break;
                case "1":
                    deductStatus = "已扣款";
                    break;
                case "2":
                    deductStatus = "已发起预调账";
                    break;
                case "3":
                    deductStatus = "待扣款";
                    break;
                default:
                    deductStatus = "";
                    break;
            }
            map.put("deductStatus", deductStatus);
            map.put("deductTime", item.getDeductTime() == null ? "" : sdfTime.format(item.getDeductTime()));
            map.put("merchantNo", item.getMerchantNo());
            map.put("agentName", item.getAgentName());
            map.put("agentNo", item.getAgentNo());
            data.add(map);
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"activeOrder","activeTime","targetStatus","rewardAmount","rewardAccountStatus"
                ,"targetTime","rewardAccountTime","deductAmount","deductStatus","deductTime","merchantNo",
                "agentName","agentNo"};
        String[] colsName = new String[]{"激活订单号","激活日期","奖励达标状态","奖励金额(元)","奖励入账状态","奖励达标日期","奖励入账日期",
                "扣款金额(元)","扣款状态","扣款/调账日期","所属商户编号","所属代理商名称","所属代理商编号"};
        OutputStream ouputStream = response.getOutputStream();
        export.export(cols, colsName, data, ouputStream);
        ouputStream.close();
    }

    /**
     * 设置所属代理商和一级代理商编号/名称
     * @param item
     */
    private void setAgentInfomation(HappyBackActivityMerchant item) {
        String agentNode = item.getAgentNode();//所属代理商节点
        AgentInfo info = agentInfoDao.selectByAgentNode(agentNode);
        String agentNo = info.getAgentNo();
        String agentName = info.getAgentName();
        item.setAgentNo(agentNo);
        item.setAgentName(agentName);
    }

    /**
     * 设置当前登录代理商节点为作为查询条件
     * @param happyBackActivityMerchant
     */
    private void setAgentNode(HappyBackActivityMerchant happyBackActivityMerchant) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AgentInfo loginAgent = agentInfoDao.selectByAgentNo(principal.getUserEntityInfo().getEntityId());
        happyBackActivityMerchant.setCurrentAgentNode(loginAgent.getAgentNode());
    }
}

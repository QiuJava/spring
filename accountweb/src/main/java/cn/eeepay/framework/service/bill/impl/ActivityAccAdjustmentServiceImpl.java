package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.BeforeAdjustApplyMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentPreAdjust;
import cn.eeepay.framework.model.bill.AgentPreFreeze;
import cn.eeepay.framework.model.bill.BeforeAdjustApply;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.service.bill.ActivityAccAdjustmentService;
import cn.eeepay.framework.service.bill.AgentPreFreezeService;
import cn.eeepay.framework.util.HttpConnectUtil;
import cn.eeepay.framework.util.StringUtil;
import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/5/17
 * Time: 9:30
 * Description: 类注释
 */
@Transactional
@Service("activityAccAdjustmentService")
public class ActivityAccAdjustmentServiceImpl implements ActivityAccAdjustmentService {

    private static final Logger log = LoggerFactory.getLogger(ActivityAccAdjustmentServiceImpl.class);

    @Value("${accountApi.http.secret}")
    private String accountApiHttpSecret;
    @Value("${accountApi.http.url}")
    private String accountApiHttpUrl;
    @Resource
    private AgentPreFreezeService agentPreFreezeService;
    @Resource
    private BeforeAdjustApplyMapper beforeAdjustApplyMapper;

    @Override
    public void insertActivityAdjustment(Map<String, Object> msg,AgentPreAdjust agentPreAdjust,AgentInfo agentInfo) throws IOException {
        final String secret = accountApiHttpSecret;
        // expires in 60 seconds
        final JWTSigner signer = new JWTSigner(secret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("agentNo", agentPreAdjust.getAgentNo());
        claims.put("agentName", agentPreAdjust.getAgentName());
        claims.put("amount", agentPreAdjust.getAdjustAmount().toString());
        claims.put("agentLevel", String.valueOf(agentInfo.getAgentLevel()));

        final String token = signer.sign(claims);
        String url = accountApiHttpUrl + "/agentAccountController/agentActPreAdjustment.do";
        log.info("预调账 url：" + url);
        String response = HttpConnectUtil.postHttp(url, "token", token);
        log.info("预调账返回结果：" + response);

        ObjectMapper om = new ObjectMapper();
        if (response == null || "".equals(response)) {
            log.error("预调账请求账户API接口失败....");
            String errorMsg = "预调账请求接口失败";
            msg.put("status", false);
            msg.put("msg", errorMsg);
            return ;
        }

        Map<String, Object> resp = om.readValue(response, Map.class);
        if (!(Boolean) resp.get("status")) {
            log.error("预调账请求账户API接口返回处理失败....");
            String errorMsg = "预调账请求接口返回处理失败";
            msg.put("status", false);
            msg.put("msg", errorMsg);
            return ;
        }

        String resultDate = (String) resp.get("data");
        if (StringUtil.isBlank(resultDate)) {
            log.error("预调账请求账户API接口成功，金额数据为空，处理失败....");
            String errorMsg = "预调账请求账户API接口成功，金额数据为空，处理失败";
            msg.put("status", false);
            msg.put("msg", errorMsg);
            return ;
        }
        Map<String, Object> respDate = om.readValue(resultDate, Map.class);
        Boolean respStatus = (Boolean) respDate.get("status");
        if (!respStatus) {
            log.error("预调账请求账户API接口成功，但是status为失败，处理失败....");
            String errorMsg = "预调账请求账户API接口成功，但是status为失败，处理失败";
            msg.put("status", false);
            msg.put("msg", errorMsg);
            return ;
        }
        String preFreezeMark = (String) respDate.get("preFreezeMark");      //0 不需要新增预冻结记录  ，1 新增预冻结记录
        BigDecimal dongJieJiLuAmount = new BigDecimal((String) respDate.get("dongJieJiLuAmount"));      //新增冻结记录的金额

        BigDecimal actDongJieAmount = new BigDecimal((String) respDate.get("actDongJieAmount"));    //活动补贴冻结金额调账金额
        BigDecimal actYuAmount = new BigDecimal((String) respDate.get("actYuAmount"));  //活动补贴预调账金额
        //BigDecimal avaliBalance = new BigDecimal((String) respDate.get("avaliBalance"));
        BigDecimal actkeYongAmount = new BigDecimal((String) respDate.get("actkeYongAmount"));  //活动补贴可用余额调账金额

        //添加明细
        //添加预调账申请记录
        BeforeAdjustApply beforeAdjustApply = new BeforeAdjustApply();
        try {
            beforeAdjustApply.setAgentNo(agentPreAdjust.getAgentNo());
            beforeAdjustApply.setAgentName(agentPreAdjust.getAgentName());
            beforeAdjustApply.setFreezeAmount(agentPreAdjust.getAdjustAmount());
            beforeAdjustApply.setActivityAvailableAmount(actkeYongAmount);
            beforeAdjustApply.setActivityFreezeAmount(actDongJieAmount);
            beforeAdjustApply.setGenerateAmount(actYuAmount);
            beforeAdjustApply.setApplyDate(new Date());
            beforeAdjustApply.setApplicant(agentPreAdjust.getApplicant());
            beforeAdjustApply.setRemark(agentPreAdjust.getRemark());
            beforeAdjustApply.setIsApply(1);
            beforeAdjustApplyMapper.insertBeforeAdjustApply(beforeAdjustApply);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增预调账申请记录失败。。。");
            msg.put("status", false);
            msg.put("msg", "预调账成功，但是新增预调账申请记录失败");
        }


        if (preFreezeMark.equals("1")) {
            log.info("发起账户预冻结，冻结金额："+dongJieJiLuAmount.toString()+"   代理商编号："+agentPreAdjust.getAgentNo());
            //申请预冻结
            AgentPreFreeze agentPreFreeze = new AgentPreFreeze();
            agentPreFreeze.setAgentNo(agentPreAdjust.getAgentNo());
            agentPreFreeze.setAgentName(agentPreAdjust.getAgentName());
            agentPreFreeze.setOperater(agentPreAdjust.getApplicant());
            agentPreFreeze.setRemark(agentPreAdjust.getRemark());
            agentPreFreeze.setFreezeReason("other");
            agentPreFreeze.setTerminalFreezeAmount(BigDecimal.ZERO);
            agentPreFreeze.setOtherFreezeAmount(dongJieJiLuAmount);
            agentPreFreeze.setFreezeTime(new Date());
            try {
                Map<String, Object> result = agentPreFreezeService.saveAgentsProfitPreFreeze(agentPreFreeze,agentPreFreeze.getFreezeReason());
                Boolean resultStatus = (Boolean) result.get("status");
                String resultMsg = (String) result.get("msg");
                if (!resultStatus) {
                    msg.put("status", false);
                    msg.put("msg", "发起预冻结失败，预冻金额："+dongJieJiLuAmount.toString()+" "+resultMsg);
                    log.error("发起预冻结失败，预冻金额："+dongJieJiLuAmount.toString()+" "+resultMsg);
                }
            } catch (Exception e) {
                msg.put("status", false);
                msg.put("msg", "发起预冻结失败，预冻金额："+dongJieJiLuAmount.toString()+e.getMessage());
                log.error("保存失败", e);
                log.error("发起预冻结失败，预冻金额："+dongJieJiLuAmount.toString());
            }
        }

    }

    @Override
    public void findBeforeAdjustApplyList(BeforeAdjustApply beforeAdjustApply, Map<String, String> params, Sort sort, Page<BeforeAdjustApply> page) {
        if(StringUtils.isNotBlank(beforeAdjustApply.getDate1())){
            String date1 = beforeAdjustApply.getDate1()+" 00:00:00";
            beforeAdjustApply.setDate1(date1);
        }
        if(StringUtils.isNotBlank(beforeAdjustApply.getDate2())){
            String date2 = beforeAdjustApply.getDate2()+" 23:59:59";
            beforeAdjustApply.setDate2(date2);
        }
        beforeAdjustApplyMapper.findBeforeAdjustApplyList(beforeAdjustApply,params,sort,page);
    }

    @Override
    public List<BeforeAdjustApply> exportBeforeAdjustApplyList(BeforeAdjustApply beforeAdjustApply) {
        if(StringUtils.isNotBlank(beforeAdjustApply.getDate1())){
            String date1 = beforeAdjustApply.getDate1()+" 00:00:00";
            beforeAdjustApply.setDate1(date1);
        }
        if(StringUtils.isNotBlank(beforeAdjustApply.getDate2())){
            String date2 = beforeAdjustApply.getDate2()+" 23:59:59";
            beforeAdjustApply.setDate2(date2);
        }
        return beforeAdjustApplyMapper.exportBeforeAdjustApplyList(beforeAdjustApply);
    }

    @Override
    public Map<String, BigDecimal> findBeforeAdjustApplyListCollection(BeforeAdjustApply beforeAdjustApply) {
        if(StringUtils.isNotBlank(beforeAdjustApply.getDate1())){
            String date1 = beforeAdjustApply.getDate1()+" 00:00:00";
            beforeAdjustApply.setDate1(date1);
        }
        if(StringUtils.isNotBlank(beforeAdjustApply.getDate2())){
            String date2 = beforeAdjustApply.getDate2()+" 23:59:59";
            beforeAdjustApply.setDate2(date2);
        }
        return beforeAdjustApplyMapper.findBeforeAdjustApplyListCollection(beforeAdjustApply);
    }

    @Override
    public Map<String, Object> resolvebatchPreAdjustFile(File temp, String uname) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        List<AgentPreAdjust> agentPreAdjusts = new ArrayList<AgentPreAdjust>();
        Workbook workbook = null;
        workbook = WorkbookFactory.create(temp);
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum() + 1;
        log.info("导入账单的行数是：" + rowCount);
        //从第2行开始逐条
        for(int i = 1; i < rowCount; i++){
            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            Cell cell0 = row.getCell(0); // 代理商编号
            Cell cell1 = row.getCell(1); // 代理商名称
            Cell cell2 = row.getCell(2); // 申请调账金额
            Cell cell3 = row.getCell(3); // 备注
            AgentPreAdjust agentPreAdjust = new AgentPreAdjust();
            if(!StringUtils.isEmpty(getStringCell(cell0))){
                agentPreAdjust.setAgentNo(getStringCell(cell0).trim());
                agentPreAdjust.setAgentName(getStringCell(cell1));
                agentPreAdjust.setAdjustAmount(StringUtils.isBlank(getStringCell(cell2)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell2)));
                agentPreAdjust.setRemark(getStringCell(cell3));
            }
            agentPreAdjusts.add(agentPreAdjust);
        }
        map.put("agentPreAdjusts", agentPreAdjusts);
        map.put("status", true);
        return map;
    }

    @Override
    public Map<String, Object> leadingInBatchPreAdjustDetails(Map<String, Object> map) throws Exception {
        Map<String,Object> result = new HashMap<String, Object>();
        Map<String,Object> resultMapData = new HashMap<String, Object>();
        List<AgentPreAdjust> agentPreAdjustsExcel = new ArrayList<AgentPreAdjust>();
        agentPreAdjustsExcel = (List<AgentPreAdjust>) map.get("agentPreAdjusts");
//		for (AgentPreAdjust agentPreAdjust : agentPreAdjustsExcel) {
//			//查询代理商编号是否存在
//			AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentPreAdjust.getAgentNo());
//			if(agentInfo == null ){
//				//resultMapData.put( agentPreAdjust.getOneAgentNo(), "该代理商不存在！");
//			}else{
//				agentPreAdjust.setAgentName(agentInfo.getAgentName());
//				agentPreAdjustService.insertAgentPreAdjustAndUpdateAccount(agentPreAdjust);
//				resultMapData.put(agentPreAdjust.getAgentNo(), "导入成功！");
//			}
//		}

//        resultMapData = agentPreAdjustService.insertAgentPreAdjustAndUpdateAccountExcel(agentPreAdjustsExcel);
//        result.put("resultMapData",resultMapData);
        return result;
    }

    private static String getStringCell(Cell cell) {
        if (cell != null)
            cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell != null ? cell.getStringCellValue() : null;
    }

}

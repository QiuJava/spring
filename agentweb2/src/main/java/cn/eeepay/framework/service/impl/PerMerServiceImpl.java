package cn.eeepay.framework.service.impl;


import java.io.OutputStream;
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

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.daoPerAgent.PerAgentDao;
import cn.eeepay.framework.daoPerAgent.PerMerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.PaMerInfo;
import cn.eeepay.framework.model.PaUserInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.PerMerService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * @author MXG
 * create 2018/07/11
 */
@Service
public class PerMerServiceImpl implements PerMerService {

    @Resource
    private PerAgentDao perAgentDao;
    @Resource
    private PerMerDao perMerDao;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private MerchantInfoService merchantInfoService;

    private Logger log = LoggerFactory.getLogger(PerMerServiceImpl.class);
    //接口调用成功
    private static final String SUCCESS = "200";



    @Override
    public Map<String, Object> selectRealNameByUserCode(String userCode) {
        return perMerDao.selectRealNameByUserCode(userCode);
    }



    @Override
    public Map<String, Object> queryUserByParam(PaMerInfo info, int pageNo, int pageSize) {
        Map<String, Object> msg = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent != null) {
            if(info != null ){
                if(info.getAgentNode() == null){
                    info.setAgentNode(loginAgent.getAgentNode());
                }
            }else {
                info = new PaMerInfo();
                info.setAgentNode(loginAgent.getAgentNode());
            }
        }else {
            msg.put("status", false);
            msg.put("msg", "请先登录");
            return msg;
        }
        //查询登录代理商对应的人人代理用户信息
        PaUserInfo paUserInfo = perAgentDao.selectUserByAgentNo(loginAgent.getAgentNo());
        info.setLoginUserCode(paUserInfo.getUserCode());
        info.setLoginUserNode(paUserInfo.getUserNode());
        info.setLoginUserType(paUserInfo.getUserType());
        Page<PaMerInfo> page = new Page<>(pageNo, pageSize);
        perMerDao.selectPaMerByParam(page, info);

        if(page.getResult().size() == 0){
            msg.put("status", true);
            msg.put("page", page);
            return msg;
        }
        if(page.getResult().size() > 0){
            List<PaMerInfo> list = page.getResult();
            for (PaMerInfo merInfo : list) {
                MerchantInfo mer=merchantInfoService.selectMn(merInfo.getMerchantNo());
                if(mer!=null){
                    merInfo.setMerchantName(mer.getMerchantName());
                }
                //非机构登录而且非直属,隐藏手机号
                if (loginAgent.getAgentLevel() != 1 && !(paUserInfo.getUserCode()).equals(merInfo.getUserCode())) {
//                	merInfo.setMobile(merInfo.getMobile().replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));//商户手机号
                	merInfo.setMobilePhone(StringUtils.isBlank(merInfo.getMobilePhone()) ? "" : merInfo.getMobilePhone().replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));//所属盟主手机号
				}
                showMerType(merInfo);
            }
        }
        /*if(page.getResult().size() == 0){
            msg.put("status", true);
            msg.put("page", page);
            return msg;
        }
        if(page.getResult().size() > 0){
            List<PaMerInfo> list = page.getResult();
            for (PaMerInfo merInfo : list) {
                //隐藏手机号
               // userInfo.setMobile(userInfo.getMobile().replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
            }
        }
        //统计大盟主、盟主数量
        msg.put("userTotal", perMerDao.countUser(info));//盟主总数
        msg.put("bigUserTotal", perMerDao.countBigUser(info));//大盟主总数
        //统计由商家成为盟友的数量
        long merUserTotal = countUserFromMer(info);*/

        msg.put("merTotal", perMerDao.countMer(info));//大盟主总数
        msg.put("status", true);
        msg.put("page", page);
       // msg.put("merUserTotal", merUserTotal);
      //  msg.put("maxAgentLevel", loginAgent.getAgentShareLevel());//登录代理商的分润最高可调级别
        return msg;
    }
    /**
     * 转换商户类型显示
     * @param merInfo
     * 
     * {text:"已注册未认证商户",value:'0'},{text:"已认证未绑定机具商户",value:'1'},{text:"已绑机具未激活商户",value:'2'},
		          {text:"已激活商户",value:'3'},{text:"由商户成为盟主的商户",value:'4'}
     */
	private void showMerType(PaMerInfo merInfo) {
		/**
		 * 1.每个筛选项的筛选结果对应正确（已注册未认证商户、已认证未绑定机具商户、已绑机具未激活商户、已激活商户、由商户成为盟主的商户）
			2.有重叠的时候优先展示为由商户成为盟主的商户
		 */
		if ("0".equals(merInfo.getStatus())) {
			merInfo.setMerType("已注册未认证商户");
		}else if ("1".equals(merInfo.getStatus()) && "0".equals(merInfo.getBindTer())) {
			merInfo.setMerType("已认证未绑定机具商户");
		}else if ("1".equals(merInfo.getBindTer()) && "0".equals(merInfo.getIsAct())) {
			merInfo.setMerType("已绑机具未激活商户");
		}else if ("1".equals(merInfo.getIsAct())) {
			merInfo.setMerType("已激活商户");
		}
		if ("1".equals(merInfo.getIsMerUser())) {
			merInfo.setMerType("由商户成为盟主的商户");
		}
	}

	@Override
	public void exportMerInfo(PaMerInfo info, HttpServletResponse response, HttpServletRequest request) throws Exception {
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		if (loginAgent == null) {
			throw new AgentWebException("请登录后再操作");
		}
		 //查询登录代理商对应的人人代理用户信息
        PaUserInfo paUserInfo = perAgentDao.selectUserByAgentNo(loginAgent.getAgentNo());
        info.setLoginUserCode(paUserInfo.getUserCode());
        info.setLoginUserNode(paUserInfo.getUserNode());
        info.setLoginUserType(paUserInfo.getUserType());
        List<PaMerInfo> list =  perMerDao.exportMerInfo(info);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "盟主商户记录"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size() < 1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("merchantNo",null);
            maps.put("merchantName",null);
            maps.put("merType",null);
            maps.put("mobilePhone",null);
            maps.put("userCode", null);
            maps.put("realName", null);
            maps.put("nickName",null);
            maps.put("mobile",null);
            maps.put("createTime",null);
            maps.put("actTime",null);
            data.add(maps);
        }else{
            for (PaMerInfo paMerInfo : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("merchantNo",paMerInfo.getMerchantNo());
                MerchantInfo mer=merchantInfoService.selectMn(paMerInfo.getMerchantNo());
                if(mer!=null){
                	paMerInfo.setMerchantName(mer.getMerchantName());
                }
                maps.put("merchantName",paMerInfo.getMerchantName());
                showMerType(paMerInfo);
                maps.put("merType",paMerInfo.getMerType());
                if (loginAgent.getAgentLevel() != 1 && !(paUserInfo.getUserCode()).equals(paMerInfo.getUserCode())) {
//                	merInfo.setMobile(merInfo.getMobile().replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));//商户手机号
                	String mobilePhone = paMerInfo.getMobilePhone();
                	if (StringUtils.isNotBlank(mobilePhone)) {
                		paMerInfo.setMobilePhone(mobilePhone.replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));//所属盟主手机号
					}
				}
                maps.put("mobilePhone",paMerInfo.getMobilePhone());
                maps.put("userCode", paMerInfo.getUserCode());
                maps.put("realName", paMerInfo.getRealName());
                maps.put("nickName",paMerInfo.getNickName());
                maps.put("mobile",paMerInfo.getMobile());
                maps.put("createTime",paMerInfo.getCreateTime() == null ? "" : sdf1.format(paMerInfo.getCreateTime()));
                maps.put("actTime",paMerInfo.getActTime() == null ? "" : sdf1.format(paMerInfo.getActTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"merchantNo","merchantName","merType","mobilePhone","userCode","realName","nickName","mobile","createTime","actTime"};
        String[] colsName = new String[]{"商户编号","商户名称","商户类型","商户手机号码","所属盟主编号","所属盟主姓名","所属盟主昵称","所属盟主手机","注册日期","激活日期"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("盟主商户导出失败!",e);
        }finally {
            if(ouputStream != null){
                ouputStream.close();
            }
        }
	}
}

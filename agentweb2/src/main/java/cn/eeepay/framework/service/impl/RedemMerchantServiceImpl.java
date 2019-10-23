package cn.eeepay.framework.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.UserInfoDao;
import cn.eeepay.framework.daoRedem.RedemMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.AgentUserEntity;
import cn.eeepay.framework.model.AgentUserInfo;
import cn.eeepay.framework.model.RedemMerchantBean;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.redemActive.MerInfoTotal;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.RedemMerchantService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.StringUtil;

/**
 * Created by 666666 on 2018/5/9.
 */
@Service
public class RedemMerchantServiceImpl implements RedemMerchantService {

    private static final Logger log = LoggerFactory.getLogger(RedemMerchantServiceImpl.class);
    private static final String numberPattern = "((100)|([1-9]\\d)|(\\d))(\\.\\d{1,4})?";
    @Resource
    private RedemMerchantDao redemMerchantDao;
    @Resource
    private AgentInfoDao agentInfoDao;
    @Resource
    private SeqService seqService;
    @Resource
    private UserInfoDao userInfoDao;
    
    @Resource
    private AgentInfoService agentInfoService;

    @Override
    public List<RedemMerchantBean> selectByUserRedemMerchant(RedemMerchantBean userRedemMerchant, AgentInfo loginAgent,Page<ResponseBean> page) {
        String rootMerchantNo = redemMerchantDao.selectRootMerchantNo(loginAgent.getAgentNo());
        List<RedemMerchantBean> redemMerchantBeans = redemMerchantDao.selectByUserRedemMerchant(userRedemMerchant, loginAgent, page);
        if (redemMerchantBeans != null && !redemMerchantBeans.isEmpty()){
            for (RedemMerchantBean bean : redemMerchantBeans){
                bean.setDirectMerchant(StringUtils.equals(rootMerchantNo, bean.getParMerNo()));
                bean.setNotOpenAgent(StringUtils.equals(loginAgent.getAgentNode(), bean.getAgentNode()));
//                String mobileUsername = bean.getMobileUsername();
//                if (StringUtils.isNotBlank(mobileUsername)){
//                    bean.setMobileUsername(mobileUsername.replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//                }
            }
        }
        return redemMerchantBeans;
    }

    @Override
    public MerInfoTotal selectSum(RedemMerchantBean userRedemMerchant, AgentInfo loginAgent, Page<ResponseBean> page) {
        MerInfoTotal list = redemMerchantDao.selectSum(userRedemMerchant, loginAgent, page);
        return list;
    }

    @Override
    public RedemMerchantBean queryUpdateMerchantInfo(String merchantNo, AgentInfo loginAgent) {
        if (loginAgent.getAgentLevel() != 1){
            throw new AgentWebException("登陆代理商不是一级代理商,不能查看相应的信息");
        }
        String rootMerchantNo = redemMerchantDao.selectRootMerchantNo(loginAgent.getAgentNo());
        RedemMerchantBean userRedemMerchant = redemMerchantDao.queryMerchantInfoByMerchantNoAndRootMerNo(merchantNo, rootMerchantNo);
        if (userRedemMerchant == null){
            throw new AgentWebException("该商户不是直推商户,无法开通代理商帐号");
        }
        AgentInfo agentInfo = agentInfoDao.selectByAgentNo(userRedemMerchant.getAgentNo());
        RedemMerchantBean result = agentInfoTranslate2RedemMerchant(agentInfo);
        result.setMerchantNo(merchantNo);

        result.setOrdparProShare(redemMerchantDao.selectMerchantShare(userRedemMerchant.getAgentNo(), merchantNo, "3","A"));
        result.setGoldparProShare(redemMerchantDao.selectMerchantShare(userRedemMerchant.getAgentNo(), merchantNo, "4","A"));
        result.setDiamparProShare(redemMerchantDao.selectMerchantShare(userRedemMerchant.getAgentNo(), merchantNo, "5","A"));
        result.setAgentFee(redemMerchantDao.selectMerchantShare(userRedemMerchant.getAgentNo(), merchantNo,null,"D"));
        result.setOemFee(userRedemMerchant.getOemFee());
        return result;
    }

    private RedemMerchantBean agentInfoTranslate2RedemMerchant(AgentInfo agentInfo) {
        RedemMerchantBean bean = new RedemMerchantBean();
        bean.setAgentNo(agentInfo.getAgentNo());
        bean.setAgentNode(agentInfo.getAgentNode());
        bean.setEmail(agentInfo.getEmail());
        bean.setUserName(agentInfo.getAgentName());
        bean.setMobileUsername(agentInfo.getMobilephone());
        bean.setPhone(agentInfo.getPhone());
        bean.setSaleName(agentInfo.getSaleName());
        bean.setLinkName(agentInfo.getLinkName());

        bean.setProvince(agentInfo.getProvince());
        bean.setCity(agentInfo.getCity());
        bean.setArea(agentInfo.getArea());
        bean.setAddress(agentInfo.getAddress());
        bean.setAgentArea(agentInfo.getAgentArea());

        bean.setAccountType(agentInfo.getAccountType());
        bean.setAccountName(agentInfo.getAccountName());
        bean.setAccountNo(agentInfo.getAccountNo());
        bean.setAccountProvince(agentInfo.getAccountProvince());
        bean.setAccountCity(agentInfo.getAccountCity());
        bean.setBankName(agentInfo.getBankName());
        bean.setCnapsNo(agentInfo.getCnapsNo());
        return bean;
    }

    @Override
    public RedemMerchantBean queryAddMerchantInfo(String merchantNo, AgentInfo loginAgent) {
        if (loginAgent.getAgentLevel() != 1){
            throw new AgentWebException("登陆代理商不是一级代理商,不能查看相应的信息");
        }
        String rootMerchantNo = redemMerchantDao.selectRootMerchantNo(loginAgent.getAgentNo());
        RedemMerchantBean userRedemMerchant = redemMerchantDao.queryMerchantInfoByMerchantNoAndRootMerNo(merchantNo, rootMerchantNo);
        if (userRedemMerchant == null){
            throw new AgentWebException("该商户不是直推商户,无法开通代理商帐号");
        }
        return userRedemMerchant;
    }

    @Override
    public boolean userAddAgent(RedemMerchantBean userRedemMerchant, AgentInfo loginAgent) {
        if (loginAgent.getAgentLevel() != 1){
            throw new AgentWebException("登陆代理商不是一级代理商,不能查看相应的信息");
        }
        String rootMerchantNo = redemMerchantDao.selectRootMerchantNo(loginAgent.getAgentNo());
        log.info("超级兑开通二级代理商, 母码:" + rootMerchantNo);
        RedemMerchantBean exsitMerchantNo = redemMerchantDao.queryMerchantInfoByMerchantNoAndRootMerNo(userRedemMerchant.getMerchantNo(), rootMerchantNo);
        log.info("超级兑开通二级代理商, 商户信息:" + exsitMerchantNo);
        if(exsitMerchantNo == null){
            throw new AgentWebException("该商户不是直推商户,无法开通代理商帐号");
        }
        if (!StringUtils.equals(exsitMerchantNo.getAgentNo(), loginAgent.getAgentNo())){
            throw new AgentWebException("该商户已经开通二级代理商");
        }
        if (StringUtils.isBlank(exsitMerchantNo.getMerNode())){
            throw new AgentWebException("该商户节点异常,无法开通代理商帐号");
        }
        if (!StringUtils.equals("0-" + rootMerchantNo + "-" + exsitMerchantNo.getMerchantNo() + "-", exsitMerchantNo.getMerNode())){
            log.warn("超级兑开通二级代理商, 该商户节点异常:" + exsitMerchantNo.getMerNode());
            throw new AgentWebException("该商户节点异常,无法开通代理商帐号");
        }

        // 将参数转化为agentInfo信息,并校验相应的必填项
        AgentInfo agentInfo = redemMerchantTranslate2AgentInfo(userRedemMerchant, loginAgent);
        agentInfo.setTeamId(loginAgent.getTeamId());
        if(agentInfoDao.existAgentByMobilephoneAndTeamId(agentInfo)>0){
            throw new AgentWebException("该组织下的手机号码或者邮箱代理商名称已存在!");
        }
        if(agentInfoDao.existUserByMobilephoneAndTeamId(agentInfo)>0){
            log.info("该组织下的手机号码在User_info表已存在!");
            throw new AgentWebException("该组织下的手机号码已被使用!");
        }
        if(agentInfoDao.existUserByEmailAndTeamId(agentInfo)>0){
            log.info("该组织下的邮箱在User_info表已存在!");
            throw new AgentWebException("该组织下的邮箱已被使用!");
        }
        String agent_no = seqService.createKey("agent_no");
        agentInfo.setAgentNo(agent_no);
        agentInfo.setAgentNode(loginAgent.getAgentNode() + agent_no + "-");
        agentInfo.setAgentLevel(loginAgent.getAgentLevel()+1);
        agentInfo.setParentId(loginAgent.getAgentNo());
        agentInfo.setOneLevelId(loginAgent.getOneLevelId());
        agentInfo.setIsOem(loginAgent.getIsOem());
        agentInfo.setCountLevel(loginAgent.getCountLevel());
        agentInfo.setIsApprove(loginAgent.getIsApprove());
        agentInfo.setCreator(loginAgent.getAgentNo());
        agentInfo.setStatus("1");

        // 插入商户的对应级别分润成本
        if (StringUtils.isBlank(userRedemMerchant.getOrdparProShare()) || !userRedemMerchant.getOrdparProShare().matches(numberPattern)){
            throw new AgentWebException("请配置正确会员入账比例");
        }
        if (StringUtils.isBlank(userRedemMerchant.getGoldparProShare()) || !userRedemMerchant.getGoldparProShare().matches(numberPattern)){
            throw new AgentWebException("请配置正确黄金会员入账比例");
        }
        if (StringUtils.isBlank(userRedemMerchant.getDiamparProShare()) || !userRedemMerchant.getDiamparProShare().matches(numberPattern)){
            throw new AgentWebException("请配置正确钻石会员入账比例");
        }
        if (StringUtils.isBlank(userRedemMerchant.getAgentFee()) || !userRedemMerchant.getAgentFee().matches("[0-9\\.]*")){
            throw new AgentWebException("请配置正确积分兑换成本");
        }else{
            try {
                if(Double.valueOf(userRedemMerchant.getAgentFee())>Double.valueOf(exsitMerchantNo.getOemFee())){
                    throw new AgentWebException("积分兑换成本不能大于oem成本");
                }
            }catch (Exception e){
                throw new AgentWebException("请配置正确积分兑换成本");
            }
        }
        //保存代理商分润配置
        saveConfig(agentInfo,userRedemMerchant);

        // 新增代理商信息
        agentInfoDao.insertAgentInfo(agentInfo);
        // 更新商户代理商节点信息
        redemMerchantDao.updateMerchantAgentNode(exsitMerchantNo.getMerNode(), agentInfo.getAgentNo(), agentInfo.getAgentNode());
        // 创建代理商管理员
        saveAgentUserInfo(agentInfo);
        // 开设代理商账户
        openAgentAccount(agentInfo.getAgentNo());
        return true;
    }

    @Override
    public boolean userUpdateAgent(RedemMerchantBean userRedemMerchant, AgentInfo loginAgent) {
        if (loginAgent.getAgentLevel() != 1){
            throw new AgentWebException("登陆代理商不是一级代理商,不能查看相应的信息");
        }
        String rootMerchantNo = redemMerchantDao.selectRootMerchantNo(loginAgent.getAgentNo());
        RedemMerchantBean exsitMerchantNo = redemMerchantDao.queryMerchantInfoByMerchantNoAndRootMerNo(userRedemMerchant.getMerchantNo(), rootMerchantNo);
        if(exsitMerchantNo == null){
            throw new AgentWebException("该商户不是直推商户,无法修改代理商信息");
        }
        if (StringUtils.equals(exsitMerchantNo.getAgentNo(), loginAgent.getAgentNo())){
            throw new AgentWebException("该商户还未开通二级代理商,无法进行修改");
        }
        AgentInfo existAgentNo = agentInfoDao.selectByAgentNo(userRedemMerchant.getAgentNo());
        if (existAgentNo == null){
            throw new AgentWebException("没找到代理商信息");
        }
        AgentInfo agentInfo = redemMerchantTranslate2AgentInfo(userRedemMerchant, loginAgent);
        agentInfo.setAgentNo(userRedemMerchant.getAgentNo());
        agentInfo.setAgentNode(userRedemMerchant.getAgentNode());
        agentInfo.setId(existAgentNo.getId());
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
        Map<String,Object> map = agentInfoDao.getAgentEntity(agentInfo.getAgentNo(),info.getTeamId());

        agentInfoDao.updateAgent(agentInfo);
        if (StringUtils.isBlank(userRedemMerchant.getOrdparProShare()) || !userRedemMerchant.getOrdparProShare().matches(numberPattern)){
            throw new AgentWebException("请配置正确会员入账比例");
        }
        if (StringUtils.isBlank(userRedemMerchant.getGoldparProShare()) || !userRedemMerchant.getGoldparProShare().matches(numberPattern)){
            throw new AgentWebException("请配置正确黄金会员入账比例");
        }
        if (StringUtils.isBlank(userRedemMerchant.getDiamparProShare()) || !userRedemMerchant.getDiamparProShare().matches(numberPattern)){
            throw new AgentWebException("请配置正确钻石会员入账比例");
        }
        if (StringUtils.isBlank(userRedemMerchant.getAgentFee()) || !userRedemMerchant.getAgentFee().matches("[0-9\\.]*")){
            throw new AgentWebException("请配置正确积分兑换成本");
        }else{
            try {
                if(Double.valueOf(userRedemMerchant.getAgentFee())>Double.valueOf(exsitMerchantNo.getOemFee())){
                    throw new AgentWebException("积分兑换成本不能大于oem成本");
                }
            }catch (Exception e){
                throw new AgentWebException("请配置正确积分兑换成本");
            }
        }
        //保存代理商分润配置
        saveConfig(agentInfo,userRedemMerchant);

        // 更新代理商系统的用户信息
        if (map == null || map.isEmpty()){
            throw new AgentWebException("更新失败");
        }
        String userId=(String)map.get("user_id");
        AgentUserInfo agentUser=new AgentUserInfo();
        agentUser.setUserName(agentInfo.getAgentName());
        agentUser.setMobilephone(agentInfo.getMobilephone());
        agentUser.setEmail(agentInfo.getEmail());
        agentUser.setUserId(userId);
        UserInfo userInfo = userInfoDao.findUserInfoByUserId(userId);
        if(!userInfo.getMobilephone().equals(agentInfo.getMobilephone())){
            agentUser.setPassword(new Md5PasswordEncoder().encodePassword("123456",agentInfo.getMobilephone()));
        }
        agentInfoDao.updateAgentUser(agentUser);
        return true;
    }

    private void saveConfig(AgentInfo agentInfo,RedemMerchantBean userRedemMerchant){
        saveShareConfig(agentInfo,userRedemMerchant,"3",userRedemMerchant.getOrdparProShare());
        saveShareConfig(agentInfo,userRedemMerchant,"4",userRedemMerchant.getGoldparProShare());
        saveShareConfig(agentInfo,userRedemMerchant,"5",userRedemMerchant.getDiamparProShare());
        Map<String,Object> map=redemMerchantDao.getRdmpAgentFeeConfig(agentInfo.getAgentNo(), userRedemMerchant.getMerchantNo(),userRedemMerchant.getAgentFee());
        if(map!=null){
            redemMerchantDao.updateRdmpAgentFeeConfig(agentInfo.getAgentNo(), userRedemMerchant.getMerchantNo(),userRedemMerchant.getAgentFee());
        }else {
            redemMerchantDao.insertRdmpAgentFeeConfig(agentInfo.getAgentNo(), userRedemMerchant.getMerchantNo(),userRedemMerchant.getAgentFee());
        }
    }
    private void saveShareConfig(AgentInfo agentInfo,RedemMerchantBean userRedemMerchant,String merCapa,String shareRate){
        Map<String,Object> map=redemMerchantDao.getRdmpAgentShareConfig(agentInfo.getAgentNo(), userRedemMerchant.getMerchantNo(), merCapa,shareRate);
        if(map!=null){
            redemMerchantDao.updateRdmpAgentShareConfig(agentInfo.getAgentNo(), userRedemMerchant.getMerchantNo(), merCapa,shareRate);
        }else {
            redemMerchantDao.insertRdmpAgentShareConfig(agentInfo.getAgentNo(), userRedemMerchant.getMerchantNo(), merCapa,shareRate);
        }
    }
    private void saveAgentUserInfo(AgentInfo agent) {
        AgentUserInfo agentUser=agentInfoDao.selectAgentUser(agent.getMobilephone(),agent.getTeamId().toString());
        if(agentUser==null){
            agentUser=new AgentUserInfo();
            agentUser.setUserName(agent.getAgentName());
            String userId=seqService.createKey(Constants.AGENT_USER_SEQ, new BigInteger("1000000000000000000"));
            agentUser.setUserId(userId);
            agentUser.setTeamId(agent.getTeamId().toString());
            agentUser.setMobilephone(agent.getMobilephone());
            agentUser.setEmail(agent.getEmail());
            agentUser.setPassword(new Md5PasswordEncoder().encodePassword("123456",agent.getMobilephone()));
            agentInfoDao.insertAgentUser(agentUser);
        }
        AgentUserEntity entity=agentInfoDao.selectAgentUserEntity(agentUser.getUserId(),agent.getAgentNo());
        if(entity==null){
            entity=new AgentUserEntity();
            entity.setEntityId(agent.getAgentNo());
            entity.setIsAgent("1");
            entity.setUserId(agentUser.getUserId());
            agentInfoDao.insertAgentEntity(entity);
            agentInfoDao.insertAgentRole(entity.getId());
        }else{
            throw new AgentWebException("代理商手机号已注册");
        }
    }
    private void openAgentAccount(final String agentNo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(100);
                    //不需要传科目号
                    String acc = ClientInterface.createAgentAccount(agentNo, "224105");
                    log.info("开立代理商账户(224105) --> " + acc);
                    agentInfoDao.updateAgentAccount(agentNo, 1);
                }catch(Exception e){
                    log.error("开立代理商账户异常",e);
                }
            }
        }).start();
    }
    private AgentInfo redemMerchantTranslate2AgentInfo(RedemMerchantBean userRedemMerchant, AgentInfo loginAgent) {
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setEmail(userRedemMerchant.getEmail());
        agentInfo.setAgentName(userRedemMerchant.getUserName());
        agentInfo.setMobilephone(userRedemMerchant.getMobileUsername());
        agentInfo.setPhone(userRedemMerchant.getPhone());
        agentInfo.setSaleName(userRedemMerchant.getSaleName());
        agentInfo.setLinkName(userRedemMerchant.getLinkName());

        agentInfo.setProvince(userRedemMerchant.getProvince());
        agentInfo.setCity(userRedemMerchant.getCity());
        agentInfo.setArea(userRedemMerchant.getArea());
        agentInfo.setAddress(userRedemMerchant.getAddress());
        agentInfo.setAgentArea(userRedemMerchant.getAgentArea());

        agentInfo.setAccountType(userRedemMerchant.getAccountType());
        agentInfo.setAccountName(userRedemMerchant.getAccountName());
        agentInfo.setAccountNo(userRedemMerchant.getAccountNo());
        agentInfo.setAccountProvince(userRedemMerchant.getAccountProvince());
        agentInfo.setAccountCity(userRedemMerchant.getAccountCity());
        agentInfo.setBankName(userRedemMerchant.getBankName());
        agentInfo.setCnapsNo(userRedemMerchant.getCnapsNo());
        if (StringUtils.isBlank(agentInfo.getMobilephone()) || !agentInfo.getMobilephone().matches("^1\\d{10}$")){
            throw new AgentWebException("请填写正确的手机号");
        }
        if (StringUtils.isBlank(agentInfo.getEmail()) || !agentInfo.getEmail().matches("^.*?@.+?$")){
            throw new AgentWebException("请填写正确的邮箱地址");
        }
        return agentInfo;
    }

    @Override
    public Map<String, Object> queryMerchantDetails(String merchantNo, AgentInfo loginAgent) {
        Map<String, Object> merchantInfo = redemMerchantDao.queryMerchantInfoByMerchantNo(merchantNo);
        if (merchantInfo == null || merchantInfo.isEmpty()){
            throw new AgentWebException("查不到该商户的信息");
        }
        String agent_node = StringUtil.filterNull(merchantInfo.get("agent_node"));
        if (!agent_node.startsWith(loginAgent.getAgentNode())){
            throw new AgentWebException("您无权查看该商户的信息");
        }
        String par_mer_no = StringUtil.filterNull(merchantInfo.get("par_mer_no"));
        String agent_no = StringUtil.filterNull(merchantInfo.get("agent_no"));
        String mobile_username = StringUtil.filterNull(merchantInfo.get("mobile_username"));
        String rootMerchantNo = redemMerchantDao.selectRootMerchantNo(loginAgent.getAgentNo());
        merchantInfo.put("directMerchant", StringUtils.equals(par_mer_no, rootMerchantNo));
//        merchantInfo.put("notOpenAgent", StringUtils.equals(agent_no, loginAgent.getOneLevelId()));
        merchantInfo.put("directUserNumber", redemMerchantDao.countDirectUserNumber(merchantNo));
        merchantInfo.put("directMemberNumber", redemMerchantDao.countDirectMemberNumber(merchantNo));
        merchantInfo.put("superUserTime", redemMerchantDao.selectMemberTime(merchantNo, "2"));
        merchantInfo.put("memberTime", redemMerchantDao.selectMemberTime(merchantNo, "3"));
        merchantInfo.put("superMemberTime", redemMerchantDao.selectMemberTime(merchantNo, "4"));
        merchantInfo.put("superDiamondsMemberTime", redemMerchantDao.selectMemberTime(merchantNo, "5"));
        merchantInfo.put("agentNo",agent_no);

        String business_code = StringUtil.filterNull(merchantInfo.get("business_code"));
        String mobile_no = StringUtil.filterNull(merchantInfo.get("mobile_no"));
        String account_no = StringUtil.filterNull(merchantInfo.get("account_no"));
        merchantInfo.put("business_code", business_code.replaceAll("^(.{4}).*?(.{4})$", "$1****$2"));
//        merchantInfo.put("mobile_no", mobile_no.replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//        merchantInfo.put("mobile_username", mobile_username.replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
        merchantInfo.put("account_no", account_no.replaceAll("^(.{4}).*?(.{4})$", "$1****$2"));
        return merchantInfo;
    }

    @Override
    public List<Map<String, Object>> listBalanceHis(String startTime, String endTime, String service, String merchantNo, Page<List<Map<String, Object>>> page) {
        return redemMerchantDao.listBalanceHis(startTime, endTime, service, merchantNo, page);
    }
}

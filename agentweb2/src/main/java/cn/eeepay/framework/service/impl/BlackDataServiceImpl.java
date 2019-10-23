package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.action.RiskHandleAction;
import cn.eeepay.framework.dao.BlackDataDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.model.PosCardBin;
import cn.eeepay.framework.model.blackAgent.*;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.BlackDataService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.SystemColor.info;

@Service("BlackDataService")
class BlackDataServiceImpl implements BlackDataService {
    private static final Logger log = LoggerFactory.getLogger(BlackDataServiceImpl.class);
    private final String Token = "a185faf4c55807bca4bcfdaae06b58fa";
    private final String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
    private final String PATH = "/riskhandle/answerjpush";
    @Resource
    private BlackDataDao blackDataDao;
    
    @Autowired
    private AgentInfoService agentInfoService;
    @Resource
    SysDictService sysDictService;

    @Override
    public List<BlackInfo> selectByParam(Page<BlackInfo> page, BlackInfo blackInfo) {
        log.info("条件查询黑名单开始");
        List<BlackInfo> list = blackDataDao.selectByParam(page, blackInfo);
        if (list != null) {
            for (BlackInfo info : list) {
                if (StringUtils.isNotBlank(info.getRiskLastDealStatus())) {
                    if (info.getRiskLastDealStatus().equals("2")) {
                        info.setMerLastDealStatus("3");
                    }
                }
                List<RiskNewAnswer> riskNewAnswers = blackDataDao.selectRiskNewAnswer(info.getOrderNo());
                if (riskNewAnswers != null && riskNewAnswers.size() > 1) {
                    info.setFirstRecord(true);
                } else {
                    info.setFirstRecord(false);
                }
            }
        }
        return list;
    }


    @Override
    public RiskNewAnswer selectRiskNewAnwser(String orderNo) {
        log.info("开始查询");
        RiskNewAnswer riskNewAnswer = new RiskNewAnswer();
        List<RiskNewAnswer> riskNewAnswers = blackDataDao.selectRiskNewAnswer(orderNo);
        if (riskNewAnswers != null && riskNewAnswers.size() > 1) {
            riskNewAnswer = riskNewAnswers.get(riskNewAnswers.size()-1);
            riskNewAnswer.setFirstRecord(false);
        } else if (riskNewAnswers != null && riskNewAnswers.size() == 1) {
            riskNewAnswer = riskNewAnswers.get(riskNewAnswers.size()-1);
            riskNewAnswer.setFirstRecord(true);

        } else {
            throw new NullPointerException("初次触犯风控处理记录为空");
        }
        return riskNewAnswer;
    }

    @Override
    public String agentReply(String orderNo, String replyFilesName, String replyRemark) {
        log.info("开始回复");
        String str = null;
        BlackInfo blackInfo = blackDataDao.selectByOrderNo(orderNo);
        List<RiskNewAnswer> riskNewAnswers = blackDataDao.selectRiskNewAnswer(blackInfo.getOrderNo());
        if (blackInfo != null && riskNewAnswers != null) {
            //String merchantNo = blackInfo.getAgentNo();
        	String merchantNo = agentInfoService.getCurAgentNo();
            String authCode = Md5.md5Str(USER_INFO_ID + merchantNo + Token).toUpperCase();
            String replierType = "1";
            String riskOrderNo = riskNewAnswers.get(0).getOrderNo();
            String origOrderNo = blackInfo.getOrderNo();
            String replyAgain = "0";
            String paramStr = "&merchantNo=" + merchantNo + "&replierType=" + replierType
                    + "&riskOrderNo=" + riskOrderNo + "&origOrderNo=" + origOrderNo + "&replyRemark=" + replyRemark + "&replyAgain=" + replyAgain + "&token=" + Token + "&authCode=" + authCode + "&replyFilesName=" + replyFilesName;
              String url = sysDictService.getByKey("CORE_URL").getSysValue() + "/riskhandle/answer";
            log.info("开始访问core 处理回复");
             //str = HttpUtils.sendPost("http://192.168.3.31:8091/core2/riskhandle/answer", paramStr, "UTF-8");
             str = HttpUtils.sendPost(url, paramStr, "UTF-8");
            log.info("回复结果：{}",str);
        }
        return str;
    }

    @Override
    public Map<String, Object> selectRiskHandleDetail(String orderNo) {
        //基本信息
        Map<String, Object> result = new HashMap<>();
        BlackInfo blackInfo = blackDataDao.selectByOrderNo(orderNo);

        //商户回复内容
        ReplyRecord replyRecord = blackDataDao.selectReplyRecord(blackInfo.getOrderNo());
        if (replyRecord != null) {
            if (StringUtils.isNotBlank(replyRecord.getReplyFilesName())) {
                replyRecord.setFilesList(getFileList(replyRecord.getReplyFilesName()));
            }
            result.put("replyRecord", replyRecord);
            //风控处理内容
            DealRecord dealRecord = blackDataDao.selectByOrder(replyRecord.getDealRecordOrderNo());
            result.put("dealRecord", dealRecord);
        }
        //往来记录
        List<DealReplyRecord> dealReplyRecords = blackDataDao.selectDealReplyRecord(orderNo);
        for (DealReplyRecord record : dealReplyRecords) {
            if (StringUtils.isNotBlank(record.getFilesName())) {
                record.setFilesList(getFileList(record.getFilesName()));
            }
        }
        result.put("dealReplyRecords", dealReplyRecords);
        //风控处理回显
        DealRecord deal = blackDataDao.selectByOrigOrderNoAndStatus(blackInfo.getOrderNo(), "1");
        result.put("deal", deal);
        return result;

    }

   /* @Override
    public String selectRiskHandleDetail(String orderNo) {
        log.info("查询回复记录");
        String str = null;
        BlackInfo blackInfo = blackDataDao.selectByOrderNo(orderNo);
        if (blackInfo != null) {
            String merchantNo = blackInfo.getMerchantNo();
            String origOrderNo = blackInfo.getOrderNo();
            String authCode = Md5.md5Str(USER_INFO_ID + merchantNo + Token).toUpperCase();
            String replierType = "1";
            String paramStr = "&merchantNo=" + merchantNo + "&origOrderNo=" + origOrderNo + "&token=" + Token + "&authCode=" + authCode + "&replierType=" + replierType;
              String url = sysDictService.getByKey("CORE_URL").getSysValue() + "/riskhandle/historyanswer";
            str = HttpUtils.sendPost("http://192.168.3.31:8666/core2/riskhandle/historyanswer", paramStr, "UTF-8");
*//*
            str = HttpUtils.sendPost(url, paramStr, "UTF-8");
*//*
        }
        return str;
    }*/

    public List<FileType> getFileList(String filesName){
        if(filesName!=null&&!"".equals(filesName)){
            String[] strs=filesName.split(",");
            if(strs!=null&&strs.length>0){
                List<FileType> list=new ArrayList<FileType>();
                for(String str:strs){
                    FileType fileType=checkImg(str);
                    if(fileType!=null){
                        list.add(fileType);
                    }
                }
                return list;
            }
        }
        return null;
    }

    /**
     *
     * @param name
     * 目前只识别JPG JPEG png
     */
    private FileType checkImg(String name){
        if(StringUtils.isNotBlank(name)){
            FileType fileType=new FileType();
            fileType.setName(name);
            fileType.setType("1");//默认文件
            String suffix=name.substring(name.lastIndexOf(".")+1,name.length());
            if(suffix!=null){
                suffix=suffix.toUpperCase();
                if("JPG".equals(suffix.toUpperCase())
                        ||"JPEG".equals(suffix.toUpperCase())
                        ||"PNG".equals(suffix.toUpperCase())){
                    fileType.setType("2");
                    fileType.setImgUrl(CommonUtil.getImgUrlAgent(name));
                }
            }
            return fileType;
        }
        return null;
    }
}

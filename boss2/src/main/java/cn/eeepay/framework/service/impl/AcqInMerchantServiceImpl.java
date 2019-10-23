package cn.eeepay.framework.service.impl;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.dao.AcqInMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqInMerchant;
import cn.eeepay.framework.model.AcqInMerchantFileInfo;
import cn.eeepay.framework.model.AcqInMerchantRecord;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AcqInMerchantService;
import cn.eeepay.framework.service.AcqTerminalService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.Md5;

@Service("acqInMerchantService")
@Transactional
public class AcqInMerchantServiceImpl implements AcqInMerchantService {
	private static final Logger log = LoggerFactory.getLogger(AcqInMerchantServiceImpl.class);
	@Resource
	private AcqInMerchantDao acqInMerchantDao;

	@Resource
	private SysDictService sysDictService;

	// 收单机构终端
	@Resource
	private AcqTerminalService acqTerminalService;

	private final String Token = "a185faf4c55807bca4bcfdaae06b58fa";
	private final String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
	private final String PATH = "/riskhandle/commonJPush";
	
	
	
	public List<AcqInMerchant> selectAllInfo(Page<AcqInMerchant> page, AcqInMerchant amc) {
		List<AcqInMerchant> selectAllInfo = acqInMerchantDao.selectAllInfo(page, amc);
		Map<String, String> map = this.getIntoSource();
		for (AcqInMerchant am : selectAllInfo) {
			if (am.getIntoSource() != null) {
				am.setIntoSource(map.get(am.getIntoSource()));
			}
		}
		return selectAllInfo;
	}

	// 获取渠道渠道
	private Map<String, String> getIntoSource() {
		List<SysDict> dict = sysDictService.selectByOnlyKey("ACQ_MER_INTO_SOURCE");
		Map<String, String> map = new HashMap<>();
		map.put("", "全部");
		for (SysDict sysDict : dict) {
			map.put(sysDict.getSysValue(), sysDict.getSysName());
		}
		return map;
	}

	// 获取经营范围
	private Map<String, String> getScope() {
		List<SysDict> dict = sysDictService.selectByOnlyKey("sys_mcc");
		Map<String, String> map = new HashMap<>();
		map.put("", "全部");
		for (SysDict sysDict : dict) {
			map.put(sysDict.getSysValue(), sysDict.getSysName());
		}
		return map;
	}

	public AcqInMerchant selectByPrimaryKey(Long id) {
		AcqInMerchant am = acqInMerchantDao.selectByPrimaryKey(id);
		filterAm(am);
		return am;
	}

	public AcqInMerchant filterAm(AcqInMerchant am) {
		// 渠道来源
		Map<String, String> map = this.getIntoSource();
		if (am.getIntoSource() != null) {
			am.setIntoSource(map.get(am.getIntoSource()));
		}

		// 经营范围
		Map<String, String> mapScope = this.getScope();
		if (am.getOneScope() != null) {
			am.setOneScope(mapScope.get(am.getOneScope()));
		}
		if (am.getTwoScope() != null) {
			am.setTwoScope(mapScope.get(am.getTwoScope()));
		}

		// 进件时间
		if (am.getCreateTime() != null) {
			am.setCreateTimeStr(DateUtil.getLongFormatDate(am.getCreateTime()));
		}

		// 身份证有效期
		if (am.getIdValidStart() != null) {
			am.setPersionId(DateUtil.getDefaultFormatDate(am.getIdValidStart()) + " - ");
		}

		if (am.getIdValidEnd() != null) {
			am.setPersionId(am.getPersionId() + DateUtil.getDefaultFormatDate(am.getIdValidEnd()));
		}

		// 营业执照有效期
		if (am.getCharterValidStart() != null) {
			am.setCharterValidStr(DateUtil.getDefaultFormatDate(am.getCharterValidStart()) + " - ");
		}

		if (am.getCharterValidEnd() != null) {
			am.setCharterValidStr(am.getCharterValidStr() + DateUtil.getDefaultFormatDate(am.getCharterValidEnd()));
		}
		return am;
	}

	public int updateMcc(String id, String mcc) {
		return acqInMerchantDao.updateMcc(id, mcc);
	}

	@Override
	public List<AcqInMerchantRecord> selectRecordByPrimaryKey(Long id) {
		return acqInMerchantDao.selectRecordByPrimaryKey(id);
	}

	@Override
	public List<AcqInMerchant> exportInfo(AcqInMerchant am) {
		List<AcqInMerchant> exportInfo = acqInMerchantDao.exportInfo(am);
		for (AcqInMerchant acqInMerchant : exportInfo) {
			this.filterAm(acqInMerchant);
		}

		return exportInfo;
	}

	@Override
	public void exportAcqInMerchant(List<AcqInMerchant> list, HttpServletResponse response) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "收单商户进件查询" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
		} else {

			for (AcqInMerchant or : list) {
				Map<String, String> maps = new HashMap<String, String>();

				if (or.getMerchantType() != null) {
					if (1 == or.getMerchantType()) {
						maps.put("merchantType", "个体收单商户");
					} else if (2 == or.getMerchantType()) {
						maps.put("merchantType", "企业收单商户");
					} else {
						maps.put("merchantType", "");
					}

					if (1 == or.getAccountType()) {
						maps.put("accountType", "对私");
					} else if (2 == or.getAccountType()) {
						maps.put("accountType", "对公");
					} else {
						maps.put("accountType", "");
					}

					maps.put("mcc", or.getMcc() == null ? "" : or.getMcc().toString());
					maps.put("acqIntoNo", or.getAcqIntoNo() == null ? "" : or.getAcqIntoNo().toString());
					maps.put("intoSource", or.getIntoSource() == null ? "" : or.getIntoSource().toString());
					maps.put("agentNo", or.getAgentNo() == null ? "" : or.getAgentNo().toString());
					maps.put("oneAgentNo", or.getOneAgentNo() == null ? "" : or.getOneAgentNo().toString());

					maps.put("scope", or.getOneScope() + "-" + or.getTwoScope());
					maps.put("allAddress", or.getAllAddress() == null ? "" : or.getAllAddress().toString());
					maps.put("legalPerson", or.getLegalPerson() == null ? "" : or.getLegalPerson().toString());
					maps.put("legalPersonId", or.getLegalPersonId() == null ? "" : or.getLegalPersonId().toString());

					maps.put("persionId", or.getPersionId() == null ? "" : or.getPersionId());
					maps.put("bankNo", or.getBankNo() == null ? "" : or.getBankNo());
					maps.put("accountName", or.getAccountName() == null ? "" : or.getAccountName());
					maps.put("accountBank", or.getAccountBank() == null ? "" : or.getAccountBank());

					maps.put("accountAddress", or.getAccountAddress() == null ? "" : or.getAccountAddress());
					maps.put("bankBranch", or.getBankBranch() == null ? "" : or.getBankBranch());
					maps.put("lineNumber", or.getLineNumber() == null ? "" : or.getLineNumber());
					maps.put("charterName", or.getCharterName() == null ? "" : or.getCharterName());
					maps.put("charterNo", or.getCharterNo() == null ? "" : or.getCharterNo());

					maps.put("charterValidStr", or.getCharterValidStr() == null ? "" : or.getCharterValidStr());
					data.add(maps);
				}
			}
			ListDataExcelExport export = new ListDataExcelExport();
			String[] cols = new String[] { "mcc", "acqIntoNo", "intoSource", "agentNo", "oneAgentNo", "merchantType",
					"scope", "allAddress", "legalPerson", "legalPersonId", "persionId", "accountType", "bankNo",
					"accountName", "accountBank", "accountAddress", "bankBranch", "lineNumber", "charterName",
					"charterNo", "charterValidStr" };

			String[] colsName = new String[] { "MCC", "收单进件编号", "进件来源", "所属代理商编号", "所属一级代理商编号", "商户类型", "经营范围", "经营地区",
					"法人姓名", "身份证号码", "身份证有效期", "账户类型", "银行卡号", "开户名", "开户银行", "开户地区", "支行名称", "联行号", "营业执照名称", "营业执照编号",
					"营业执照有效期" };
			OutputStream ouputStream = null;
			try {
				ouputStream = response.getOutputStream();
				export.export(cols, colsName, data, response.getOutputStream());
			} catch (Exception e) {
				log.error("收单商户进件查询导出!", e);
			} finally {
				if (ouputStream != null) {
					ouputStream.close();
				}
			}
		}

	}

	@Override
	public JSONObject audit(long id, int val, String examinationOpinions) {
		JSONObject jsonMap = new JSONObject();
		try {
			// 防止并发
			 AcqInMerchant aim = acqInMerchantDao.queryStatusByAcqId(id);
			if (aim.getAuditStatus() != 1) {
				jsonMap.put("result", false);
				jsonMap.put("msg", "该详情已经审核过");
				return jsonMap;
			}
			
			Integer temp = null;
			
			if(val == 1  || val == 3 ){
				temp = 2;
			}else{
				temp = 3;
			}
			
			
			int num = acqInMerchantDao.updateStatusByAcqId(id, temp);
			if (num == 1) {
				final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				// 添加审核记录
				AcqInMerchantRecord aq = new AcqInMerchantRecord(id, temp, examinationOpinions,
						principal.getId().toString(), new Date());
				acqInMerchantDao.insert(aq);
				jsonMap.put("result", true);
				jsonMap.put("msg", "审核完成");
				
				if (val==3 || val == 4 ) {
					List<Integer> nextIds = acqInMerchantDao.queryNextAcqId(id);
					if(nextIds.size()==0){
						jsonMap.put("nextId", -1);
						jsonMap.put("msg", "暂无可审核数据");

					}else{
						jsonMap.put("nextId",nextIds.get((int) (Math.random() * nextIds.size())));
					}
				}
				
				//调用推送接口
				if (val==2 || val ==4 ) {
					  //极光推送
					String content = "您有一条收单商户进件审核不通过,请修改后重新提交";
					Map<String, Object> map = new HashMap<>();
					map.put("acqIntoNo", aim.getAcqIntoNo());
					map.put("type", "defeated");
					JSONObject jsonObject = new JSONObject(map);
		            String authCode = Md5.md5Str(USER_INFO_ID + aim.getAgentNo() + Token).toUpperCase();
		            String paramStr = "token=" + Token + "&merchantNo=" + aim.getAgentNo() + "&authCode=" + authCode  +"&content="+URLEncoder.encode(content,"UTF-8")+"&ext="+URLEncoder.encode(jsonObject.toString(),"UTF-8");
		            String url = sysDictService.getByKey("CORE_URL").getSysValue() + PATH;
		            String str = HttpUtils.sendPost(url, paramStr, "UTF-8");
		            log.info("极光推送结果：" + str);
				}
				
			} else {
				jsonMap.put("result", false);
				jsonMap.put("msg", "审核失败");
			}
		} catch (Exception e) {
			jsonMap.put("result", false);
			jsonMap.put("msg", "审核异常");

		}

		return jsonMap;
	}
	
	public List<AcqInMerchantFileInfo> findByAcqIntoNo(String acqIntoNo) {
		return acqInMerchantDao.findByAcqIntoNo(acqIntoNo);
	}

	@Override
	public AcqInMerchant queryStatusByAcqId(long id) {
		return acqInMerchantDao.queryStatusByAcqId(id);
	}

}

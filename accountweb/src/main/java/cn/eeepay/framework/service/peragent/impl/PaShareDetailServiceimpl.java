package cn.eeepay.framework.service.peragent.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.peragent.PaShareDetailMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.peragent.PaBrand;
import cn.eeepay.framework.model.peragent.PaShareDetail;
import cn.eeepay.framework.service.peragent.PaShareDetailService;
import cn.eeepay.framework.util.StringUtil;
@Service("paShareDetailService")
@Transactional("peragent")
public class PaShareDetailServiceimpl implements PaShareDetailService{
	@Resource
	private PaShareDetailMapper paShareDetailMapper;

	@Override
	public List<PaShareDetail>  findPeragentList(PaShareDetail paShareDetail, Sort sort, Page<PaShareDetail> page) {
		
		if (StringUtils.isNotBlank(paShareDetail.getCreateTime1())) {
			paShareDetail.setCreateTime1(paShareDetail.getCreateTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(paShareDetail.getCreateTime2())) {
			paShareDetail.setCreateTime2(paShareDetail.getCreateTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(paShareDetail.getAccTime1())) {
			paShareDetail.setAccTime1(paShareDetail.getAccTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(paShareDetail.getAccTime2())) {
			paShareDetail.setAccTime2(paShareDetail.getAccTime2() + " 23:59:59");
		}
		List <PaShareDetail> peragentList = new ArrayList<>();
		peragentList = paShareDetailMapper.findPeragentList(paShareDetail, sort, page);
		for (PaShareDetail paShareDetail2 : peragentList) {
			if("null".equals(paShareDetail2.getTwoUserCode()) || StringUtil.isBlank(paShareDetail2.getTwoUserCode())  ){
				paShareDetail2.setTwoUserCode("");
			}
			if(paShareDetail2.getTwoUserCode().equals(paShareDetail2.getUserCode())){
				paShareDetail2.setTwoUserCode("");
			}
			if(paShareDetail2.getOneUserCode().equals(paShareDetail2.getUserCode())){
				paShareDetail2.setOneUserCode("");
			}
		    if (paShareDetail2.getShareLevel() != null) {
		          paShareDetail2.setShareLevelStr("Lv." + paShareDetail2.getShareLevel());
		    }
			if (paShareDetail2.getShareRatio() != null) {
			        paShareDetail2.setShareRatioStr("万分之" + paShareDetail2.getShareRatio());
			}
			if ("2".equals(paShareDetail2.getShareType()) || "1".equals(paShareDetail2.getShareType()) || "7".equals(paShareDetail2.getShareType())) {
			     paShareDetail2.setTransAmountStr(paShareDetail2.getTransAmount() == null ? "0.00" : paShareDetail2.getTransAmount().toString());
			     if("7".equals(paShareDetail2.getShareType())){
					 paShareDetail2.setTransAmountStr("");
				 }
			} else {
		        paShareDetail2.setTransAmount(null);
		        paShareDetail2.setTransNo("");   
			}
			if("6".equals(paShareDetail2.getShareType())){
				if (paShareDetail2.getShareRatio() != null) {
					paShareDetail2.setHonourShareRatioStr("万分之" + paShareDetail2.getShareRatio());
				}
				paShareDetail2.setShareRatioStr("");
			}
			
		}
		return peragentList;
	}

	@Override
	public List<PaBrand> findPaBrandList() {
		return paShareDetailMapper.findPaBrandList();
	}

	@Override
	public Map<String, Object> findPeragentListCollection(PaShareDetail paShareDetail) {
		if (StringUtils.isNotBlank(paShareDetail.getCreateTime1())) {
			paShareDetail.setCreateTime1(paShareDetail.getCreateTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(paShareDetail.getCreateTime2())) {
			paShareDetail.setCreateTime2(paShareDetail.getCreateTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(paShareDetail.getAccTime1())) {
			paShareDetail.setAccTime1(paShareDetail.getAccTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(paShareDetail.getAccTime2())) {
			paShareDetail.setAccTime2(paShareDetail.getAccTime2() + " 23:59:59");
		}
		return paShareDetailMapper.findPeragentListCollection(paShareDetail);
	}

	@Override
	public List<PaShareDetail> peragentExport(PaShareDetail paShareDetail) {
		if (StringUtils.isNotBlank(paShareDetail.getCreateTime1())) {
			paShareDetail.setCreateTime1(paShareDetail.getCreateTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(paShareDetail.getCreateTime2())) {
			paShareDetail.setCreateTime2(paShareDetail.getCreateTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(paShareDetail.getAccTime1())) {
			paShareDetail.setAccTime1(paShareDetail.getAccTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(paShareDetail.getAccTime2())) {
			paShareDetail.setAccTime2(paShareDetail.getAccTime2() + " 23:59:59");
		}
		List <PaShareDetail> peragentList = new ArrayList<>();
		peragentList = paShareDetailMapper.peragentExport(paShareDetail);
		for (PaShareDetail paShareDetail2 : peragentList) {
			if("null".equals(paShareDetail2.getTwoUserCode()) || StringUtil.isBlank(paShareDetail2.getTwoUserCode())  ){
				paShareDetail2.setTwoUserCode("");
			}
			if(paShareDetail2.getTwoUserCode().equals(paShareDetail2.getUserCode())){
				paShareDetail2.setTwoUserCode("");
			}
			if(paShareDetail2.getOneUserCode().equals(paShareDetail2.getUserCode())){
				paShareDetail2.setOneUserCode("");
			}
		    if (paShareDetail2.getShareLevel() != null) {
		          paShareDetail2.setShareLevelStr("Lv." + paShareDetail2.getShareLevel());
		    }
			if (paShareDetail2.getShareRatio() != null) {
		        paShareDetail2.setShareRatioStr("万分之" + paShareDetail2.getShareRatio());
		    }
			if ("2".equals(paShareDetail2.getShareType()) || "1".equals(paShareDetail2.getShareType()) || "7".equals(paShareDetail2.getShareType())) {
			     paShareDetail2.setTransAmountStr(paShareDetail2.getTransAmount() == null ? "0.00" : paShareDetail2.getTransAmount().toString());
			     if("7".equals(paShareDetail2.getShareType())){
					 paShareDetail2.setTransAmountStr("");
				 }
			} else {
		        paShareDetail2.setTransAmount(null);
		        paShareDetail2.setTransNo("");   
			}	
			if("6".equals(paShareDetail2.getShareType())){
				if (paShareDetail2.getShareRatio() != null) {
					paShareDetail2.setHonourShareRatioStr("万分之" + paShareDetail2.getShareRatio());
				}
				paShareDetail2.setShareRatioStr("");
			}
		}
		return peragentList;
	}

	@Override
	public PaBrand findByBrandCode(String brandCode) {
		return paShareDetailMapper.findByBrandCode(brandCode);
	}

	@Override
	public PaShareDetail findPaShareDetailById(Integer id) {
		return paShareDetailMapper.findPaShareDetailById(id);
	}

	@Override
	public Map<String, Object> comfirmBacthAccount(String accountMonth, String username) {
		return paShareDetailMapper.comfirmBacthAccount(accountMonth);
	}
}

package cn.eeepay.framework.service;


import java.util.List;

import cn.eeepay.framework.model.IndustryMcc;
import cn.eeepay.framework.model.MerchantRequireItem;

public interface MerchantRequireItemService {

    public MerchantRequireItem selectByMriId(String mriId, String merId);

    public int updateBymriId(Long id, String status);

    public int updateByMbpId(MerchantRequireItem record);

    MerchantRequireItem selectByAccountNo(String merId);

    public List<MerchantRequireItem> getByMer(String merchantNo);

    public List<MerchantRequireItem> getItemByMerId(String merchantNo);

    public List<MerchantRequireItem> selectItemByBpIdAndMerNo(String merchantNo,String bpId);

    public IndustryMcc selectIndustryMccByMcc(String mcc);

    public List<IndustryMcc> selectIndustryMccByLevel(String industryLevel);

    public List<IndustryMcc> selectIndustryMccByParentId(String parentId);

    public int updateMccById(String id,String mcc);

    public int updateMccByMerNo(String merNo,String mcc);
}

package cn.eeepay.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ZQMerchantInfo;
import cn.eeepay.framework.model.ZQTransOrderInfo;
import cn.eeepay.framework.util.ReadOnlyDataSource;

/**
 * Created by Administrator on 2017/5/22.
 */
@ReadOnlyDataSource
public interface ZQMerchantDao {

    /**
     * 查询直清商户
     * @param zqMerchantInfo    查询条件
     * @param saleName          那个直清机构,如ZF_ZQ
     * @param page              分页信息
     * @return  查询结果
     */
    List<ZQMerchantInfo> queryZQMerchantInfo(@Param("info") ZQMerchantInfo zqMerchantInfo, @Param("saleName") String saleName, Page<ZQMerchantInfo> page);

    /**
     * 查询直清商户
     * @param zqMerchantInfo    查询条件
     * @param saleName          那个直清机构,如ZF_ZQ
     * @return  查询结果
     */
    List<ZQMerchantInfo> queryZQMerchantInfo(@Param("info") ZQMerchantInfo zqMerchantInfo, @Param("saleName")String saleName);
    /**
     * 查询直清商户交易信息
     * @param zqTransOrderInfo    查询条件
     * @param saleName          那个直清机构,如ZF_ZQ
     * @param page              分页信息
     * @return  查询结果
     */
    List<ZQTransOrderInfo> queryZQTransOrder(@Param("info")ZQTransOrderInfo zqTransOrderInfo,
                                             @Param("saleName")String saleName,
                                             @Param("zqAcqId") long zqAcqId,
                                             Page<ZQTransOrderInfo> page);

    /**
     * 查询直清商户交易信息
     * @param zqTransOrderInfo    查询条件
     * @param saleName          那个直清机构,如ZF_ZQ
     * @return  查询结果
     */
    List<ZQTransOrderInfo> queryZQTransOrder(@Param("info")ZQTransOrderInfo zqTransOrderInfo,
                                             @Param("saleName") String saleName,
                                             @Param("zqAcqId") long zqAcqId);

    /**
     * 获取直清的收单id
     * @param saleName 直清名称
     * @return 收单id
     */
    Long getZQAcqId(@Param("saleName")String saleName);
    
    /**
     * 获取直清商户
     * @param merNo
     * @param mbpId
     * @param acqEnname
     * @return
     */
    ZQMerchantInfo selectByMerMbpAcq(@Param("merNo")String merNo,@Param("mbpId")String mbpId,@Param("acqEnname")String acqEnname);
}

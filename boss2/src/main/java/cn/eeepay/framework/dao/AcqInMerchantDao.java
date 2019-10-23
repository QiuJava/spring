package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqInMerchant;
import cn.eeepay.framework.model.AcqInMerchantFileInfo;
import cn.eeepay.framework.model.AcqInMerchantRecord;
import cn.eeepay.framework.model.AcqMerchant;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

public interface AcqInMerchantDao {
	
	//根据收单机构商户编号判断是否存在
	@Select("SELECT COUNT(1) FROM acq_merchant where acq_merchant_no=#{acqMerchantNo}")
	int selectOrgMerExistByMerNo(@Param("acqMerchantNo")String acqMerchantNo);
	
	@Select("SELECT * FROM acq_merchant_info where id=#{acqId}")
	@ResultType(AcqInMerchant.class)
	AcqInMerchant queryStatusByAcqId(@Param("acqId")long id);
	
	@Select("SELECT amfi.*,ari.item_name FROM acq_merchant_file_info amfi LEFT JOIN add_require_item ari ON ari.item_id=amfi.file_type where acq_into_no=#{acqIntoNo} AND status=1")
	@ResultType(AcqInMerchantFileInfo.class)
	List<AcqInMerchantFileInfo> findByAcqIntoNo(@Param("acqIntoNo")String acqIntoNo);
	
	
	@Select("SELECT mil.*,bsu.real_name FROM acq_merchant_info_log mil LEFT JOIN boss_shiro_user bsu on mil.operator=bsu.id  where mil.acq_merchant_info_id=#{id}  order by mil.create_time desc")
	@ResultType(AcqInMerchantRecord.class)
	List<AcqInMerchantRecord> selectRecordByPrimaryKey(@Param("id")Long id);
	
	@Select("SELECT id FROM acq_merchant_info where audit_status =1 and id!=#{id}")
	@ResultType(Long.class)
	List<Integer> queryNextAcqId(long id);
	
    @Insert("insert into acq_merchant_info_log (acq_merchant_info_id,audit_status,examination_opinions,operator,create_time) "
    		+ "values(#{record.acqMerchantInfoId},#{record.auditStatus},#{record.examinationOpinions},#{record.operator},#{record.createTime})")
    int insert(@Param("record")AcqInMerchantRecord aq);
    
    @Select("SELECT ami.*,ai.agent_name as agentName,ai1.agent_name as oneAgentName "
    		+ "from acq_merchant_info ami LEFT JOIN agent_info ai on ai.agent_no=ami.agent_no LEFT JOIN agent_info ai1 on ai1.agent_no=ami.one_agent_no "
    		+ "where ami.id=#{id}")
    @ResultType(AcqInMerchant.class)
    AcqInMerchant selectByPrimaryKey(@Param("id")Long id);
    
    @Update("update acq_merchant set acq_org_id=#{record.acqOrgId},acq_service_id=#{record.acqServiceId},"
    		+ "acq_merchant_name=#{record.acqMerchantName},"
    		+ "agent_no=#{record.agentNo},mcc=#{record.mcc},merchant_no=#{record.merchantNo},acq_merchant_type=#{record.acqMerchantType},acq_merchant_code=#{record.acqMerchantCode}"
    		+ " where id=#{record.id}")
    int updateByPrimaryKey(@Param("record")AcqMerchant record);
    
    @Update("update acq_merchant set quota=#{quota},day_quota=#{dayQuota} where acq_merchant_no=#{acqMerchantNo}")
    int updateAcqMerchantQuota(AcqMerchant recode);
    
    @Update("update acq_merchant set acq_status=#{record.acqStatus} where id=#{record.id}")
    int updateStatusByid(@Param("record")AcqMerchant record);
    
    @Select("select * from acq_merchant where acq_merchant_no=#{record.acqMerchantNo}")
    @ResultType(AcqMerchant.class)
    AcqMerchant selectInfoByAcqmerNo(@Param("record")AcqMerchant record);
    
    @SelectProvider(type=SqlProvider.class,method="selectAllInfo")
    @ResultType(AcqInMerchant.class)
    List<AcqInMerchant> selectAllInfo(@Param("page")Page<AcqInMerchant> page,@Param("ami")AcqInMerchant ami);
    
    @Update("update acq_merchant set day_quota=quota")
   	int updateBatchAcqMerchantQuota();
    
    
    @SelectProvider(type=SqlProvider.class,method="selectAllInfo")
    @ResultType(AcqInMerchant.class)
	List<AcqInMerchant> exportInfo(@Param("ami")AcqInMerchant am);
    
    
    public class SqlProvider{
    	
    	public String selectAllInfo(Map<String,Object> param){
    		final AcqInMerchant ami=(AcqInMerchant)param.get("ami");
    		return new SQL(){{
    			SELECT("ami.*,ai.agent_name as agentName,ai1.agent_name as oneAgentName");
    			FROM("acq_merchant_info ami LEFT JOIN agent_info ai on ai.agent_no=ami.agent_no LEFT JOIN"
    					+ " agent_info ai1 on ai1.agent_no=ami.one_agent_no ");
    			if(StringUtils.isNotBlank(ami.getAcqIntoNo())){
    				WHERE("ami.acq_into_no =#{ami.acqIntoNo}");
    			}
    			if(StringUtils.isNotBlank(ami.getMerchantName())){
    				ami.setMerchantName("%"+ami.getMerchantName()+"%");
    				WHERE("ami.merchant_name LIKE #{ami.merchantName} ");
    			}
    			if(StringUtils.isNotBlank(ami.getLegalPerson())){
    				WHERE("ami.legal_person = #{ami.legalPerson} ");
    			}
    			
    			if(StringUtils.isNotBlank(ami.getIntoSource())){
    				WHERE("ami.into_source = #{ami.intoSource} ");
    			}
    			if(ami.getAuditStatus()!=null && ami.getAuditStatus()!=-1){
    				WHERE("ami.audit_status = #{ami.auditStatus} ");
    			}
    			if(ami.getMerchantType()!= null && ami.getMerchantType()!=-1){
    				WHERE("ami.merchant_type = #{ami.merchantType} ");
    			}
    			
    			if(StringUtils.isNotBlank(ami.getAgentNo()) && !ami.getAgentNo().equals("-1")){
    				WHERE("ami.agent_no = #{ami.agentNo} ");
    			}
    			
    			if(StringUtils.isNotBlank(ami.getOneAgentNo()) && !ami.getOneAgentNo().equals("-1")){
    				WHERE("ami.one_agent_no = #{ami.oneAgentNo} ");
    			}
    			
    			//审核时间
    			if(ami.getAuditSTime()!=null){
					WHERE(" ami.audit_time>=#{ami.auditSTime}");
				}
    			if(ami.getAuditETime()!=null){
    				WHERE(" ami.audit_time<=#{ami.auditETime}");
    			}
    			//进件时间
    			if(ami.getCreateSTime()!=null){
					WHERE(" ami.create_time>=#{ami.createSTime}");
				}
    			if(ami.getCreateETime()!=null){
    				WHERE(" ami.create_time<=#{ami.createETime}");
    			}
    			
    			ORDER_BY("ami.create_time desc");
    		}}.toString();
    	}
    	
    }
    
    @Update("update acq_merchant_info set mcc=#{mcc} where id=#{id}")
    int updateMcc(@Param("id")String id, @Param("mcc")String mcc);

    @Update("update acq_merchant_info set audit_status=#{val},audit_time=now()  where id=#{id}")
	int updateStatusByAcqId(@Param("id")long id, @Param("val")int val);

}

package cn.eeepay.framework.dao.bill;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BusinessAccount;
import cn.eeepay.framework.model.bill.BusinessAccountDetail;

public interface BusinessAccountDetailMapper {
	/**
	 * 调账详情
	 * @param adjustDetail
	 * @return
	 */
	@InsertProvider(type=SqlProvider.class,method="insertBatchBusinessAccountDetail")
	int insertBatchBusinessAccountDetail(@Param("list")List<BusinessAccountDetail> list);
	
	@Update("update business_account_detail set record_status=#{detail.recordStatus},record_result=#{detail.recordResult} where id=#{detail.id}")
	int updateRecordResult(@Param("detail")BusinessAccountDetail detail);
	
	/**
	 * 删除调账ID所有明细
	 * @param adjustAccount
	 * @return
	 */
	@Delete("delete from business_account_detail where business_id = #{adjustAccount.id}")
	int deleteBusinessDetail(@Param("adjustAccount")BusinessAccount adjustAccount);
	
	@SelectProvider(type=SqlProvider.class, method="findBusinessAccountDetail")
	@ResultMap("cn.eeepay.framework.dao.bill.BusinessAccountDetailMapper.BaseResultMap")
	List<BusinessAccountDetail> findBusinessAccountDetail(@Param("adjustDetail")BusinessAccountDetail adjustDetail,@Param("params")Map<String, String> params, @Param("sort")Sort sort, Page<BusinessAccountDetail> page);
	
	
	@Select("select * from business_account_detail where business_id = #{adjustAccount.id}")
	@ResultMap("cn.eeepay.framework.dao.bill.BusinessAccountDetailMapper.BaseResultMap")
	List<BusinessAccountDetail> findBusinessDetailByBusinessId(@Param("adjustAccount")BusinessAccount adjustAccount);
	
	public class SqlProvider {
		public String insertBatchBusinessAccountDetail(Map<String, List<BusinessAccountDetail>> map) {
            List<BusinessAccountDetail> list = map.get("list");
            StringBuilder stringBuilder = new StringBuilder(256);
            stringBuilder.append("insert into business_account_detail(business_id,trans_no,out_user_no,in_user_no,amount,reason,account_type) values");
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].businessId},#'{'list[{0}].transNo},"
            		+ "#'{'list[{0}].outUserNo},#'{'list[{0}].inUserNo},#'{'list[{0}].amount},#'{'list[{0}].reason},#'{'list[{0}].accountType})");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(messageFormat.format(new Integer[]{i}));
                stringBuilder.append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
		}
		
		public String findBusinessAccountDetail(final Map<String, Object> parameter) {
			final BusinessAccountDetail adjustDetail = (BusinessAccountDetail) parameter.get("adjustDetail");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("*");
				FROM("business_account_detail");
				if (adjustDetail.getBusinessId() != null)
					WHERE(" business_id = #{adjustDetail.businessId} ");
				
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(property(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" trans_no desc") ;
				}
			}}.toString();
		}
		
		public String property(String name,int type){
			final String[] propertys={"id","applicantTime","approveTime","transNo","journalNo","childTransNo","amount","accountFlag","remark"};
		    final String[] columns={"id","applicantTime","approveTime","trans_no","journal_no","child_trans_no","amount","account_flag","remark"};
		    if(StringUtils.isNotBlank(name)){
		    	if(type==0){//属性查出字段名
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(propertys[i])){
		    				return columns[i];
		    			}
		    		}
		    	}else if(type==1){//字段名查出属性
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(columns[i])){
		    				return propertys[i];
		    			}
		    		}
		    	}
		    }
			return null;
		}
	}
}

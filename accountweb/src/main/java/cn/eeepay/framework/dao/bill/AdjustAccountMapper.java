package cn.eeepay.framework.dao.bill;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AdjustAccount;
import cn.eeepay.framework.model.bill.AdjustDetail;

/**
 * 调账管理相关Dao
 * @author Administrator
 *
 */
public interface AdjustAccountMapper {
	
	/**
	 * 调账表(这里还没有提交，所以提交时间这个时候补处理)
	 * @param adjustAccount
	 * @return
	 */
	@Insert("insert into adjust_account(applicant,applicant_time,approver,approve_time,status,approve_remark,remark,account_type,file_path)"
			+ " values(#{adjustAccount.applicant},#{adjustAccount.applicantTime},#{adjustAccount.approver},#{adjustAccount.approveTime},#{adjustAccount.status},"
			+ "#{adjustAccount.approveRemark},#{adjustAccount.remark},#{adjustAccount.accountType},#{adjustAccount.filePath})")			
	@SelectKey(statement=" SELECT LAST_INSERT_ID() AS id", keyProperty="adjustAccount.id", before=false, resultType=int.class)  
	int insertAdjustAccount(@Param("adjustAccount")AdjustAccount adjustAccount);
	
	
	@Update("update adjust_account set approver = #{adjustAccount.approver},account_type= #{adjustAccount.accountType},remark= #{adjustAccount.remark},file_path= #{adjustAccount.filePath} where id = #{adjustAccount.id}")
	int updateAdjustAccount(@Param("adjustAccount")AdjustAccount adjustAccount);
	
	
	/**
	 * 删除调账ID所有明细
	 * @param adjustAccount
	 * @return
	 */
	@Delete("delete from adjust_detail where adjust_id = #{adjustAccount.id}")
	int deleteAdjustDetail(@Param("adjustAccount")AdjustAccount adjustAccount);
	
	/**
	 * 调账详情
	 * @param adjustDetail
	 * @return
	 */
	@InsertProvider(type=SqlProvider.class,method="insertBatchAdjustDetail")
	int insertBatchAdjustDetail(@Param("list")List<AdjustDetail> list);
 
   

	/**
	 * 更新审批状态及意见，记录提交时间
	 * @param adjustAccount
	 * @return
	 */
	@Update("update adjust_account set approve_time = now(),status= #{adjustAccount.status},approve_remark= #{adjustAccount.approveRemark} where id = #{adjustAccount.id}")
	int updateadjustExamine(@Param("adjustAccount")AdjustAccount adjustAccount);
	
	/**
	 * (只更新调账状态为  2  待审批,及提交时间 为 当前时间)
	 * @param adjustAccount
	 * @return
	 */
	@Update("update adjust_account set status= #{adjustAccount.status},applicant_time = now() where id = #{adjustAccount.id}")
	int updateadjustExamineDate(@Param("adjustAccount")AdjustAccount adjustAccount);
	
	@Select("select id,applicant,applicant_time,approver,approve_time,status,approve_remark,remark,account_type,file_path,record_fail_remark from adjust_account where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.AdjustAccountMapper.BaseResultMap")
	AdjustAccount getAdjustAccount(Integer id);

	@SelectProvider(type=SqlProvider.class,method="findAdjustAccountApprove")
	@ResultMap("cn.eeepay.framework.dao.bill.AdjustAccountMapper.BaseResultMap")
	List<AdjustAccount> findAdjustAccountApprove(@Param("adjustAccount")AdjustAccount adjustAccount,@Param("params")Map<String, String> params, @Param("sort")Sort sort, Page<AdjustAccount> page);
	
	@SelectProvider(type=SqlProvider.class,method="findAdjustAccount")
	@ResultMap("cn.eeepay.framework.dao.bill.AdjustAccountMapper.BaseResultMap")
	List<AdjustAccount> findAdjustAccount(@Param("adjustAccount")AdjustAccount adjustAccount,@Param("params")Map<String, String> params, @Param("sort")Sort sort, Page<AdjustAccount> page);
	
	
	@SelectProvider(type=SqlProvider.class,method="findAdjustdetail")
	@ResultMap("cn.eeepay.framework.dao.bill.AdjustDetailMapper.BaseResultMap")
	List<AdjustDetail> findAdjustdetail(@Param("adjustDetail")AdjustDetail adjustDetail,@Param("params")Map<String, String> params, @Param("sort")Sort sort, Page<AdjustDetail> page);
	
	@Select("select * from adjust_detail where adjust_id = #{adjustAccount.id}")
	@ResultMap("cn.eeepay.framework.dao.bill.AdjustDetailMapper.BaseResultMap")
	List<AdjustDetail> findAdjustDetailByAdjustId(@Param("adjustAccount")AdjustAccount adjustAccount);
	
	
	public class SqlProvider{
		 public String insertBatchAdjustDetail(Map<String, List<AdjustDetail>> map) {
	            List<AdjustDetail> list = map.get("list");
	            StringBuilder stringBuilder = new StringBuilder(256);
	            stringBuilder.append("insert into adjust_detail(adjust_id,trans_no,journal_no,child_trans_no,amount_from,account_flag,account,amount,remark,account_type,"
	            		+ "user_id,account_owner,subject_no,currency_no,card_no) values");
	            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].adjustId},#'{'list[{0}].transNo},#'{'list[{0}].journalNo},#'{'list[{0}].childTransNo},#'{'list[{0}].amountFrom},"
	            		+ "#'{'list[{0}].accountFlag},#'{'list[{0}].account},#'{'list[{0}].amount},#'{'list[{0}].remark},#'{'list[{0}].accountType},"
	            		+ "#'{'list[{0}].userId},#'{'list[{0}].accountOwner},#'{'list[{0}].subjectNo},#'{'list[{0}].currencyNo},#'{'list[{0}].cardNo})");
	            for (int i = 0; i < list.size(); i++) {
	                stringBuilder.append(messageFormat.format(new Integer[]{i}));
	                stringBuilder.append(",");
	            }
	            stringBuilder.setLength(stringBuilder.length() - 1);
	            return stringBuilder.toString();
	     }

		public String findAdjustdetail(final Map<String, Object> parameter) {
			final AdjustDetail adjustDetail = (AdjustDetail) parameter.get("adjustDetail");
			@SuppressWarnings("unchecked")
			final Sort sord=(Sort)parameter.get("sort");
	

			return new SQL(){{
				SELECT("ad.adjust_id,"
						+ "ad.trans_no,"
						+ "ad.journal_no,"
						+ "ad.child_trans_no,"
						+ "ad.amount_from,"
						+ "ad.account_flag,"
						+ "ad.account,"
						+ "ad.amount,"
						+ "ad.remark,ad.account_type,ad.user_id,ad.account_owner,ad.subject_no,ad.currency_no,ad.card_no"
						/*+ ",eai.account_type,"
						+ "eai.user_id,"
						+ "eai.account_owner,"
						+ "eai.subject_no,"
						+ "eai.currency_no,"
						+ "eai.card_no"*/
						);
				FROM("adjust_detail ad");
				//LEFT_OUTER_JOIN("ext_account_info eai on ad.account=eai.account_no");
				
				if (!(adjustDetail.getAdjustId()==null))
					WHERE(" adjust_id = #{adjustDetail.adjustId} ");
				
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(property(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" trans_no desc") ;
				}
			}}.toString();
		}
	

		 public String findAdjustAccountApprove(final Map<String, Object> parameter) {
			final AdjustAccount adjustAccount = (AdjustAccount) parameter.get("adjustAccount");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final Sort sord=(Sort)parameter.get("sort");
			
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");
			final String id = params.get("id");
			
			return new SQL(){{
				SELECT("id,applicant, applicant_time, approver,approve_time,status,approve_remark,remark,account_type,file_path,record_fail_remark");
				FROM("adjust_account");
				System.out.println(adjustAccount.getId());
					//WHERE(" ba.account_no like  \"%\"#{insAccount.accountNo}\"%\" ");
				WHERE(" approver = #{adjustAccount.approver} ") ;
				WHERE(" status != 0");
				if (StringUtils.isNotBlank(id))
					WHERE(" id like \"%\"#{params.id}\"%\" ");
				if (!StringUtils.isBlank(adjustAccount.getApplicant()))
					WHERE(" applicant like \"%\"#{adjustAccount.applicant}\"%\" ");
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" applicant_time >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" applicant_time <= #{params.endDate} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" applicant_time desc") ;
				}
			}}.toString();
		}
		
		 public String findAdjustAccount(final Map<String, Object> parameter) {
			final AdjustAccount adjustAccount = (AdjustAccount) parameter.get("adjustAccount");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final Sort sord=(Sort)parameter.get("sort");
			
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");
			final String id = params.get("id");
			
			return new SQL(){{
				SELECT("id,applicant, applicant_time, approver,approve_time,status,approve_remark,remark,account_type,file_path,record_fail_remark");
				FROM("adjust_account");
				System.out.println(adjustAccount.getId());
					//WHERE(" ba.account_no like  \"%\"#{insAccount.accountNo}\"%\" ");
				if (StringUtils.isNotBlank(id))
					WHERE(" id like \"%\"#{params.id}\"%\" ");
				if (!StringUtils.isBlank(adjustAccount.getApplicant()))
					WHERE(" applicant like \"%\"#{adjustAccount.applicant}\"%\" ");
				if ((adjustAccount.getStatus() != null) && adjustAccount.getStatus() != 9999)
					WHERE(" status = #{adjustAccount.status} ");
				if ((adjustAccount.getAccountType() != null) && adjustAccount.getAccountType() != 9999)
					WHERE(" account_type = #{adjustAccount.accountType} ");
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" applicant_time >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" applicant_time <= #{params.endDate} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" applicant_time desc") ;
				}
			}}.toString();
		}
	
	
	
	public String propertyMapping(String name,int type){
		final String[] propertys={"id","status","applicant","applicantTime","approver","approveTime"};
	    final String[] columns={"id","status","applicant","applicant_time","approver","approve_time"};
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
	
	public String property(String name,int type){
		/*final String[] propertys={"batchNo","account","amountFrom","amount","accountFlag","remark"};
	    final String[] columns={"batch_no","account","amount_from","amount","account_flag","remark"};*/
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

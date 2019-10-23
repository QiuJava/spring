package cn.eeepay.framework.dao;
import cn.eeepay.framework.model.BusinessProductHardware;
import cn.eeepay.framework.model.MerchantBusinessProduct;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.MerchantRequireItem;
import cn.eeepay.framework.model.MerchantService;
import cn.eeepay.framework.model.MerchantServiceQuota;
import cn.eeepay.framework.model.MerchantServiceRate;
import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.model.ServiceQuota;
import cn.eeepay.framework.model.ServiceRate;
import cn.eeepay.framework.model.SuperPush;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserEntityInfo;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.util.Constants;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

@WriteReadDataSource
public interface MerchantInfoDao {
	
	@Select(" SELECT * FROM super_push_user WHERE user_id = #{userId} ")
	@ResultType(SuperPush.class)
	SuperPush selectFromSuperPuserUserByUserId(@Param("userId")String userId);

	@Select(" select hp.team_entry_id from hardware_product hp" +
			" left join terminal_info ti on hp.hp_id = ti.type where ti.sn = #{sn}")
	@ResultType(String.class)
	String selectTeamEntryId(@Param("sn") String sn);
	
	@Select("select * from function_manage where function_number=#{functionNumber}")
	public  Map<String,Object>  findFunctionManage(@Param("functionNumber")String functionNumber);
//	@Select("select * from agent_function_manage where agent_no= #{oneAgentNO}  and function_number= #{functionNumber}  and team_id ='"+Constants.TEAM_ID+"' ")
	@Select("select * from agent_function_manage where agent_no= #{oneAgentNO}  and function_number= #{functionNumber}  and team_id = #{teamId}")
	public Map<String,Object>  findActivityIsSwitch(@Param("oneAgentNO")String oneAgentNO,@Param("functionNumber")String functionNumber,@Param("teamId")Integer teamId);
	
	@Insert("insert merchant_info (merchant_no,merchant_name,merchant_type,lawyer,business_type,"
			+ "industry_type,id_card_no,province,city,address,mobilephone,email,operator,agent_no,"
			+ "create_time,status,parent_node,sale_name,creator,mender,last_update_time,remark,"
			+ "one_agent_no,team_id,register_source,recommended_source,team_entry_id)"
			+ " values(#{mer.merchantNo},#{mer.merchantName},#{mer.merchantType},#{mer.lawyer},"
			+ "#{mer.businessType},#{mer.industryType},#{mer.idCardNo},#{mer.province},#{mer.city},"
			+ "#{mer.address},#{mer.mobilephone},#{mer.email},#{mer.operator},#{mer.agentNo},"
			+ "#{mer.createTime},#{mer.status},#{mer.parentNode},#{mer.saleName},#{mer.creator},"
			+ "#{mer.mender},#{mer.lastUpdateTime},#{mer.remark},#{mer.oneAgentNo},#{mer.teamId},3,"
			+ "#{mer.recommendedSource}),#{mer.teamEntryId}")
	int insertMer(@Param("mer")MerchantInfo mer);
	
	@Insert("insert merchant_service_rate(service_id,merchant_no,holidays_mark,card_type,rate_type,single_num_amount,rate,capping,safe_line,efficient_date,disabled_date,ladder1_rate,ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,ladder3_max,ladder4_rate,ladder4_max,useable)"
			+ " values(#{merRate.serviceId},#{merRate.merchantNo},#{merRate.holidaysMark},#{merRate.cardType},#{merRate.rateType},#{merRate.singleNumAmount},#{merRate.rate},#{merRate.capping},#{merRate.safeLine},#{merRate.efficientDate},#{merRate.disabledDate},#{merRate.ladder1Rate},#{merRate.ladder1Max},#{merRate.ladder2Rate},#{merRate.ladder2Max},#{merRate.ladder3Rate},#{merRate.ladder3Max},#{merRate.ladder4Rate},#{merRate.ladder4Max},#{merRate.useable})")
	int insertMerRate(@Param("merRate")MerchantServiceRate merRate);
	
	@Insert("insert merchant_service_quota(service_id,card_type,holidays_mark,merchant_no,single_day_amount,"
			+ "single_count_amount,single_daycard_amount,single_daycard_count,efficient_date,disabled_date,useable,single_min_amount)"
			+" values(#{merquota.serviceId},#{merquota.cardType},#{merquota.holidaysMark},#{merquota.merchantNo}"
			+ ",#{merquota.singleDayAmount},#{merquota.singleCountAmount},#{merquota.singleDaycardAmount},"
			+ "#{merquota.singleDaycardCount},#{merquota.efficientDate},#{merquota.disabledDate},#{merquota.useable},#{merquota.singleMinAmount})")
	int insertMerQuota(@Param("merquota")MerchantServiceQuota merQuota);
	
	@Insert("insert merchant_require_item(merchant_no,mri_id,content,STATUS,audit_time)"
			+"values(#{item.merchantNo},#{item.mriId},#{item.content},#{item.status},#{item.auditTime})")
	int insertMerItem(@Param("merItem")MerchantRequireItem merItem);

	@Insert("insert merchant_service(bp_id,merchant_no,service_id,create_date,disabled_date,status) "
			+"values(#{merService.bpId},#{merService.merchantNo},#{merService.serviceId},#{merService.createDate},#{merService.disabledDate},#{merService.status})")
	int insertMerService(@Param("merService")MerchantService merService);
	
	
	 
	
	@Insert("insert merchant_business_product(merchant_no,bp_id,sale_name,status,examination_opinions,item_source,auditor_id)"
			+"values(#{merProduct.merchantNo},#{merProduct.bpId},#{merProduct.saleName},#{merProduct.status},#{merProduct.examinationOpinions},3,#{merProduct.auditorId})")
	int insertMerProduct(@Param("merProduct")MerchantBusinessProduct merProduct);
	
	@Select("SELECT is_approve FROM agent_info WHERE agent_no=#{oneAgentNO}")
	public  int isApprove(@Param("oneAgentNO")String oneAgentNO);
	
    int deleteByPrimaryKey(Long id);
    int insertSelective(MerchantInfo record);

    @Select("SELECT mis.merchant_no,tis.SN,mis.merchant_name,mis.mobilephone,mis.create_time,"
    		+ "ai.agent_name,mis.`status` from merchant_info mis "
    		+ "LEFT JOIN terminal_info tis on tis.merchant_no=mis.merchant_no "
    		+ "LEFT JOIN agent_info ai on ai.agent_no=mis.agent_no "
    		+ "where mis.id=#{id}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectByPrimaryKey(@Param("id")Long id);

    int updateByPrimaryKeySelective(MerchantInfo record);
    
    //商户进件中的修改
    @Update("update merchant_info set merchant_name=#{record.merchantName},merchant_type=#{record.merchantType}"
    		+ ",business_type=#{record.businessType},mobilephone=#{record.mobilephone},"
    		+ "sale_name=#{record.saleName},remark=#{record.remark},lawyer=#{record.lawyer}"
    		+ "where merchant_no=#{record.merchantNo}")
    int updateByMerId(@Param("record")MerchantInfo record);
    
    @Select("select * from merchant_info where merchant_no=#{merchantNo}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectMn(@Param("merchantNo")String merchantNo);
    
    @Select("select * from merchant_info where mobilephone=#{mobilephone} and team_id=#{teamId}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectMp(@Param("mobilephone")String mobilephone,@Param("teamId")String teamId);
    
    //根据商户ID修改信息审核信息中
    @Update("update merchant_info SET id_card_no=#{record.idCardNo},province=#{record.province}"
    		+ ",city=#{record.city},address=#{record.address} where merchant_no=#{record.merchantNo}")
    int updateByPrimaryKey(@Param("record")MerchantInfo record);
    
    @Select("SELECT mis.id,mis.merchant_no,mis.merchant_name,mis.mobilephone,mis.create_time,"
    		+ "ai.agent_name,mis.`status` from merchant_info mis "
    		+ "LEFT JOIN agent_info ai on ai.agent_no=mis.agent_no")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> selectAllInfo();
    
    @Select("select * from merchant_info")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> selectAllInfoByTermianl();
    
    @Select("select * from merchant_info")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> selectByNameInfoByTermianl();
    
    @Select("select * from merchant_info where merchant_name=#{merName}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectByNameInfo(String merName);

	@Select("select * from merchant_info where merchant_no=#{merchantNo}")
	@ResultType(MerchantInfo.class)
	MerchantInfo selectMerchantById(@Param("merchantNo") String merchantNo);

    @Select("select * from merchant_info where merchant_name=#{merName}")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> selectByNameAllInfo(String merName);
    
    @Select("SELECT * from merchant_info mis "
    		+ "LEFT JOIN terminal_info tis on tis.merchant_no=mis.merchant_no "
    		+ "LEFT JOIN agent_info ai on ai.agent_no=mis.agent_no "
    		+ " where mis.merchant_no=#{mertId}")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> selectByMertId(@Param("mertId")String mertId);
	@Select("SELECT smr.*,si.fixed_rate,si.service_name FROM business_product_info bpi "
			+ "INNER JOIN service_manage_rate smr ON bpi.service_id= smr.service_id "
			+ "INNER JOIN service_info si ON si.service_id = bpi.service_id "
			+ " WHERE bpi.bp_id=#{bp_id} AND smr.agent_no=#{one_agent_no} " +
			"and si.effective_status = 1 ")
	public List<ServiceRate> getServiceRatedByParams(@Param("one_agent_no") String one_agent_no,@Param("bp_id") String bp_id);
	
	@Select("SELECT smq.* ,si.fixed_quota,si.service_name FROM business_product_info bpi "
			+ "INNER JOIN service_manage_quota smq ON bpi.service_id= smq.service_id "
			+ "INNER JOIN service_info si ON si.service_id = bpi.service_id "
			+ " WHERE bpi.bp_id=#{bp_id} AND smq.agent_no=#{one_agent_no} " +
			"and si.effective_status = 1")
	public List<ServiceQuota> getServiceQuotaByParams(@Param("one_agent_no") String one_agent_no,@Param("bp_id") String bp_id);
	
	@Select("select distinct si.service_id,si.* from service_info si"
			+ " inner join business_product_info  bpi on si.service_id = bpi.service_id"
			+ " inner join agent_business_product abp on abp.bp_id = bpi.bp_id"
			+ " where abp.agent_no=#{agent_no} and bpi.bp_id=#{bp_id} " +
			"and si.effective_status = 1"
			)
	public List<ServiceInfo> getServiceInfoByParams(@Param("agent_no") String agent_no,@Param("bp_id") String bp_id);
	
	@Select("select * from sys_dict where sys_value=#{key} and `status`=1")
    @ResultType(SysDict.class)
    SysDict selectSysDictByKey(@Param("key")String key);
	
	@Select("select * from sys_dict where sys_key=#{syskey} and  parent_id=#{parentId} and `status`='1'")
	public List<SysDict> getMerTypeMcc(@Param("syskey") String syskey,@Param("parentId") String parentId);
	
	@Select("select * from sys_dict where parent_id=#{ParentId} and `status`='1'")
	@ResultType(SysDict.class)
    List<SysDict> selectTwoInfoByParentId(@Param("ParentId")String ParentId);
	     
	
	@Select("select * from sys_dict where parent_id=-1 and `status`='1'")
    @ResultType(SysDict.class)
    List<SysDict> selectOneInfo();
	
	@Select("select * from sys_dict where sys_key=#{syskey} and  parent_id !='-1' and `status`='1'")
	public List<SysDict> getMerAllMcc(@Param("syskey") String syskey);

	
	
	/**
	 * 根据手机号查询用户信息
	 * @param mobilephone
	 * @return
	 */
	@Select("SELECT * from user_info where mobilephone = #{mobilephone} and team_id = #{teamID}")
	@ResultType(UserInfo.class)
	public  UserInfo getMobilephone(@Param("mobilephone")String mobilephone,@Param("teamID")String  teamID);
	
	/**
	 * 保存用户信息
	 * @param userInfo
	 * @return
	 */
	@Insert("insert into user_info(team_id,user_id,user_name,mobilephone,status,password,create_time)"
			+"values(#{userInfo.teamId},#{userInfo.userId},#{userInfo.userName},#{userInfo.mobilephone},'1',#{userInfo.password},now())"
			)
	int insertUserInfo(@Param("userInfo")UserInfo userInfo);

	/**
	 * 保存代理商用户实体类信息
	 * @param userEntityInfo
	 * @return
	 */
	@Insert("insert into user_entity_info(user_id,user_type,manage,status,entity_id,apply,last_notice_time)"
			+"values(#{userEntityInfo.userId},#{userEntityInfo.userType},#{userEntityInfo.manage},#{userEntityInfo.status},#{userEntityInfo.entityId},#{userEntityInfo.apply},now())"
			)
	@SelectKey(statement=" SELECT LAST_INSERT_ID() AS id", keyProperty="userEntityInfo.id", before=false, resultType=int.class)  
	int insertAgentUserEntity(@Param("userEntityInfo")UserEntityInfo userEntityInfo);
	
	
	@Select("Select m.* from merchant_info m"
					+ " inner join agent_info a on m.agent_no = a.agent_no"
					+ " inner join team_info t on t.team_id = a.team_id"
					+ " where t.team_id=#{teamId} and m.id_card_no=#{cardNo} ")
	public List<MerchantInfo> queryCardMerInfo(@Param("cardNo")String cardNo,@Param("teamId")String teamId);
	
	@Select("Select m.* from merchant_info m"
			+ " inner join agent_info a on m.agent_no = a.agent_no"
			+ " inner join team_info t on t.team_id = a.team_id"
			+ " where t.team_id=#{teamId} and m.mobilephone=#{mobilephone} ")
	public List<MerchantInfo> queryPhoneMerInfo(@Param("mobilephone")String mobilephone,@Param("teamId")String teamId);	
	/**
	 * 查询硬件种类
	 * @param bpId
	 * @return
	 */
	@Select("select * from business_product_hardware where bp_id=#{bpId}")
	List<BusinessProductHardware>queryHardWare(@Param("bpId")String  bpId);
	
	/**
	 * 修改是否有商户账号状态
	 */
	@Update("update merchant_info set mer_account=#{merCount} where merchant_no=#{merNo}")
    int updateMerCountBymerNo(@Param("merNo")String merNo,@Param("merCount")int merCount);
	
	@Select("select id from risk_roll where roll_no=#{rollNo} and roll_type=#{rollType} and roll_belong=#{rollBelong} and roll_status='1'")
	@ResultType(String.class)
	String findBlacklist(@Param("rollNo")String rollNo,@Param("rollType")String rollType,@Param("rollBelong")String rollBelong);

	/**
	 * 根据商户经营地址归属对应集群
	 */
	@Update("INSERT INTO trans_route_group_merchant (pos_merchant_no,service_type,group_code,create_time,create_person"
			+ ") SELECT mi.merchant_no,si.service_type,IFNULL((SELECT g.group_code FROM trans_route_group g,acq_service ase WHERE"
			+ " g.group_province = mi.province	AND g.group_city IN (mi.city, '不限') AND dg.start_pc = 1 AND dg.acq_org_id = g.acq_id AND g.acq_service_id = ase.id AND ase.service_type = dg.acq_service_type AND g.route_type IN ('1', '2')"
			+ " ORDER BY CASE g.group_city WHEN mi.city THEN 2 WHEN '不限' THEN 1 ELSE 0 END DESC LIMIT 1 ),dg.def_group_code ) group_code,"
			+ " SYSDATE(),'admin' FROM	merchant_info mi,merchant_service ms,service_info si,def_trans_route_group dg"
			+ " WHERE ms.service_id = si.service_id AND ms.merchant_no = mi.merchant_no  AND mi.merchant_no = #{merNo}  "
			+ " AND NOT EXISTS (SELECT 1 FROM trans_route_group_merchant gm WHERE gm.pos_merchant_no = mi.merchant_no"
			+ " AND gm.service_type = si.service_type) AND si.service_id = dg.service_id")
	int updateMerGroupCity(@Param("merNo")String merNo);



	//==============tgh===========
	/**
	 * 根据输入号码和teamId判断
	 * @param mobilephone
	 * @param teamId
	 * @return
	 */
	@Select("	SELECT 	ui.*,  ue_m.user_id  userid_m,ue_m.entity_id entity_id_m FROM user_info ui LEFT JOIN user_entity_info ue_m ON ui.user_id = ue_m.user_id AND ue_m.user_type = '2' and ue_m.apply='2' where  ui.mobilephone = #{mobilephone} AND ui.team_id = #{teamId}")
	Map<String, Object> getMerMobilephone(@Param("mobilephone")String mobilephone, @Param("teamId")String teamId);

	@Insert("insert into user_entity_info(user_id,user_type,entity_id,apply,manage,status)"
			+"values(#{id},2,#{merNo},2,0,1)" )
	int insertMerchantUserEntity(@Param("merNo")String merNo, @Param("id")String id);

	/**
	 * 更新用户名
	 * @param merName
	 * @param userId
	 * @return
	 */
	@Update("update user_info set user_name = #{merName} where user_id = #{userId}")
	int updateUserName(@Param("merName")String merName, @Param("userId")String userId);

	@Update("update user_entity_info set entity_id = #{entity_id} where user_id = #{userId} and user_type='2' and apply='2'")
	int updateEntity(@Param("entity_id")String  entity_id,@Param("userId")String  userId);
	
	

    @Select("select smr.service_id,si.service_name, smr.holidays_mark, smr.card_type, smr.rate_type, smr.single_num_amount, smr.rate, smr.capping,smr.safe_line,smr.ladder1_rate,smr.ladder1_max, smr.ladder2_rate, smr.ladder2_max, smr.ladder3_rate, smr.ladder3_max, smr.ladder4_rate,smr.ladder4_max  from service_manage_rate smr"
    		+ " inner join service_info si on si.service_id = smr.service_id where  smr.agent_no=#{one_agent_no} and smr.service_id=#{serviceId}")
	public List<ServiceRate> getServiceRateByServiceId(@Param("one_agent_no") String one_agent_no,@Param("serviceId") String serviceId);


    @Select(" select smq.* ,si.service_name from service_manage_quota smq inner join service_info si on si.service_id = smq.service_id"
    		+ " where  smq.agent_no=#{one_agent_no} and smq.service_id=#{serviceId}")
	public List<ServiceQuota> getServiceQuotaByServiceId(@Param("one_agent_no") String one_agent_no,@Param("serviceId") String serviceId);

    @Select("SELECT COUNT(1) FROM merchant_info WHERE id_card_no = #{idCardNo} AND team_id = #{teamId}")
    int countMerchantByIdCardAndTeamId(@Param("idCardNo") String idCardNo, @Param("teamId") String teamId);

	@Select("select count(1) from merchant_info where mobilephone=#{mobilePhone} and team_id=#{bpIdTeamId}")
	int countMerchantPhone(@Param("mobilePhone") String mobilePhone, @Param("bpIdTeamId")String bpIdTeamId);

	/**
	 * 判断是不是人人代理的机具
	 * @param entityId
	 * @param sn
	 * @return
	 */
	@Select("select * from pa_ter_info where agent_no = #{entityId} and sn = #{sn}")
	@ResultType(Map.class)
	Map<String, Object> selectFromPaTerInfo(@Param("entityId")String entityId, @Param("sn")String sn);

	/**
	 * 判断是否是超级推机具
	 * @param sn
	 * @return
	 */
	@Select("SELECT count(0) FROM terminal_info ti ,cjt_team_hardware cth " +
			"WHERE ti.type = cth.hp_id AND cth.team_id = #{teamId} and ti.sn = #{sn}")
	@ResultType(Integer.class)
	Integer selectSuperPushTerminal(@Param("teamId")String teamId,@Param("sn") String sn);


	@Select("select count(1) from sensitive_words where #{word} like concat('%',key_word,'%') and status = '1'")
	@ResultType(Integer.class)
	int getByMerchantName(@Param("word")String merchantName);

	@Select("\r\n" +
			"SELECT DISTINCT\r\n" + 
			"	a_i.team_name as appName,\r\n" + 
			"	a_i.team_id as merTeamId\r\n" + 
			"FROM\r\n" + 
			"	app_info a_i\r\n" + 
			"JOIN business_product_define b_p_d ON b_p_d.team_id = a_i.team_id\r\n" + 
			"JOIN agent_business_product a_b_p ON a_b_p.bp_id = b_p_d.bp_id\r\n" + 
			"AND a_b_p.agent_no = #{agentNo};")
	@ResultType(Map.class)
	List<Map<String,Object>> findMerTeamsByAgentNo(String agentNo);
	
	@Select("SELECT DISTINCT\r\n" + 
			"	a_i.team_name,\r\n" + 
			"	a_i.team_id\r\n" + 
			"FROM\r\n" + 
			"	app_info a_i\r\n" + 
			"WHERE\r\n" + 
			"	a_i.team_id = #{merTeamId}")
	Map<String, Object> findMerGroupByTeamId(String merTeamId);

	@Select("SELECT" +
			"        uei.user_id,mi.merchant_no,mi.province $province,mi.city $city,mi.create_time registration_time" +
			"        ,mi.agent_no agent_no,mi.`status` merchant_status,ai.sale_name sales" +
			"        ,mi.one_agent_no first_level_agent_no,mi.recommended_source recmand_source" +
			"        ,mi.id_card_no,mi.team_id orgnize_id,'商户' user_type" +
			"        ,'' vip_level,jd.device_id device_id,mri.content bank_name " +
			"        ,mi.create_time  submit_time, mi.register_source sign_source " +
			"        from user_entity_info uei " +
			"        LEFT JOIN merchant_info mi on uei.user_type = 2 and mi.merchant_no = uei.entity_id" +
			"        LEFT JOIN agent_info ai on ai.agent_no = mi.agent_no " +
			"        LEFT JOIN merchant_require_item mri on mri.merchant_no =   mi.merchant_no and mri.mri_id = '4' " +
			"        LEFT JOIN jpush_device jd on jd.user_no =   mi.merchant_no  " +
			" where mi.merchant_no = #{merchantNo}")
	@ResultType(Map.class)
	Map<String, Object> getUserByMerNo(@Param("merchantNo") String merchantNo);

	@Select("select bpd.bp_name,hp.type_name from terminal_info ti " +
			"LEFT JOIN business_product_define bpd on bpd.bp_id = ti.bp_id  " +
			"LEFT JOIN hardware_product hp on hp.hp_id = ti.type " +
			"where ti.merchant_no = #{merchantNo} ")
	@ResultType(Map.class)
	List<Map<String, Object>> getBpHpByMerNo(@Param("merchantNo") String merchantNo);

	@Select("SELECT recommended_source FROM merchant_info WHERE merchant_no=#{merchantNo}")
	@ResultType(String.class)
	String selectRecommendedSource(@Param("merchantNo") String merchantNo);

	@SelectProvider(type = MerchantInfoDao.SqlProvider.class, method = "batchSelectRecommendedSource")
	@ResultType(Map.class)
    List<Map<String, String>> batchSelectRecommendedSource(@Param("merchantNos") List<String> merchantNos);

	@Select("select * from merchant_info where merchant_no=#{merchantNo}")
	@ResultType(MerchantInfo.class)
	MerchantInfo selectByMerchantNo(@Param("merchantNo") String merchantNo);
	@Select("select parent_node from merchant_info where agent_no=#{agentNo} limit 1")
	@ResultType(String.class)
	String selectParentNodeByAgentNo(@Param("agentNo") String agentNo);
	public class SqlProvider{

		public String batchSelectRecommendedSource(Map<String, Object> param){
			final List<String> list = (List<String>) param.get("merchantNos");
			SQL sql = new SQL(){
				{
					SELECT("merchant_no,recommended_source");
					FROM("merchant_info");
					StringBuffer sb = new StringBuffer();
					sb.append("(");
					for (int i = 0; i < list.size(); i++) {
						sb.append("'" + list.get(i) + "'");
						sb.append(i == list.size() - 1 ? "": ",");
					}
					sb.append(")");
					WHERE("merchant_no in " + sb.toString());
					//ORDER_BY("Field (merchant_no," + sb.toString().substring(1));
				}
			};
			return sql.toString();
		}
	}


}
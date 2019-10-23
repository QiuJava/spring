package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.util.WriteReadDataSource;

@WriteReadDataSource
public interface TerminalInfoDao {

	@SelectProvider(type=SqlProvider.class,method="selectTerActivityCheck")
	@ResultType(TerActivityCheck.class)
	List<TerActivityCheck> selectTerActivityCheck(@Param("page")Page<TerActivityCheck> page,
												  @Param("info")TerActivityCheck terActivityCheck);

	@SelectProvider(type=SqlProvider.class,method="selectTerActivityCheck")
	@ResultType(TerActivityCheck.class)
	List<TerActivityCheck> exportTerActivityCheck(@Param("info")TerActivityCheck terActivityCheck);

	/**
	 * 查询业务产品名称
	 * @param bpId
	 * @return
	 */
	@Select(" select bp_name from business_product_define where bp_id = #{bpId}")
	@ResultType(String.class)
	String selectBpName(@Param("bpId")String bpId);

	@Select(" select activity_type_name from activity_hardware_type where activity_type_no = #{activityTypeNo}")
	@ResultType(String.class)
	String selectActivityName(@Param("activityTypeNo")String activityTypeNo);

	@Update("update terminal_info set open_status=#{openStatus},merchant_no=#{merchantNo},START_TIME=now(),bp_id=#{bpId} where agent_no=#{agentNo} and sn=#{sn}")
	@ResultType(Integer.class)
	Integer updateBundlingBySn(TerminalInfo terminalInfo);
	
	@Select(" select merchant_no,merchant_name from merchant_info where parent_node like concat(#{agentNode},'%')")
	@ResultType(List.class)
	List<Map<String, String>> selectAllMerchantInfo1(@Param("agentNode")String agentNode);
	
	@Select(" select merchant_no,merchant_name from merchant_info where agent_no = #{agentNo}")
	@ResultType(List.class)
	List<Map<String, String>> selectAllMerchantInfo2(@Param("agentNo")String agentNo);

	@Select("select merchant_name from merchant_info where merchant_no=#{merchantNo} and parent_node like concat(#{agentNode},'%')")
	@ResultType(String.class)
	String selectMerchantNameByMerchantNo1(@Param("merchantNo") String merchantNo, @Param("agentNode") String agentNode);

	@Select("select merchant_name from merchant_info where merchant_no=#{merchantNo} and agent_no=#{agentNo}")
	@ResultType(String.class)
	String selectMerchantNameByMerchantNo2(@Param("merchantNo") String merchantNo, @Param("agentNo") String agentNo);
	
	@Select(" select * from terminal_info where merchant_no = #{merchantNo} and SN = PSAM_NO ")
	@ResultType(List.class)
	List<TerminalInfo> selectByMerchantNo(@Param("merchantNo") String merchantNo);
	
	@Select(" select * from terminal_info where id = #{id}")
	@ResultType(TerminalInfo.class)
	TerminalInfo selectById(@Param("id")Long id);
	
	@Select(" SELECT agent_node FROM terminal_info WHERE sn = #{sn}")
	@ResultType(String.class)
	String selectAgentNode(@Param("sn")String sn);

	@Select("SELECT * FROM pa_ter_info WHERE sn = #{sn}")
	@ResultType(Map.class)
	Map<String,Object> selectPaTerInfo(@Param("sn")String sn);
	
	@Select(" select user_code from pa_ter_info where sn = #{sn} limit 1")
	@ResultType(String.class)
	String selectFromPaTerInfoBySn(@Param("sn")String sn);
	
	//=tgh308==
	@Select("SELECT * FROM terminal_info WHERE sn BETWEEN #{snStart1} AND #{snEnd1} ")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> selectTerminalInfoBySn(@Param("snStart1")String snStart1, @Param("snEnd1")String snEnd1);
	
	//228tgh
	@Select("SELECT sys_name,sys_value FROM sys_dict WHERE sys_key = 'ACTIVITY_TYPE'")
	@ResultType(Map.class)
	List<Map<String,String>> selectAllActivityType();

    @Insert("insert into terminal_info(sn,PSAM_NO,open_status,type,CREATE_TIME,cashier_no)"
    		+ "values(#{record.sn},#{record.psamNo},#{record.openStatus},#{record.type},#{record.createTime},#{record.cashierNo})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class) 
    int insert(@Param("record")TerminalInfo record);
    
    @Insert("insert into terminal_info(sn,PSAM_NO,open_status,type,CREATE_TIME,agent_no,agent_node)"
    		+ "values(#{record.sn},#{record.psamNo},#{record.openStatus},#{record.type},#{record.createTime}"
    		+ ",#{record.agentNo},#{record.agentNode})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insertSelective(@Param("record")TerminalInfo record);
    
    
    @Update("update terminal_info set sn=#{record.sn},PSAM_NO=#{record.psamNo},type=#{record.type} where id=#{record.id}")
    int updateByPrimaryKey(@Param("record")TerminalInfo record);
    
    //解绑
  	@Update("update terminal_info set merchant_no = null,open_status = 1,START_TIME = null,bp_id = null where id = #{id}")
  	@ResultType(Integer.class)
  	Integer updateUnbundlingById(@Param("id")Long id);
    
    //解分/分配
    @Update("update terminal_info set agent_no=#{record.agentNo},agent_node=#{record.agentNode},open_status=#{record.openStatus} where id=#{record.id}")
    int solutionById(@Param("record")TerminalInfo record);

	//回收
	@Update("update pa_ter_info set user_code=#{paUserInfo.userCode},agent_no=#{paUserInfo.agentNo},status=1 where id=#{id}")
	int recoverById(@Param("paUserInfo")PaUserInfo paUserInfo,@Param("id")String id);
    
    //绑定
    @Update("update terminal_info set merchant_no=#{record.merchantNo},open_status=#{record.openStatus},START_TIME=#{record.startTime},terminal_id=nextval('terminal_id_seq') where id=#{record.id}")
    int bundlingById(@Param("record")TerminalInfo record);
    
    @Select("select * from acq_terminal_store where sn = #{sn} and acq_enname = #{acqEnname} limit 1")
    @ResultType(Map.class)
    Map<String,Object> selectBySnAcq(@Param("sn") String sn, @Param("acqEnname") String acqEnname);
    
    //进件使用时
    @Update("update terminal_info set merchant_no=#{record.merchantNo},open_status=#{record.openStatus},START_TIME=#{record.startTime},terminal_id=nextval('terminal_id_seq'),bp_id=#{record.bpId} where id=#{record.id}")
    int bundlingItem(@Param("record")TerminalInfo record);
    
    @Update("update pa_ter_info set user_code = #{userCode},status = 2,agent_no = #{agentNo} where sn = #{sn} and user_code = #{entityUserCode} and callback_lock=0")
    int UpdateTerminalInfoBySn(@Param("agentNo")String agentNo, @Param("userCode")String userCode, @Param("sn")String sn, @Param("entityUserCode")String entityUserCode);

	//解绑
	/*@Update("update terminal_info set merchant_no=#{record.merchantNo},open_status=#{record.openStatus},START_TIME=null where id=#{record.id}")
    int unbundlingById(@Param("record")TerminalInfo record);*/
    
	@Update("update pa_ter_info set callback_lock = '0',last_update = now() where sn = #{sn}")
    @ResultType(Integer.class)
    Integer updateLockStatus(@Param("sn")String sn);
	
	@Update("update pa_ter_info set user_code = #{userCode},agent_no = #{agentNo},status = #{status},callback_lock = '0',last_update = now() "
			+ "where sn = #{sn}")	
	@ResultType(Integer.class)
	Integer updateLockStatusToHave(@Param("sn")String sn,@Param("agentNo")String agentNo,@Param("userCode")String userCode,@Param("status")String status);
	
	@Update("update terminal_info set agent_node = #{agentNode},agent_no = #{agentNo} where sn = #{sn}")
	@ResultType(Integer.class)
	Integer updateBelongAgent(@Param("agentNo")String agentNo, @Param("agentNode")String agentNode, @Param("sn")String sn);
	
	@Update("update terminal_info set agent_node = #{agentNode},agent_no = #{agentNo} where merchant_no = #{merchantNo}")
	@ResultType(Integer.class)
	Integer updateByMerchantNo(@Param("agentNo")String agentNo, @Param("agentNode")String agentNode, @Param("merchantNo")String merchantNo);
	
    @Select("select ti.id,ti.sn,ti.terminal_id,ti.merchant_no,ti.START_TIME,ti.open_status,ti.agent_no,mi.merchant_name,"
    		+ "ai.agent_name,pti.status from terminal_info ti "
    		+ "left JOIN merchant_info mi on ti.merchant_no=mi.merchant_no "
    		+ "left JOIN pa_ter_info pti on pti.sn=ti.sn "
    		+ "left JOIN agent_info ai on ti.agent_no=ai.agent_no where ti.open_status!=0")
    List<TerminalInfo> selectAllInfo(@Param("page")Page<TerminalInfo> page);
    
    @Select("select tis.*,bpd.bp_name from terminal_info tis "
    		+ "left JOIN business_product_define bpd on bpd.bp_id=tis.bp_id "
    		+ "where tis.merchant_no=#{merNo} and tis.bp_id=#{bpId}")
    @ResultType(TerminalInfo.class)
    List<TerminalInfo> selectAllInfoBymerNoAndBpId(@Param("merNo")String merNo,@Param("bpId")String bpId);
    
    @Select("select * from terminal_info where "
			+ "sn=#{record.sn} or PSAM_NO=#{record.psamNo}")
	@ResultType(TerminalInfo.class)
	TerminalInfo selectBySameData(@Param("record")TerminalInfo record);



	@Select("select ti.SN,hp.type_name " +
			"from pa_ter_info pti " +
			"LEFT JOIN terminal_info ti on pti.sn=ti.SN " +
			"LEFT JOIN hardware_product hp on hp.hp_id=ti.type " +
			"where pti.order_no=#{orderNo} and ti.agent_node like concat(#{agentNode},'%')")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> selectByOrderNo(@Param("page") Page<TerminalInfo> page,@Param("orderNo")String orderNo, @Param("agentNode")String agentNode);
    
    @Select("select ti.id,ti.sn,ti.terminal_id,ti.CREATE_TIME,ti.merchant_no,ti.PSAM_NO,ti.type,ti.START_TIME,"
    		+ "ti.open_status,ti.agent_no,ato.create_time as downDate,ato1.create_time as receiptDate,mi.merchant_name,ai.agent_name,ai.agent_level,hp.type_name,hp.version_nu "
    		+ "from terminal_info ti "
    		+ "left JOIN merchant_info mi on ti.merchant_no=mi.merchant_no "
    		+ "left JOIN agent_info ai on ti.agent_no=ai.agent_no "
    		+ "left JOIN hardware_product hp on hp.hp_id=ti.type  "
			+ "LEFT JOIN agent_terminal_operate ato ON ti.SN =ato.sn AND ato.oper_type='2' AND ato.agent_no = #{agentNo} "
			+ "LEFT JOIN agent_terminal_operate ato1 ON ti.SN =ato1.sn AND ato1.oper_type='1' AND  ato1.agent_no = #{agentNo} where ti.id=#{id} "
	         )
    @ResultType(TerminalInfo.class)
    TerminalInfo selectObjInfo(@Param("id") Long id,@Param("agentNo")String agentNo);

	//该SQL只走读库 ,已经迁移到 TerminalInfoReadDao 中 liuks
//    @SelectProvider(type=SqlProvider.class,method="selectByParamas")
//    @ResultType(TerminalInfo.class)
//    List<TerminalInfo> selectByParam(@Param("page")Page<TerminalInfo> page,@Param("terminalInfo")TerminalInfo terminalInfo);

	@Select("SELECT pti.user_code FROM terminal_info ti left JOIN merchant_info mi on ti.merchant_no=mi.merchant_no left JOIN pa_ter_info pti on pti.sn=ti.SN left JOIN agent_info ai on ti.agent_no=ai.agent_no WHERE (ai.agent_node LIKE concat(#{agentNode},'%')) GROUP BY pti.user_code")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> selectUserCodeLists(@Param("agentNode")String agentNode);

    @SelectProvider(type=SqlProvider.class,method="selectByAddParam")
    @ResultType(TerminalInfo.class)
    List<TerminalInfo> selectByAddParam(@Param("terminalInfo")TerminalInfo terminalInfo);
    
	
	@Select("select * from terminal_info where agent_no=#{agentNo} and sn=#{sn} and open_status=#{status}  ")
	TerminalInfo checkSn(@Param("agentNo")String  agentNo,@Param("sn")String sn,@Param("status")String status);
	
	@Select("select * from terminal_info where  sn=#{sn}")
	TerminalInfo querySn(@Param("sn")String sn);
	
	@Select("select * from terminal_info where agent_no=#{agentNo} and sn=#{sn}")
	TerminalInfo checkAgentSn(@Param("agentNo")String  agentNo,@Param("sn")String sn);

	@SelectProvider(type=SqlProvider.class,method="selectByUserCode")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> selectByUserCode(@Param("page") Page<PaOrder> page, @Param("userCode") String userCode, @Param("info") TerminalInfo info);

	@Select("SELECT pti.* FROM pa_ter_info pti WHERE pti.callback_lock='0' and pti.user_code = #{userCode} AND (pti.status='1' or pti.status='2') AND pti.sn >= #{snStart} AND pti.sn <= #{snEnd}")
	@ResultType(TerminalInfo.class)
    List<TerminalInfo> queryTerminalByOrder(@Param("userCode") String userCode, @Param("snStart") String snStart, @Param("snEnd") String snEnd);
	
	@Select("select type_name from hardware_product where hp_id = #{type}")
	@ResultType(String.class)
	String selectHardNameByType(@Param("type")String type);

	@Select("SELECT * FROM merchant_info WHERE merchant_no = #{merchantNo}")
	@ResultType(MerchantInfo.class)
	MerchantInfo selectMerchantDetail(@Param("merchantNo")String merchantNo);

	@Select("SELECT hp.* from terminal_info ti " +
			"LEFT JOIN hardware_product hp on hp.hp_id=ti.type " +
			"where ti.type is not null and sn=#{sn}")
	@ResultType(HardwareProduct.class)
	HardwareProduct selectHardwareProductBySn(@Param("sn")String sn);

	// 判断该机具是否为超级推机具
	@Select("SELECT id FROM cjt_team_hardware WHERE team_id=#{teamId} AND hp_id=#{bpId}")
	@ResultType(String.class)
    String selectCjtTerminal(@Param("bpId") String bpId, @Param("teamId") Integer teamId);

	@Select("SELECT * FROM terminal_info WHERE sn=#{sn}")
	@ResultType(TerminalInfo.class)
	TerminalInfo selectBySn(@Param("sn") String sn);

	@Select("SELECT COUNT(id) " +
			"FROM terminal_info " +
			"WHERE activity_type='7' AND activity_type_no=#{activityTypeNo} AND agent_node LIKE CONCAT(#{agentNode}, '%') AND open_status='1'")
    int countAgentNodeAndActivityTypeNo(@Param("agentNode") String agentNode, @Param("activityTypeNo")String activityTypeNo);

	@Select("SELECT team_entry_id from terminal_info ti JOIN hardware_product hp ON ti.type=hp_id AND ti.sn=#{sn} ")
	String getTeamEntryIdBySn(@Param("sn")String sn);


	public class SqlProvider{
		public String selectTerActivityCheck(Map<String,Object> param){
			final TerActivityCheck info = (TerActivityCheck)param.get("info");
			 SQL sql = new SQL(){
				{
					SELECT(" ti.SN sn,ti.id,ti.type,ti.bp_id,ti.agent_no,tac.check_time,tac.due_days,tac.`status`," +
							"ti.open_status,ti.activity_type_no ");
					FROM(" ter_activity_check tac ");
					LEFT_OUTER_JOIN(" terminal_info ti on ti.SN = tac.sn ");
					LEFT_OUTER_JOIN(" business_product_define bpd on bpd.bp_id = ti.bp_id ");
					WHERE(" ti.agent_node like concat(#{info.entityNode},'%')");
					if (StringUtils.isNotBlank(info.getSnStart())) {
						WHERE("ti.sn >= #{info.snStart}");
					}
					if (StringUtils.isNotBlank(info.getSnEnd())) {
						WHERE("ti.sn <= #{info.snEnd}");
					}
					if (StringUtils.isNotBlank(info.getDueDaysMin())) {
						WHERE("tac.due_days >= #{info.dueDaysMin}");
					}
					if (StringUtils.isNotBlank(info.getDueDaysMax())) {
						WHERE("tac.due_days <= #{info.dueDaysMax}");
					}
					if ("1".equals(info.getHasChild())){
//						if(StringUtils.isNotBlank(info.getAgentNodeTwo())){
//							WHERE("ti.agent_node like concat(#{info.agentNodeTwo},'%')");
//						}
						if(StringUtils.isNotBlank(info.getAgentNode())){
							WHERE("ti.agent_node like concat(#{info.agentNode},'%')");
						}
					}else{
						if(StringUtils.isNotBlank(info.getAgentNo())){
							WHERE("ti.agent_no = #{info.agentNo}");
						}
//						if(StringUtils.isNotBlank(info.getAgentNoTwo())){
//							WHERE("ti.agent_no = #{info.agentNoTwo}");
//						}
					}
					if(StringUtils.isNotBlank(info.getType())){
						WHERE("ti.type = #{info.type}");
					}
					if(StringUtils.isNotBlank(info.getBpId())){
						WHERE("ti.bp_id = #{info.bpId}");
					}
					if(StringUtils.isNotBlank(info.getActivityTypeNo())){
						WHERE("ti.activity_type_no = #{info.activityTypeNo}");
					}
					if(StringUtils.isNotBlank(info.getOpenStatus())){
						WHERE("ti.open_status = #{info.openStatus}");
					}
					if(StringUtils.isNotBlank(info.getStatus())){
						WHERE("tac.status = #{info.status}");
					}
					/**
					 * 考核达标状态：①已达标 1：考核天数为正数且激活状态为已激活(成为已达标状态后达标状态不会再变更)
					 * ②考核中 2：考核剩余天数为正数的且激活状态为未激活
					 * ③未达标 0：1.考核剩余天数为负数或0的且激活状态为未激活  2.已激活但剩余考核天数为0或负数，也属于未达标
					 */
					String standardStatus = info.getStandardStatus();
					switch(standardStatus){
					    case "1":
							WHERE(" tac.due_days > 0 and tac.status = 1 ");
					        break;
					    case "2":
							WHERE(" tac.due_days > 0 and tac.status = 0 ");
					        break;
					    case "0":
							WHERE(" (tac.due_days <= 0)");
					        break;
					    default:
					        break;
					}
					if (StringUtils.isNotBlank(info.getCheckTimeStart())) {
						WHERE("tac.check_time >= #{info.checkTimeStart}");
					}
					if (StringUtils.isNotBlank(info.getCheckTimeEnd())) {
						WHERE("tac.check_time <= #{info.checkTimeEnd}");
					}
					if ("ASC".equals(info.getSort())){
						ORDER_BY("tac.due_days ASC");
					}else if("DESC".equals(info.getSort())){
						ORDER_BY("tac.due_days DESC");
					}else {
						ORDER_BY("tac.check_time ASC");
					}
				}
			 };
			 return sql.toString();
		}

		public String selectByUserCode(Map<String,Object> param){
			final TerminalInfo info=(TerminalInfo)param.get("info");
			 SQL sql = new SQL(){
				{
					SELECT("ti.id,ti.sn,concat(hp.type_name,hp.version_nu) as typeName,ti.activity_type");
					FROM("terminal_info ti");
					LEFT_OUTER_JOIN("hardware_product hp ON hp.hp_id=ti.type");
					WHERE("sn IN (SELECT sn FROM pa_ter_info WHERE user_code=#{userCode} and callback_lock=0 and (status=1 or status=2))");
					if (StringUtils.isNotBlank(info.getSnStart())) {
						WHERE("ti.sn>=#{info.snStart}");
					}
					if (StringUtils.isNotBlank(info.getSnEnd())) {
						WHERE("ti.sn<=#{info.snEnd}");
					}
					if(StringUtils.isNotBlank(info.getType())){
						WHERE("ti.type = #{info.type}");
					}
					ORDER_BY("ti.create_time ASC");
				}
			 };
			 return sql.toString();
		}

		

		
		public String selectByAddParam(Map<String,Object> param){
			final TerminalInfo terminalInfo=(TerminalInfo)param.get("terminalInfo");
			return new SQL(){{
				StringBuilder sql=new StringBuilder("");
				if(StringUtils.isNotBlank(terminalInfo.getPsamNo())){
					if(sql.length()!=0){
						sql.append(" or ");
					}
					sql.append("PSAM_NO=#{terminalInfo.psamNo}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getSn())){
					if(sql.length()!=0){
						sql.append(" or ");
					}
					sql.append("sn=#{terminalInfo.sn}");
				}
				
				SELECT(" id,sn,PSAM_NO");
				FROM("terminal_info ");
				if(sql.length()!=0){
					WHERE(sql.toString());
				}
				
			}}.toString();
		}
		
		
		public String selectByParamThree(Map<String,Object> param){
			final TerminalInfo terminalInfo=(TerminalInfo)param.get("terminalInfo");
			final String selectAgentNo=(String)param.get("selectAgentNo");
/*			@SuppressWarnings("unchecked")
			final List<AgentInfo> list=(List<AgentInfo>)param.get("list");*/
			return new SQL(){{
				SELECT(" ti.id,ti.activity_type,ti.sn,ti.terminal_id,ti.merchant_no,ti.START_TIME,ti.open_status,ti.agent_no,mi.merchant_name,"
						+ "ai.agent_name,ti.agent_node,ai.agent_type,ai.agent_level,pti.user_code,pti.status,pti.callback_lock ");
				FROM("terminal_info ti "
						+ "left JOIN merchant_info mi on ti.merchant_no=mi.merchant_no "
						+ "left JOIN pa_ter_info pti on pti.sn=ti.SN "
						+ "left JOIN agent_info ai on ti.agent_no=ai.agent_no "
						
                );
				if(StringUtils.isNotBlank(terminalInfo.getMerchantName())){
					WHERE("(mi.merchant_no = #{terminalInfo.merchantName} or mi.merchant_name = #{terminalInfo.merchantName})");
				}
				
				if(StringUtils.isNotBlank(selectAgentNo)){
    				// 默认包含下级
                    if (StringUtils.isBlank(terminalInfo.getBool()) || StringUtils.equals("1", terminalInfo.getBool())){
                        WHERE(" mi.one_agent_no =#{selectAgentNo}");
                    }else{
                        WHERE(" mi.agent_no = #{selectAgentNo}");
                    }
    			}
				if(StringUtils.isNotBlank(terminalInfo.getSnStart())&&StringUtils.isBlank(terminalInfo.getSnEnd())){
					WHERE("ti.sn like concat(#{terminalInfo.snStart},'%')");
				} else if(StringUtils.isBlank(terminalInfo.getSnStart())&&StringUtils.isNotBlank(terminalInfo.getSnEnd())){
					WHERE("ti.sn like concat(#{terminalInfo.snEnd},'%')");
				} else if(StringUtils.isNotBlank(terminalInfo.getSnStart())&&StringUtils.isNotBlank(terminalInfo.getSnEnd())){
					WHERE("ti.sn >= #{terminalInfo.snStart} and ti.sn <= #{terminalInfo.snEnd}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getType()) &&!StringUtils.equals("-1", terminalInfo.getType())){
					WHERE("ti.type = #{terminalInfo.type}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getOpenStatus())){
					if (!StringUtils.equals("-1", terminalInfo.getOpenStatus())) {
						WHERE("ti.open_status =#{terminalInfo.openStatus}");
					} else {
						WHERE("ti.open_status != 0");
					}
				}
				if(StringUtils.isNotBlank(terminalInfo.getUserCode())){
					WHERE("pti.user_code = #{terminalInfo.userCode}");
				}
                if(StringUtils.isNotBlank(terminalInfo.getRealName())){
                    WHERE("pui.real_name = #{terminalInfo.realName}");
                }

				/*if(StringUtils.isNotBlank(terminalInfo.getBool())){
					if (terminalInfo.getBool().contains("%")) {
						WHERE("ai.agent_node LIKE #{terminalInfo.bool}");
					}else{
						WHERE("ai.agent_node = #{terminalInfo.bool}");
					}
				}*/
				
				//20170116tgh
				if(StringUtils.isNotBlank(terminalInfo.getActivityType())){
					WHERE("FIND_IN_SET(#{terminalInfo.activityType},ti.activity_type)");
				}
				
			
				/*else {
    				// 默认全部
    				
    				 if (StringUtils.isBlank(terminalInfo.getBool()) || StringUtils.equals("1", terminalInfo.getBool())){
    					 StringBuilder sb = new StringBuilder();
    					 sb.append("(");
    					 for (AgentInfo agentInfo : list) {
    						 sb.append("ai.agent_node LIKE CONCAT('"+agentInfo.getAgentNode()+"', '%') or ");
						 }
    					 sb.delete(sb.length()-3, sb.length());
    					 sb.append(") ");
    					 WHERE(sb.toString());
                     }else{
                    	 StringBuilder sb = new StringBuilder();
    					 sb.append("(");
    					 for (AgentInfo agentInfo : list) {
    						 sb.append("ai.agent_node = '"+agentInfo.getAgentNode()+"' or ");
						 }
    					 sb.delete(sb.length()-3, sb.length());
    					 sb.append(") ");
    					 WHERE(sb.toString());
                     }
    			}*/
			}}.toString();
			
			
		}
		public String findActivityType(Map<String,Object> param){
			 final List<TerminalInfo> list = (List<TerminalInfo>) param.get("list");
			 final String agentNo = param.get("agentNo").toString();
			 SQL sql = new SQL(){{
				SELECT("DISTINCT " + 
						"	a_h_t.activity_type_no, " + 
						"	a_h_t.activity_type_name ") ;
				FROM("activity_hardware a_h");
				JOIN("activity_hardware_type a_h_t ON a_h.activity_type_no = a_h_t.activity_type_no");
				JOIN("terminal_info t_i ON t_i.type = a_h.hard_id");
				JOIN("agent_activity a_a ON a_h_t.activity_type_no = a_a.activity_type_no");
				WHERE("a_h_t.update_agent_status = 1");
				WHERE("a_a.agent_no = '" +agentNo+"'");
				WHERE("a_a.status=TRUE");
				StringBuilder sb = new StringBuilder();
				sb.append("t_i.sn IN (");
				for (TerminalInfo terminalInfo : list) {
					sb.append("'"+terminalInfo.getSn()+"'").append(",");
				}
				if (list.size() > 0) {
					sb.deleteCharAt(sb.length()-1);
				}
				sb.append(")");
				WHERE(sb.toString());
				 
			 }};
			 return sql.toString();
		}

		public String findActivityTypeWithSonTeam(Map<String,Object> param){
			final List<TerminalInfo> list = (List<TerminalInfo>) param.get("list");
			final String agentNo = param.get("agentNo").toString();
			final String teamEntryId = param.get("teamEntryId").toString();
			SQL sql = new SQL(){{
				SELECT("DISTINCT " +
						"	a_h_t.activity_type_no, " +
						"	a_h_t.activity_type_name ") ;
				FROM("activity_hardware a_h");
				JOIN("activity_hardware_type a_h_t ON a_h.activity_type_no = a_h_t.activity_type_no");
				JOIN("terminal_info t_i ON t_i.type = a_h.hard_id");
				JOIN("hardware_product hp on hp.hp_id =t_i.type");
				JOIN("agent_activity a_a ON a_h_t.activity_type_no = a_a.activity_type_no");
				WHERE("a_h_t.update_agent_status = 1");
				WHERE("a_a.agent_no = '" +agentNo+"'");
				WHERE("a_a.status=TRUE");
				WHERE("hp.team_entry_id = '"+teamEntryId+"'");

				StringBuilder sb = new StringBuilder();
				sb.append("t_i.sn IN (");
				for (TerminalInfo terminalInfo : list) {
					sb.append("'"+terminalInfo.getSn()+"'").append(",");
				}
				if (list.size() > 0) {
					sb.deleteCharAt(sb.length()-1);
				}
				sb.append(")");
				WHERE(sb.toString());

			}};
			return sql.toString();
		}

	}
	
	 @SelectProvider(type=SqlProvider.class,method="selectByParamThree")
	  @ResultType(TerminalInfo.class)
	List<TerminalInfo> selectByParamThree(@Param("page")Page<TerminalInfo> page, @Param("terminalInfo")TerminalInfo terminalInfo, @Param("selectAgentNo")String selectAgentNo);


	 @SelectProvider(type=SqlProvider.class,method="findActivityType")
	 @ResultType(HardwareAcvitityType.class)
	 List<HardwareAcvitityType> findActivityType(@Param("list")List<TerminalInfo> list,@Param("agentNo") String agentNo);

	@SelectProvider(type=SqlProvider.class,method="findActivityTypeWithSonTeam")
	@ResultType(HardwareAcvitityType.class)
	List<HardwareAcvitityType> findActivityTypeWithSonTeam(@Param("list")List<TerminalInfo> list,@Param("agentNo") String agentNo,@Param("teamEntryId") String teamEntryId);

	@Update("UPDATE terminal_info  " + 
			"SET activity_type_no = #{activityTypeNo}  " + 
			"WHERE " + 
			"	sn = #{sn}")
	int updateActivity(@Param("sn")String sn, @Param("activityTypeNo")String activityTypeNo);

	@Select("SELECT DISTINCT " + 
			"	h_p.org_id  " + 
			"FROM " + 
			"	activity_hardware a_h " + 
			"	JOIN hardware_product h_p ON a_h.hard_id = h_p.hp_id  " + 
			"	AND a_h.activity_type_no = #{activityTypeNo}")
	List<String> findOrgIdByTypeNo(String activityTypeNo);

	@Select(" SELECT DISTINCT h_p.org_id FROM activity_hardware a_h " +
			" JOIN hardware_product h_p ON a_h.hard_id = h_p.hp_id  " +
			" AND a_h.activity_type_no = #{activityTypeNo} AND a_h.team_entry_id=#{teamEntryId} ")
	List<String> findOrgIdByTypeNoWithSonTeam(@Param("activityTypeNo")String activityTypeNo,@Param("teamEntryId")String teamEntryId);


	@Select("SELECT " + 
			"	h_pro.org_id  " + 
			"FROM " + 
			"	terminal_info t_i " + 
			"	JOIN hardware_product h_pro ON t_i.type = h_pro.hp_id  " + 
			"WHERE " + 
			"	sn = #{sn}")
	String findOrgIdBySn(String sn);
	
	@Select("SELECT " + 
			"	count( a_h.id )  " + 
			"FROM " + 
			"	activity_hardware a_h " + 
			"	JOIN terminal_info t_i ON a_h.hard_id = t_i.type  " + 
			"	AND t_i.sn = #{sn}  " + 
			"	AND a_h.activity_type_no = #{activityTypeNo}")
	long countActivityHardwareBySn(@Param("sn")String sn, @Param("activityTypeNo")String activityTypeNo);

	@Select("SELECT " + 
			"	ti.id, " + 
			"	ti.activity_type, " + 
			"	ti.sn, " + 
			"	ats.ter_no terminal_id, " + 
			"	ti.merchant_no, " + 
			"	ti.START_TIME, " + 
			"	ti.open_status, " + 
			"	ti.agent_no, " + 
			"	mi.merchant_name, " + 
			"	ti.type, " + 
			"	ai.agent_name, " + 
			"	ti.agent_node, " + 
			"	ai.agent_type, " + 
			"	ai.agent_level, " + 
			"	pti.user_code, " + 
			"	pti.STATUS, " + 
			"	pti.callback_lock, " + 
			"	a_h_t.update_agent_status AS update_agent_status, " + 
			"	ti.activity_type_no AS activity_type_no  " + 
			"FROM " + 
			"	terminal_info ti " + 
			"	LEFT JOIN merchant_info mi ON ti.merchant_no = mi.merchant_no " + 
			"	LEFT JOIN pa_ter_info pti ON pti.sn = ti.SN " + 
			"	LEFT JOIN agent_info ai ON ti.agent_no = ai.agent_no " + 
			"	LEFT JOIN acq_terminal_store ats ON ats.sn = ti.SN " + 
			//"	LEFT JOIN peragent.pa_user_info pui ON pti.user_code = pui.user_code " + 
			"	LEFT JOIN activity_hardware_type a_h_t ON ti.activity_type_no = a_h_t.activity_type_no  " + 
			"WHERE " + 
			"	ti.sn = #{sn}")
	TerminalInfo findTerminalInfoBySn(String sn);

}
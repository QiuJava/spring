package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.JoinTable;
import org.springframework.stereotype.Service;

@WriteReadDataSource
public interface BusinessProductDefineDao {
	
	/**
	 * 根据业务产品ID查询到teamIdtgh
	 * @param bpId
	 * @return
	 */
	@Select("select team_id from business_product_define where bp_id=#{bpId} ")
	@ResultType(String.class)
	String selectTeamIdByBpId(String bpId);
	
	@Insert("INSERT INTO business_product_define(bp_id,bp_name,sale_starttime,"
			+ "sale_endtime,proxy,bp_type,is_oem,team_id,own_bp_id,two_code,remark"
			+ ",bp_img,not_check) values (#{product.bpId},#{product.bpName},#{product.saleStarttime},"
			+ "#{product.saleEndtime},#{product.proxy},#{product.bpType},#{product.isOem},"
			+ "#{product.teamId},#{product.ownBpId},#{product.twoCode},#{product.remark},"
			+ "#{product.bpImg},#{product.notCheck})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "product.bpId", before = false, resultType = Long.class)
	int insert(@Param("product") BusinessProductDefine product);

	//20170221tghxy
	@Select("select * from business_product_define b " +
			"WHERE b.bp_id IN ( SELECT ap.bp_id FROM agent_business_product ap WHERE ap.agent_no =#{agentNo} AND `status` = 1 ) " +
			"AND b.effective_status = 1 " +
			"order by b.bp_name ")
	@ResultMap("cn.eeepay.framework.dao.BusinessProductDefineDao.BaseResultMap")
	List<BusinessProductDefine> selectAllInfo(@Param("agentNo")String agentNo);

	@Select("select b1.*,b2.bp_name bp_name_2,t.team_name from business_product_define b1 left join business_product_define"
			+ " b2 on b1.own_bp_id=b2.bp_id left join team_info t on b1.team_id=t.team_id where b1.bp_id=#{id}")
	@ResultMap("cn.eeepay.framework.dao.BusinessProductDefineDao.BaseResultMap")
	BusinessProductDefine selectDetailById(@Param("id") String id);

	@SelectProvider(type = SqlProvider.class, method = "selectByParam")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectByParam(Page<BusinessProductDefine> page,@Param("bpd") BusinessProductDefine bpd);

	@Select("SELECT bp_id,bp_name FROM business_product_define WHERE team_id='100010' and bp_id <> #{id}")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> queryOtherProduct(@Param("id") String id);

	@Select("select * from business_product_define where bp_id=#{bpId}")
	@ResultMap("cn.eeepay.framework.dao.BusinessProductDefineDao.BaseResultMap")
	BusinessProductDefine selectBybpId(@Param("bpId") String bpId);

	@Update("update business_product_define set bp_name=#{product.bpName},"
			+ "sale_starttime=#{product.saleStarttime},sale_endtime=#{product.saleEndtime},"
			+ "proxy=#{product.proxy},bp_type=#{product.bpType},is_oem=#{product.isOem}"
			+ ",team_id=#{product.teamId},own_bp_id=#{product.ownBpId},"
			+ "two_code=#{product.twoCode},remark=#{product.remark},bp_img=#{product.bpImg},"
			+ "not_check=#{product.notCheck} where bp_id=#{product.bpId}")
	int update(@Param("product") BusinessProductDefine product);
	
	@Select("select b.bp_id,b.bp_name,t.team_name from business_product_define b,"
			+ "team_info t where b.team_id=t.team_id")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectBpTeam();
	
	@Select("select b.bp_id,b.bp_name,t.team_name from business_product_define b,"
			+ "team_info t where b.team_id=t.team_id and b.bp_Id=#{id}")
	@ResultType(BusinessProductDefine.class)
	BusinessProductDefine selectById(String id);
	
	/**
	 * 多条件查询业务产品列表
	 * @param map
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="getProducesByCondition")
	@ResultType(JoinTable.class)
	List<JoinTable> getProducesByCondition(@Param("param")Map<String, Object> map);
	
	/**
	 * 查询代理商的业务产品
	 */
	@Select("SELECT abp.id id,bpd.bp_id key1,abp.status key2,bpd.agent_show_name key3,abp.default_bp_flag key8,bpd.bp_type key5,"
			+ "bpg.group_no key6,bpd.allow_individual_apply key7,bpd.team_id teamId"
			+ " FROM business_product_define bpd"
			+ " LEFT JOIN agent_business_product abp ON abp.bp_id=bpd.bp_id"
			+ " LEFT JOIN business_product_group bpg ON bpg.bp_id = bpd.bp_id"
			+ " WHERE abp.agent_no=#{agentNo}" +
			" AND bpd.effective_status = 1")
	@ResultType(JoinTable.class)
	List<JoinTable> getAgentProducts(@Param("agentNo")String agentNo);

	@Select("SELECT ti.team_id teamId,ti.team_name teamName " +
			"FROM team_info ti " +
			"WHERE EXISTS( " +
			" SELECT 1 FROM business_product_define bpd " +
			" LEFT JOIN agent_business_product abp ON abp.bp_id=bpd.bp_id " +
			" WHERE abp.agent_no=#{agentNo} " +
			" AND bpd.team_id = ti.team_id " +
			" AND bpd.effective_status = 1 " +
			")")
	@ResultType(JoinTable.class)
	List<JoinTable> selectTeamByAgentAndBp(@Param("agentNo")String agentNo);
	/**
	 * 查询上级代理商的业务产品
	 */
	@Select("SELECT abp.id key1,bpd.bp_id id, bpd.bp_name key3,bpd.bp_type key5 FROM business_product_define bpd"
			+ " LEFT JOIN agent_business_product abp ON abp.bp_id=bpd.bp_id WHERE abp.agent_no=#{agentNo}")
	@ResultType(JoinTable.class)
	List<JoinTable> getParentAgentProducts(@Param("agentNo")String agentNo);
	
	/**
	 * 根据代理商的ID，查询相关的业务产品
	 * @param id
	 * @return
	 */
	@Select("SELECT bpd.bp_id, bpd.bp_name,bpd.remark,team.team_name FROM business_product_define bpd"
			+ " LEFT JOIN agent_business_product abp ON abp.bp_id=bpd.bp_id " +
			"LEFT JOIN team_info team ON bpd.team_id=team.team_id " +
			"WHERE abp.agent_no=#{agentNo} " +
			"AND bpd.effective_status = 1")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> getProductsByAgent(String id);



	public class SqlProvider {
		public String getProducesByCondition(Map<String,Object> map){
			@SuppressWarnings("unchecked")
			final Map<String,Object> params=(Map<String,Object>)map.get("param");
			return new SQL(){{
				SELECT("bpd.bp_id key1, bpd.bp_name key3,bp_type key5");
				FROM("business_product_define bpd");
				LEFT_OUTER_JOIN("team_info ti ON bpd.team_id=ti.team_id");
				if (params.get("teamId")!=null) {
					WHERE("bpd.team_id=#{param.teamId} ");
				}
			}}.toString();
		}

		public String selectByParam(Map<String, Object> param) {
			final BusinessProductDefine bpd = (BusinessProductDefine) param.get("bpd");
			return new SQL() {
				{
					SELECT("bpd.bp_id, bpd.bp_name, bpd.bp_type, bpd.is_oem,ti.team_name");
					FROM("business_product_define bpd LEFT JOIN team_info ti ON bpd.team_id=ti.team_id");
					String bpName = bpd.getBpName();
					if (StringUtils.isNotBlank(bpName)) {
						bpd.setBpName(bpd.getBpName()+"%");
						WHERE("bpd.bp_name like #{bpd.bpName}");
					}
					String bpType = bpd.getBpType();
					if (StringUtils.isNotBlank(bpType)) {
						WHERE("bpd.bp_type=#{bpd.bpType}");
					}
					String isOem = bpd.getIsOem();
					if (StringUtils.isNotBlank(isOem)) {
						WHERE("bpd.is_oem=#{bpd.isOem}");
					}
					String teamId = bpd.getTeamId();
					if (StringUtils.isNotBlank(teamId)) {
						WHERE("ti.team_id = #{bpd.teamId}");
					}
				}
			}.toString();
		}
	}

}
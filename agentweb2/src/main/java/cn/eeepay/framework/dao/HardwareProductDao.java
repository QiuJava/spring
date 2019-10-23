package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.HardwareProduct;

@WriteReadDataSource
public interface HardwareProductDao {

    int deleteByPrimaryKey(Long id);
    
    @Insert("insert into hardware_product (hp_id, type_name,"
      +"model, version_nu, sale_starttime," 
      +"sale_endtime, prod_starttime, prod_endtime"
      +"use_starttime, use_endtime, repa_starttime," 
      +"repa_endtime, oem_mark, oem_id," 
      +"manufacturer)"
      +"values (#{hpId,jdbcType=VARCHAR}, #{typeName,jdbcType=VARCHAR}," 
	      +"#{model,jdbcType=VARCHAR}, #{versionNu,jdbcType=VARCHAR}, #{saleStarttime,jdbcType=DATE}," 
	      +"#{saleEndtime,jdbcType=DATE}, #{prodStarttime,jdbcType=DATE}, #{prodEndtime,jdbcType=DATE}," 
	      +"#{useStarttime,jdbcType=DATE}, #{useEndtime,jdbcType=DATE}, #{repaStarttime,jdbcType=DATE}, "
	      +"#{repaEndtime,jdbcType=DATE}, #{oemMark,jdbcType=VARCHAR}, #{oemId,jdbcType=VARCHAR}, "
	      +"#{manufacturer,jdbcType=VARCHAR})")
    int insert(HardwareProduct record);

    //需求编号2153,并提供了查询sql
    /*@Select("SELECT hp_id, type_name, model, version_nu, sale_starttime, sale_endtime, prod_starttime, "
    		+ "prod_endtime, use_starttime, use_endtime, repa_starttime, repa_endtime, oem_mark, oem_id, "
    		+ "manufacturer FROM hardware_product hp WHERE EXISTS(SELECT 1 FROM agent_business_product ap, "
    		+ " business_product_hardware h WHERE ap.agent_no = #{agentNo} AND ap.bp_id = h.bp_id  AND h.hp_id = hp.hp_id)"
    		+ " and (hp.org_id = #{agnetOem} or hp.org_id is null or hp.org_id = '')")*/
    @Select("select hp_id, type_name, model, version_nu, sale_starttime, sale_endtime, prod_starttime, "
    		+ "prod_endtime, use_starttime, use_endtime, repa_starttime, repa_endtime, oem_mark, oem_id, "
    		+ "manufacturer from hardware_product where org_id in (select bpd.team_id from agent_business_product abp,"
    		+ " business_product_define bpd where abp.bp_id = bpd.bp_id and agent_no = #{agentNo} GROUP BY bpd.team_id)")
    @ResultType(HardwareProduct.class)
    List<HardwareProduct> selectAllInfo(@Param("agentNo")String agentNo,@Param("agnetOem")String agnetOem);

    @Select("select * from ( " +
            "select hp_id, type_name, model, version_nu, sale_starttime, sale_endtime, prod_starttime, device_pn," +
            "prod_endtime, use_starttime, use_endtime, repa_starttime, repa_endtime, oem_mark, oem_id," +
            "manufacturer from hardware_product where org_id in (select bpd.team_id from agent_business_product abp," +
            "business_product_define bpd where abp.bp_id = bpd.bp_id and agent_no = #{agentNo} GROUP BY bpd.team_id)" +
            ") tab where tab.device_pn in (select sys_value from sys_dict where sys_key = 'TER_ACTIVITY_DEVICE_PN');")
    @ResultType(HardwareProduct.class)
    List<HardwareProduct> selectAllInfoByPn(@Param("agentNo")String agentNo,@Param("agnetOem")String agnetOem);

    /**
     * 根据ID，查询出类型、型号
     * by tans
     * @param id
     * @return
     */
    @Select("SELECT hp_id,type_name,version_nu FROM hardware_product WHERE hp_id=#{id}")
    @ResultType(HardwareProduct.class)
	HardwareProduct findHardwareName(@Param("id")String hardWareId);

    @Select("SELECT hp_id,type_name,version_nu FROM hardware_product")
    @ResultType(HardwareProduct.class)
	List<HardwareProduct> findAllHardwareName();

    /**
     * 根据组织ID查询出所有的超级推硬件类型
     * @param teamId
     * @return
     */
    @Select("SELECT hp_id FROM cjt_team_hardware WHERE team_id=#{teamId}")
    @ResultType(String.class)
    List<String> selectSuperPushHardwareProduct(@Param("teamId") Integer teamId);

    @Select("SELECT id FROM cjt_team_hardware WHERE team_id=#{teamId} and hp_id=#{type}")
    @ResultType(String.class)
    String selectSuperPushHardwareProductByTeamIdAndHpId(@Param("teamId") String teamId, @Param("type") String type);

    @Select("SELECT id FROM cjt_team_hardware WHERE hp_id=#{type} limit 1")
    @ResultType(String.class)
    String selectSuperPushHardwareByHpId(@Param("type") String type);
}
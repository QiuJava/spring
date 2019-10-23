package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.TerminalInfo;

@WriteReadDataSource
public interface AddRequireItemDao {

    @Select("SELECT * from add_require_item where item_id=#{meriId}")   
    @ResultType(AddRequireItem.class)
    AddRequireItem selectByMeriId(@Param("meriId")String meriId);
    
    @Select("SELECT item_id,item_name FROM add_require_item WHERE item_name LIKE #{itemName} ORDER BY item_id")
    @ResultType(AddRequireItem.class)
	List<AddRequireItem> selectByName(Page<AddRequireItem> page, @Param("itemName")String itemName);

	@Select("SELECT * FROM add_require_item WHERE item_id=#{id}")
	@ResultType(AddRequireItem.class)
	AddRequireItem selectById(@Param("id")String id);
	
	@Update("UPDATE add_require_item SET item_name=#{item.itemName},example_type=#{item.exampleType},"
			+ "example=#{item.example},photo=#{item.photo},photo_address=#{item.photoAddress},"
			+ "remark=#{item.remark},check_status=#{item.checkStatus},check_msg=#{item.checkMsg} WHERE item_id=#{item.itemId}")
	int update(@Param("item")AddRequireItem item);

	@Insert("INSERT INTO add_require_item(item_id,item_name,example_type,example,photo,photo_address,remark,check_status,check_msg) "
			+ "values (#{item.itemId},#{item.itemName},#{item.exampleType},#{item.example},#{item.photo},#{item.photoAddress},"
			+ "#{item.remark},#{item.checkStatus},#{item.checkMsg})")
	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="item.itemId", before=false, resultType=Long.class)  
	int insert(@Param("item")AddRequireItem item);

	@Select("SELECT item_id,item_name FROM add_require_item WHERE item_id=#{id}")
	@ResultType(AddRequireItem.class)
	AddRequireItem selectRequireName(@Param("id")String id);

	@Select("SELECT item_id,item_name FROM add_require_item")
	@ResultType(AddRequireItem.class)
	List<AddRequireItem> selectAllRequireName();

	@Select("SELECT item_id,item_name FROM add_require_item")
	@ResultType(AddRequireItem.class)
	List<AddRequireItem> queryItemNameList(Page<TerminalInfo> page);
	
	/**
	 * gw
	 * @param agent_no
	 * @param bp_id
	 * @return
	 */
	@Select("select ari.* from add_require_item ari"
			+ " inner join business_require_item bri on bri.br_id = ari.item_id"
			+ " inner join business_product_define bpd on bri.bp_id = bpd.bp_id"
			+ " inner join agent_business_product abp on abp.bp_id = bpd.bp_id"
			+ " inner join agent_info ai on ai.agent_no = abp.agent_no"
			+ " where ai.agent_no=#{agent_no} and abp.bp_id=#{bp_id} and ari.example_type='1' and bpd.allow_web_item='1'"
			)
	@ResultType(AddRequireItem.class)
	public List<AddRequireItem> getRequireItemByParams(@Param("agent_no") String agent_no,@Param("bp_id") String bp_id);

	List<AddRequireItem> findRequireItemByNameList(@Param("intoList")List<Integer> intoList);
	
}
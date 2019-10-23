package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.MerchantRequireItem;

@WriteReadDataSource
public interface MerchantRequireItemDao {
    @Update("update merchant_require_item set status=#{status},audit_time=now() where id=#{id}")
    int updateBymriId(@Param("id")Long id,@Param("status")String status);
    
    @Update("update merchant_require_item set content=#{record.content},status=0 where id=#{record.id}")
    int updateByMbpId(@Param("record")MerchantRequireItem record);
    
    @Select("select mri.*,ari.item_name,ari.remark,ari.check_status,ari.example_type,ari.photo from merchant_require_item mri"
    		+ " left join add_require_item ari on ari.item_id=mri.mri_id "
    		+ "where mri.mri_id=#{mriId} and merchant_no=#{merId}")
    @ResultType(MerchantRequireItem.class)
    MerchantRequireItem selectByMriId(@Param("mriId")String mriId,@Param("merId")String merId);
    
    @Insert("insert into merchant_require_item(merchant_no,mri_id,content,status)"
    		+ " values(#{record.merchantNo},#{record.mriId},#{record.content},#{record.status})")
    int insertInfo(@Param("record")MerchantRequireItem record);
    
    
    @Select("select * from  merchant_require_item where merchant_no=#{merId} and mri_id=3")
    @ResultType(MerchantRequireItem.class)
    MerchantRequireItem selectByAccountNo(@Param("merId")String merId);
    
    @Select("select * from  merchant_require_item where id=#{merId}")
    @ResultType(MerchantRequireItem.class)
    MerchantRequireItem findInfo(@Param("merId")String merId);

    @Select("select * from add_require_item ")
    @ResultType(AddRequireItem.class)
	List<AddRequireItem> selectPhotoAddress();
    
    @Select("select * from  merchant_require_item where merchant_no=#{merchantNo} and mri_id=#{mriId}")
    @ResultType(MerchantRequireItem.class)
    MerchantRequireItem selectByNoAndId(@Param("merchantNo")String merchantNo,@Param("mriId")String mriId);
}
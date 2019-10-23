package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ExaminationsLog;
import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

@WriteReadDataSource
public interface ExaminationsLogDao {

    @Insert("insert into examinations_log set item_no=#{record.itemNo},bp_id=#{record.bpId},open_status=#{record.openStatus},"
    		+ "examination_opinions=#{record.examinationOpinions},operator=#{record.operator},create_time=#{record.createTime}")
    int insert(@Param("record")ExaminationsLog record);

    @Select("select exl.*,bsu.real_name,uis.user_name as realName from examinations_log exl "
    		+ "LEFT JOIN boss_shiro_user bsu on exl.operator=bsu.id "
    		+ "LEFT JOIN user_info uis on exl.operator=uis.mobilephone "
    		+ "where exl.item_no=#{merchantId}")
    @ResultType(ExaminationsLog.class)
    List<ExaminationsLog> selectByMerchantId(@Param("merchantId")String merchantId);
}
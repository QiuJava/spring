package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MposMachines;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MposMachinesDao {


    @SelectProvider(type = MposMachinesDao.SqlProvider.class, method = "selectList")
    @ResultType(MposMachines.class)
    List<MposMachines> selectList(@Param("baseInfo") MposMachines baseInfo, @Param("page") Page<MposMachines> page);

    @Select("select count(1) from mpos_machines where sn_no = #{snNo}")
    Long snNoExited(@Param("snNo")String snNo);

    @InsertProvider(type = SqlProvider.class, method = "insertBatch")
    int insertBatch(@Param("list")List<MposMachines> list);

    @Select("select mm.* from mpos_machines mm where id = #{id}")
    MposMachines selectById(@Param("id") Long id);

    @Select("select mm.*,oi.org_name as orgName from mpos_machines mm LEFT OUTER JOIN org_info oi on oi.org_id = mm.org_id  where sn_no = #{sn}")
    MposMachines selectBySn(@Param("sn") String sn);

    @Update("UPDATE `mpos_machines` SET  `sn_no`=#{mposMachines.snNo}, `product_type`=#{mposMachines.productType},  `org_id`=#{mposMachines.orgId}, `status`=#{mposMachines.status}, " +
            "`v2_merchant_code`=#{mposMachines.v2MerchantCode}, `v2_merchant_name`=#{mposMachines.v2MerchantName}, `v2_merchant_phone`=#{mposMachines.v2MerchantPhone}, `purchaser_user_code`=#{mposMachines.purchaserUserCode}, `ship_date`=#{mposMachines.shipDate}, " +
            "`enabled_date`=#{mposMachines.enabledDate}, `active_date`=#{mposMachines.activeDate}, `order_id`=#{mposMachines.orderId}, `create_by`=#{mposMachines.createBy}, `create_date`=#{mposMachines.createDate}, `update_by`=#{mposMachines.updateBy}, `update_date`=#{mposMachines.updateDate} " +
            " WHERE `id` = #{mposMachines.id}")
    int update(@Param("mposMachines")MposMachines mposMachines);

    @Update("UPDATE `mpos_machines` SET  `org_id`=#{orgId}, `status`=2, " +
            " `update_by`=#{username}, `update_date`=#{date} " +
            " WHERE `status`=1 and `sn_no` BETWEEN #{snStart} AND #{snEnd}")
    int batchIssuedMachines(@Param("snStart")String snStart, @Param("snEnd")String snEnd, @Param("orgId")Long orgId, @Param("username")String username, @Param("date")Date date);

    @Select("select mm.sn_no ,mpt.type_name as productTypeName, oi.org_name as orgName from mpos_machines mm" +
            " LEFT OUTER JOIN org_info oi on oi.org_id = mm.org_id  "+
            " LEFT OUTER JOIN mpos_product_type mpt on mpt.id = mm.product_type " +
            " where order_id = #{orderId}")
    @ResultType(MposMachines.class)
    List<MposMachines> selectMposMachinesByOrderId(@Param("orderId") Long orderId);

    @Update("UPDATE `mpos_machines` SET  `order_id`=#{orderId}, `status`=#{status}, `ship_date`=#{date}, `purchaser_user_code` = #{userCode}, " +
            " `update_by`=#{username}, `update_date`=#{date} " +
            " WHERE 1=1 and `sn_no` = #{sn} ")
    int mposMachinesShip(@Param("orderId") Long orderId, @Param("status") Integer status, @Param("sn") String sn, @Param("username") String username, @Param("date") Date date ,@Param("userCode") String userCode);




    class SqlProvider{
        public String selectList(Map<String, Object> param){
            MposMachines baseInfo = (MposMachines) param.get("baseInfo");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("SELECT mm.*,oi.org_name,mpt.type_name as productTypeName,ui.phone as purchaserUserPhone,mo.order_no as orderNo  FROM mpos_machines mm ");
            stringBuffer.append(" LEFT OUTER JOIN org_info oi on oi.org_id = mm.org_id  ");
            stringBuffer.append(" LEFT OUTER JOIN user_info ui on ui.user_code = mm.purchaser_user_code  ");
            stringBuffer.append(" LEFT OUTER JOIN mpos_order mo on mo.id = mm.order_id  ");
            stringBuffer.append(" LEFT OUTER JOIN mpos_product_type mpt on mpt.id = mm.product_type  where 1=1 ");
            if(baseInfo != null){

                if(StringUtils.isNotEmpty(baseInfo.getSnStart())&& StringUtils.isNotEmpty(baseInfo.getSnEnd())){
                    stringBuffer.append(" and mm.sn_no BETWEEN #{baseInfo.snStart} AND #{baseInfo.snEnd}");
                    stringBuffer.append(" and length(`sn_no`)= LENGTH(#{baseInfo.snStart})");
                }
                if(StringUtils.isNotEmpty(baseInfo.getSnStart())&&StringUtils.isEmpty(baseInfo.getSnEnd())){
                    baseInfo.setSnStart(baseInfo.getSnStart()+"%");
                    stringBuffer.append(" and mm.sn_no like #{baseInfo.snStart}");
                }
                if(StringUtils.isEmpty(baseInfo.getSnStart())&&StringUtils.isNotEmpty(baseInfo.getSnEnd())){
                    baseInfo.setSnEnd(baseInfo.getSnEnd()+"%");
                    stringBuffer.append(" and mm.sn_no like #{baseInfo.snEnd}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getEnabledDateStart())&& StringUtils.isNotEmpty(baseInfo.getEnabledDateEnd())){
                    stringBuffer.append(" and mm.enabled_date BETWEEN #{baseInfo.enabledDateStart} AND #{baseInfo.enabledDateEnd}");
                }
                if(baseInfo.getStatus()!=null){
                    stringBuffer.append(" and mm.status = #{baseInfo.status}");
                }
                if(baseInfo.getOrgId()!=null&&baseInfo.getOrgId()!=-1){
                    stringBuffer.append(" and mm.org_id = #{baseInfo.orgId}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getPurchaserUserCode())){
                    stringBuffer.append(" and mm.purchaser_user_code = #{baseInfo.purchaserUserCode}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getV2MerchantPhone())){
                    stringBuffer.append(" and mm.v2_merchant_phone = #{baseInfo.v2MerchantPhone}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getPurchaserUserPhone())){
                    stringBuffer.append(" and ui.phone = #{baseInfo.purchaserUserPhone}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getV2MerchantCode())){
                    stringBuffer.append(" and mm.v2_merchant_code = #{baseInfo.v2MerchantCode}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getProductType())){
                    stringBuffer.append(" and mm.product_type = #{baseInfo.productType}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getOrgName())){
                    if(baseInfo.getOrgName().indexOf(",")!=-1){
                        String[] orgNameArray = baseInfo.getOrgName().split(",");
                        if(orgNameArray.length>0) {
                            stringBuffer.append(" and oi.org_name in( ");
                            for (int i = 0; i < orgNameArray.length; i++) {
                                String orgName = orgNameArray[i];
                                if(i==0){
                                    stringBuffer.append("'"+orgName+"'");
                                }else{
                                    stringBuffer.append(", '"+orgName+"'");
                                }
                            }
                        }
                        stringBuffer.append(" )");
                    }else {
                        stringBuffer.append(" and oi.org_name = #{baseInfo.orgName}");
                    }
                }

            }
            if(StringUtils.isNotEmpty(baseInfo.getOrderNo())){
                stringBuffer.append(" and mo.order_no = #{baseInfo.orderNo}");
            }
            stringBuffer.append(" order by mm.create_date desc");
            return stringBuffer.toString();
        }
        public String insertBatch(Map<String, Object> param){
            List<MposMachines> list = (List<MposMachines>) param.get("list");
            if(list == null || list.size() < 1){
                return "";
            }
            StringBuilder values = new StringBuilder();
            MessageFormat message = new MessageFormat("(#'{'list[{0}].snNo},#'{'list[{0}].productType}" +
                    ", #'{'list[{0}].orgId},#'{'list[{0}].status}"
                    + ", #'{'list[{0}].v2MerchantCode}, #'{'list[{0}].v2MerchantName},#'{'list[{0}].v2MerchantPhone}"
                    + ",#'{'list[{0}].purchaserUserCode},#'{'list[{0}].shipDate},#'{'list[{0}].enabledDate},#'{'list[{0}].activeDate}" +
                    ",#'{'list[{0}].orderId},#'{'list[{0}].createBy},#'{'list[{0}].createDate},#'{'list[{0}].updateBy},#'{'list[{0}].updateDate}),");
            for(int i = 0; i < list.size(); i++){
                values.append(message.format(new Integer[]{i}));
            }
            final String valuesSql  = values.substring(1, values.length() - 2);//去掉最前面那个括号,和最后面的,)
            SQL sql = new SQL();
            sql.INSERT_INTO("mpos_machines");
            sql.VALUES("`sn_no`, `product_type`, `org_id`, `status`, `v2_merchant_code`, `v2_merchant_name`, `v2_merchant_phone`, `purchaser_user_code`, `ship_date`, `enabled_date`, `active_date`, `order_id`, `create_by`, `create_date`, `update_by`, `update_date`" ,valuesSql);
            return sql.toString();
        }
    }
}

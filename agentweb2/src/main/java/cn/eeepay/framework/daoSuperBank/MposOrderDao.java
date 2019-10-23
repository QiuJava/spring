package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MposOrder;
import cn.eeepay.framework.model.MposOrderSum;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface MposOrderDao {

    @SelectProvider(type = MposOrderDao.SqlProvider.class, method = "selectList")
    @ResultType(MposOrder.class)
    List<MposOrder> selectList(@Param("baseInfo") MposOrder baseInfo, @Param("page") Page<MposOrder> page);

    @Select("SELECT " +
            " mo.*, oi.org_name, mg.good_no AS goodNo, mg.good_type_id, mg.mpos_type_id, mg.`status` AS goodStatus," +
            " mgi.img_url AS imgUrl, mgt.type_name AS typeName, mpt.type_name AS productType, ui0.user_name, ui0.user_type, ui0.phone as userPhone, ui0.remark as userRemark," +
            " ui1.user_name AS oneUserName," +
            " ui2.user_name AS twoUserName," +
            " ui3.user_name AS thrUserName," +
            " ui4.user_name AS fouUserName" +
            " ,pc.channel_name as payChannelName" +
            " FROM" +
            " mpos_order mo" +
            " LEFT OUTER JOIN org_info oi ON oi.org_id = mo.org_id" +
            " LEFT OUTER JOIN mpos_goods mg ON mg.id = mo.good_id" +
            " LEFT OUTER JOIN (select * from mpos_good_imgs where `type`=1 group by good_id) as mgi ON mgi.good_id = mg.id " +
            " LEFT OUTER JOIN mpos_good_type mgt ON mgt.id = mg.good_type_id" +
            " LEFT OUTER JOIN mpos_product_type mpt ON mpt.id = mg.mpos_type_id" +
            " LEFT OUTER JOIN user_info ui0 ON ui0.user_code = mo.user_code" +
            " LEFT OUTER JOIN user_info ui1 ON ui1.user_code = mo.one_user_code" +
            " LEFT OUTER JOIN user_info ui2 ON ui2.user_code = mo.two_user_code" +
            " LEFT OUTER JOIN user_info ui3 ON ui3.user_code = mo.thr_user_code" +
            " LEFT OUTER JOIN user_info ui4 ON ui4.user_code = mo.fou_user_code" +
            " LEFT OUTER JOIN pay_channles pc on pc.id = mo.pay_channel" +
            " WHERE order_no = #{orderNo}")
    MposOrder findByOrderNo(@Param("orderNo") String orderNo);

    @SelectProvider(type = SqlProvider.class, method = "selectSum")
    @ResultType(MposOrderSum.class)
    MposOrderSum selectMposOrderSum(@Param("baseInfo") MposOrder baseInfo);

    @Update("UPDATE `mpos_order` SET `order_no`=#{baseInfo.orderNo}, `user_code`=#{baseInfo.userCode}, `status`=#{baseInfo.status}, `pay_status`=#{baseInfo.payStatus}, " +
            " `org_id`=#{baseInfo.orgId}, `good_id`=#{baseInfo.goodId}, `good_title`=#{baseInfo.goodTitle}, `buy_num`=#{baseInfo.buyNum}, `good_single_price`=#{baseInfo.goodSinglePrice}, " +
            " `good_total_price`=#{baseInfo.goodTotalPrice}, `ship_way`=#{baseInfo.shipWay}, `need_ship_fee`=#{baseInfo.needShipFee}, `ship_fee`=#{baseInfo.shipFee}, `total_price`=#{baseInfo.totalPrice}, " +
            " `shipper`=#{baseInfo.shipper}, `receiver_name`=#{baseInfo.receiverName}, `receiver_phone`=#{baseInfo.receiverPhone}, `receiver_addr`=#{baseInfo.receiverAddr}, `pay_method`=#{baseInfo.payMethod}, " +
            " `pay_date`=#{baseInfo.payDate}, `pay_channel`=#{baseInfo.payChannel}, `pay_order_no`=#{baseInfo.payOrderNo}, `pay_channel_no`=#{baseInfo.payChannelNo}, `complete_date`=#{baseInfo.completeDate}, " +
            " `to_org_amount`=#{baseInfo.toOrgAmount}, `to_org_status`=#{baseInfo.toOrgStatus}, `to_org_date`=#{baseInfo.toOrgDate}, `receive_type`=#{baseInfo.receiveType}, `receive_date`=#{baseInfo.receiveDate}, " +
            " `ship_express`=#{baseInfo.shipExpress}, `ship_express_no`=#{baseInfo.shipExpressNo}, `ship_date`=#{baseInfo.shipDate}, `total_bonus`=#{baseInfo.totalBonus}, `plate_profit`=#{baseInfo.plateProfit}, `org_profit`=#{baseInfo.orgProfit}," +
            " `one_user_code`=#{baseInfo.oneUserCode}, `one_user_type`=#{baseInfo.oneUserType}, `one_user_profit`=#{baseInfo.oneUserProfit}, " +
            " `two_user_code`=#{baseInfo.twoUserCode}, `two_user_type`=#{baseInfo.twoUserType}, `two_user_profit`=#{baseInfo.twoUserProfit}, " +
            " `thr_user_code`=#{baseInfo.thrUserCode}, `thr_user_type`=#{baseInfo.thrUserType}, `thr_user_profit`=#{baseInfo.thrUserProfit}, " +
            " `fou_user_code`=#{baseInfo.fouUserCode}, `fou_user_type`=#{baseInfo.fouUserType}, `fou_user_profit`=#{baseInfo.fouUserProfit}, " +
            " `profit_status`=#{baseInfo.profitStatus}, `account_status`=#{baseInfo.accountStatus}, `application`=#{baseInfo.application}, `second_user_node`=#{baseInfo.secondUserNode}, " +
            " `province_name`=#{baseInfo.provinceName}, `city_name`=#{baseInfo.cityName}, `district_name`=#{baseInfo.districtName}, `remark`=#{baseInfo.remark}, `create_by`=#{baseInfo.createBy}, `create_date`=#{baseInfo.createDate}, `update_by`=#{baseInfo.updateBy}, `update_date`=#{baseInfo.updateDate}, `consign_remark`=#{baseInfo.consignRemark} WHERE `id`=#{baseInfo.id}")
    int update(@Param("baseInfo") MposOrder baseInfo);

    class SqlProvider {
        public String selectList(Map<String, Object> param) {
            MposOrder baseInfo = (MposOrder) param.get("baseInfo");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("SELECT mo.*,oi.org_name ");
            stringBuffer.append(" ,mg.good_no as goodNo, mg.good_type_id, mg.mpos_type_id,  mg.`status` as goodStatus");
            stringBuffer.append(" ,mgi.img_url as imgUrl");
            stringBuffer.append(" ,mgt.type_name as typeName");
            stringBuffer.append(" ,mpt.type_name as productType");
            stringBuffer.append(" ,ui0.user_name, ui0.user_type, ui0.remark as userRemark, ui1.user_name as oneUserName, ui2.user_name as twoUserName, ui3.user_name as thrUserName, ui4.user_name as fouUserName ");
            stringBuffer.append(" ,pc.channel_name as payChannelName");
            stringBuffer.append(" FROM mpos_order mo ");
            stringBuffer.append(" LEFT OUTER JOIN org_info oi on oi.org_id = mo.org_id");
            stringBuffer.append(" LEFT OUTER JOIN mpos_goods mg on mg.id = mo.good_id");
            stringBuffer.append(" LEFT OUTER JOIN (select * from mpos_good_imgs where `type`=1 group by good_id) as mgi ON mgi.good_id = mg.id ");
            stringBuffer.append(" LEFT OUTER JOIN mpos_good_type mgt on mgt.id = mg.good_type_id");
            stringBuffer.append(" LEFT OUTER JOIN mpos_product_type mpt on mpt.id = mg.mpos_type_id");
            stringBuffer.append(" LEFT OUTER JOIN user_info ui0 on ui0.user_code = mo.user_code");
            stringBuffer.append(" LEFT OUTER JOIN user_info ui1 on ui1.user_code = mo.one_user_code");
            stringBuffer.append(" LEFT OUTER JOIN user_info ui2 on ui2.user_code = mo.two_user_code");
            stringBuffer.append(" LEFT OUTER JOIN user_info ui3 on ui3.user_code = mo.thr_user_code");
            stringBuffer.append(" LEFT OUTER JOIN user_info ui4 on ui4.user_code = mo.fou_user_code");
            stringBuffer.append(" LEFT OUTER JOIN pay_channles pc on pc.id = mo.pay_channel");
            stringBuffer.append(" where 1=1");
            stringBuffer.append(whereSql(baseInfo));
            stringBuffer.append(" order by mo.create_date desc");

            return stringBuffer.toString();
        }
        public String selectSum(Map<String, Object> param) {
            MposOrder baseInfo = (MposOrder) param.get("baseInfo");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("SELECT count(*) as orderCount ,SUM(mo.buy_num) as goodCount,SUM(mo.total_price) as orderAmountCount");
       /*     stringBuffer.append(" ,mg.good_no as goodNo, mg.good_type_id, mg.mpos_type_id,  mg.`status` as goodStatus");
            stringBuffer.append(" ,mgi.img_url as imgUrl");
            stringBuffer.append(" ,mgt.type_name as typeName");
            stringBuffer.append(" ,mpt.type_name as productType");
            stringBuffer.append(" ,ui0.user_name, ui0.user_type");*/
            stringBuffer.append(" FROM mpos_order mo ");
            stringBuffer.append(" LEFT OUTER JOIN org_info oi on oi.org_id = mo.org_id");
            stringBuffer.append(" LEFT OUTER JOIN mpos_goods mg on mg.id = mo.good_id");
            stringBuffer.append(" LEFT OUTER JOIN (select * from mpos_good_imgs where `type`=1 group by good_id) as mgi ON mgi.good_id = mg.id");
            stringBuffer.append(" LEFT OUTER JOIN mpos_good_type mgt on mgt.id = mg.good_type_id");
            stringBuffer.append(" LEFT OUTER JOIN mpos_product_type mpt on mpt.id = mg.mpos_type_id");
            stringBuffer.append(" LEFT OUTER JOIN user_info ui0 on ui0.user_code = mo.user_code");
            stringBuffer.append(" LEFT OUTER JOIN pay_channles pc on pc.id = mo.pay_channel");
            stringBuffer.append(" where 1=1");
            stringBuffer.append(whereSql(baseInfo));

            return stringBuffer.toString();
        }

        public String whereSql(MposOrder baseInfo){
            StringBuffer stringBuffer = new StringBuffer();
            if (baseInfo != null) {
                if (StringUtils.isNotEmpty(baseInfo.getOrderNo())) {
                    stringBuffer.append(" and mo.order_no = #{baseInfo.orderNo}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getStatus())) {
                    stringBuffer.append(" and mo.status = #{baseInfo.status}");
                }
                if (baseInfo.getShipper() != null) {
                    stringBuffer.append(" and mo.shipper = #{baseInfo.shipper}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getGoodNo())) {
                    stringBuffer.append(" and mg.good_no = #{baseInfo.goodNo}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getGoodTitle())) {
                    stringBuffer.append(" and mg.good_title = #{baseInfo.goodTitle}");
                }
                if (baseInfo.getShipWay() != null) {
                    stringBuffer.append(" and mo.ship_way = #{baseInfo.shipWay}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getUserCode())) {
                    stringBuffer.append(" and mo.user_code = #{baseInfo.userCode}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getUserPhone())) {
                    stringBuffer.append(" and ui0.phone = #{baseInfo.userPhone}");
                }
                if (baseInfo.getOrgId() != null && baseInfo.getOrgId() != -1) {
                    stringBuffer.append(" and mo.org_id = #{baseInfo.orgId}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getProvinceName()) && !"全部".equals(baseInfo.getProvinceName())) {
                    stringBuffer.append(" and mo.province_name = #{baseInfo.provinceName}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getCityName()) && !"全部".equals(baseInfo.getCityName())) {
                    stringBuffer.append(" and mo.city_name = #{baseInfo.cityName}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getDistrictName()) && !"全部".equals(baseInfo.getDistrictName())) {
                    stringBuffer.append(" and mo.district_name = #{baseInfo.districtName}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getPayMethod())) {
                    stringBuffer.append(" and mo.pay_method = #{baseInfo.payMethod}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getPayStatus())) {
                    stringBuffer.append(" and mo.pay_status = #{baseInfo.payStatus}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getPayOrderNo())) {
                    stringBuffer.append(" and mo.pay_order_no = #{baseInfo.payOrderNo}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getCreateDateStart()) && StringUtils.isNotEmpty(baseInfo.getCreateDateEnd())) {
                    stringBuffer.append(" and mo.create_date BETWEEN #{baseInfo.createDateStart} AND #{baseInfo.createDateEnd}");
                }
                if (baseInfo.getToOrgStatus() != null) {
                    stringBuffer.append(" and mo.to_org_status = #{baseInfo.toOrgStatus}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getShipDateStart()) && StringUtils.isNotEmpty(baseInfo.getShipDateEnd())) {
                    stringBuffer.append(" and mo.ship_date BETWEEN #{baseInfo.shipDateStart} AND #{baseInfo.shipDateEnd}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getGoodStatus())) {
                    stringBuffer.append(" and mg.`status` = #{baseInfo.goodStatus}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getPayDateStart()) && StringUtils.isNotEmpty(baseInfo.getPayDateEnd())) {
                    stringBuffer.append(" and mo.pay_date BETWEEN #{baseInfo.payDateStart} AND #{baseInfo.payDateEnd}");
                }
                if (StringUtils.isNotEmpty(baseInfo.getReceiveDateStart()) && StringUtils.isNotEmpty(baseInfo.getReceiveDateEnd())) {
                    stringBuffer.append(" and mo.receive_date BETWEEN #{baseInfo.receiveDateStart} AND #{baseInfo.receiveDateEnd}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getPayChannel())){
                    stringBuffer.append(" and pc.`id` = #{baseInfo.payChannel}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getPayChannelNo())){
                    stringBuffer.append(" and mo.`pay_channel_no` = #{baseInfo.payChannelNo}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getAccountStatus())){
                    stringBuffer.append(" and mo.`account_status` = #{baseInfo.accountStatus}");
                }
                if(StringUtils.isNotEmpty(baseInfo.getGoodTypeId())){
                    stringBuffer.append(" and mgt.`id` = #{baseInfo.goodTypeId}");
                }


            }
            return stringBuffer.toString();
        }

    }
}

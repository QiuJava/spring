package cn.eeepay.framework.dao.nposp;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.SettleOrderInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface SettleOrderInfoMapper {




	@SelectProvider(type = SqlProvider.class, method = "findSettleOrderInfoList")
	@ResultMap("cn.eeepay.framework.dao.nposp.SettleOrderInfoMapper.BaseResultMap")
	List<SettleOrderInfo> findSettleOrderInfoList(@Param("settleOrderInfo")SettleOrderInfo settleOrderInfo, @Param("sort")Sort sort, Page<SettleOrderInfo> page);

	@SelectProvider(type = SqlProvider.class, method = "findSettleOrderInfoList")
	@ResultMap("cn.eeepay.framework.dao.nposp.SettleOrderInfoMapper.BaseResultMap")
	List<SettleOrderInfo> exportSettleOrderInfoList(@Param("settleOrderInfo")SettleOrderInfo settleOrderInfo, @Param("sort")Sort sort);



	
	public class SqlProvider {

		public String findSettleOrderInfoList(final Map<String, Object> parameter) {
			final SettleOrderInfo settleOrderInfo = (SettleOrderInfo) parameter.get("settleOrderInfo");

			final String agentNo = (String)settleOrderInfo.getSettleUserNo();
			final String status = (String)settleOrderInfo.getStatus();
			final String createTimeStart = (String)settleOrderInfo.getStartTime() ;
			final String createTimeEnd = (String)settleOrderInfo.getEndTime() ;
			final String inAccNo = (String)settleOrderInfo.getInAccNo();
			final String settleStatus = (String)settleOrderInfo.getSettleStatus();
			final Sort sord=(Sort)parameter.get("sort");

			return new SQL() {
				{
					SELECT(" o.settle_order, o.settle_user_no,o.create_time,o.settle_amount, o.sub_type, " +
							"t.fee_amount,t.in_acc_no,t.in_acc_name ," +
							"o.settle_msg ,o.agent_name ,o.settle_status , t.status ");
					FROM(" ( SELECT o.settle_order , o.sub_type, o.settle_user_no,o.create_time,o.settle_amount," +
									"o.settle_account_name,o.settle_account_no ," +
									" o.settle_msg ,a.agent_name ,o.settle_status " +
									" FROM settle_order_info o   , agent_info a " +
									" WHERE   o.settle_user_no = a.agent_no and  o.settle_user_type = 'A'" +
									" and  o.sub_type in ('4','5') ) o ");
					LEFT_OUTER_JOIN( "settle_transfer t on  o.settle_order = t.trans_id and " +
							"t.create_time = (select  create_time from settle_transfer where trans_id = o.settle_order order by create_time desc limit 1) ");

					if (!StringUtils.isBlank(agentNo))
						WHERE(" o.settle_user_no = #{settleOrderInfo.settleUserNo} ");
					if (!StringUtils.isBlank(settleStatus))
						WHERE(" o.settle_status = #{settleOrderInfo.settleStatus} ");
					if (!StringUtils.isBlank(status))
						WHERE(" t.status = #{settleOrderInfo.status} ");
					if (StringUtils.isNotBlank(createTimeStart) )
						WHERE(" o.create_time >=  #{settleOrderInfo.startTime} ");
					if (StringUtils.isNotBlank(createTimeEnd) )
						WHERE(" o.create_time <=  #{settleOrderInfo.endTime} ");
					if (!StringUtils.isBlank(inAccNo))
						WHERE(" t.in_acc_no = #{settleOrderInfo.inAccNo} ");

					if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
						ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
					} else {
						ORDER_BY("o.create_time desc ");
					}

				}
			}.toString();

		}


		public String propertyMapping(String name,int type){
			final String[] propertys={"createTime","settleUserNo","agentName","settleAmount","feeAmount"};
			final String[] columns={"create_time","settle_user_no","agent_name","settle_amount","fee_amount"};
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

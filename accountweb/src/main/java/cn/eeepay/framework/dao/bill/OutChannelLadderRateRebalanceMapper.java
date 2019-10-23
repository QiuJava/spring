package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutChannelLadderRateRebalance;

public interface OutChannelLadderRateRebalanceMapper {
	@SelectProvider(type = SqlProvider.class, method = "findOutChannelRate")
	@ResultType(OutChannelLadderRateRebalance.class)
	public List<OutChannelLadderRateRebalance> findOutChannelRate(@Param("obj")OutChannelLadderRateRebalance obj, @Param("sort")Sort sort, Page<OutChannelLadderRateRebalance> page);
	
	@Select("select * from out_channel_ladder_rate_rebalance where id=#{id}")
	@ResultType(OutChannelLadderRateRebalance.class)
	public OutChannelLadderRateRebalance getById(@Param("id")Long id);
	@Insert("insert into out_channel_ladder_rate_rebalance"
			+ "(out_acq_enname,out_service_id,re_type,re_year,re_month,"
			+ "total_out_amount_month,total_avg_day_out_amount_month,"
			+ "out_amount_month_fee,rebalance,real_rebalance,record_status) "
			+ "values(#{obj.outAcqEnname},#{obj.outServiceId},#{obj.reType},#{obj.reYear},"
			+ "#{obj.reMonth},#{obj.totalOutAmountMonth},#{obj.totalAvgDayOutAmountMonth},"
			+ "#{obj.outAmountMonthFee},#{obj.rebalance},#{obj.realRebalance},#{obj.recordStatus})")
	int insertOutChannelLadderRateRebalance(@Param("obj")OutChannelLadderRateRebalance obj);
	
	@Update("update out_channel_ladder_rate_rebalance set record_status=#{recordStatus} where id=#{id}")
	public int updateRecordStatus(@Param("id")Long id, @Param("recordStatus")Integer recordStatus );
	
	@Update("update out_channel_ladder_rate_rebalance set real_rebalance=#{item.realRebalance} where id=#{item.id}")
	int updateRealRebalance(@Param("item")OutChannelLadderRateRebalance item);
	
	public class SqlProvider {
		public String findOutChannelRate(final Map<String, Object> parameter) {
			final OutChannelLadderRateRebalance obj = (OutChannelLadderRateRebalance) parameter.get("obj");
			final Sort sort = (Sort) parameter.get("sort");
			
			return new SQL() {{
				SELECT("*");
				FROM("out_channel_ladder_rate_rebalance");
				if(StringUtils.isNotBlank(sort.getSidx())){
					ORDER_BY(propertyMapping(sort.getSidx(), 0)+" "+sort.getSord());
				}else{
					ORDER_BY(" re_year desc, re_month desc");
				}
			}}.toString();
		}
		
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"outAcqEnname","reYear","reMonth","totalOutAmountMonth","totalAvgDayOutAmountMonth","realRebalance", "recordStatus"};
		    final String[] columns={"out_acq_enname","re_year","re_month","total_out_amount_month","total_avg_day_out_amount_month","real_rebalance","record_status"};
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

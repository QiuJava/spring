package cn.eeepay.framework.dao.nposp;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;

/**
 * 快捷类对账明细表
 * @author yt
 *
 */
public interface CollectiveTransOrderMapper {
	@Select("select * from collective_trans_order where order_no = #{orderNo} and acq_enname = #{acqEnname} ")
	@ResultMap("cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper.BaseResultMap")
	CollectiveTransOrder findFastAccountDetailByTransInfo(@Param("orderNo")String orderNo,@Param("acqEnname")String acqEnname);
	
	@SelectProvider( type=SqlProvider.class,method="findCheckData")
	@ResultMap("cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper.BaseResultMap")
	List<CollectiveTransOrder> findCheckData(@Param("acqEnname")String acqEnname,@Param("transType")String transType, @Param("transTimeBegin")String transTimeBegin, @Param("transTimeEnd")String transTimeEnd);
	
	public class SqlProvider{
			
			public String findCheckData(final Map<String, Object> parameter) {
				final String acqEnname = (String) parameter.get("acqEnname");
				final String transTimeBegin = (String) parameter.get("transTimeBegin");
				final String transTimeEnd = (String) parameter.get("transTimeEnd");
				final String transType = (String) parameter.get("transType");
				return new SQL(){{
					SELECT(" * ");
					FROM("collective_trans_order ");
					WHERE(" trans_status = 'SUCCESS' ");
					if (StringUtils.isNotBlank(transType))
						WHERE(" trans_type  = #{transType} ");
					if (StringUtils.isNotBlank(acqEnname))
						WHERE(" acq_enname  = #{acqEnname} ");
					if (StringUtils.isNotBlank(transTimeBegin) && StringUtils.isNotBlank(transTimeEnd))
						WHERE(" trans_time between #{transTimeBegin} and #{transTimeEnd} ");
				}}.toString();
			}
	}
	@Select("select * from collective_trans_order where order_no = #{orderReferenceNo} and acq_enname = #{acqEnname} and trans_status='SUCCESS' and trans_type ='PURCHASE' ")
	@ResultMap("cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper.BaseResultMap")
	CollectiveTransOrder getTranDataByOrderNo(@Param("orderReferenceNo")String orderReferenceNo, @Param("acqEnname")String acqEnname);


	/**
	 * 盛付通专用
	 * @param acqReferenceNo
	 * @param acqEnname
	 * @return
	 */
	@Select("select c.*,s.acq_reference_no from collective_trans_order c LEFT JOIN scan_code_trans s ON c.order_no = s.trade_no  where s.acq_reference_no = #{acqReferenceNo} and c.acq_enname = #{acqEnname} and c.trans_status='SUCCESS' and c.trans_type ='PURCHASE' ")
	@ResultMap("cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper.BaseResultMap")
	CollectiveTransOrder getTranDataByOrderNo1(@Param("acqReferenceNo")String acqReferenceNo, @Param("acqEnname")String acqEnname);

	@Select("select profits_${agentNode} as num from collective_trans_order where order_no=#{orderNo}")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder selectByOrderNo(@Param("agentNode")int agentNode,@Param("orderNo")String orderNo);

	@Update("update collective_trans_order set freeze_status=#{freezeStatus} where order_no=#{orderNo}")
	int updateFreezeStatusByOrderNo(@Param("orderNo")String orderNo,@Param("freezeStatus")String freezeStatus);
}

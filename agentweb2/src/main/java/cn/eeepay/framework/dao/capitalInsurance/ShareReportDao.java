package cn.eeepay.framework.dao.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author liuks
 * 保险订单DAO
 */
@WriteReadDataSource
public interface ShareReportDao {

    @SelectProvider(type=ShareReportDao.SqlProvider.class,method="selectAllList")
    @ResultType(ShareReport.class)
    List<ShareReport> selectAllList(@Param("share") ShareReport share, @Param("page") Page<ShareReport> page);

    @SelectProvider(type=ShareReportDao.SqlProvider.class,method="selectSum")
    @ResultType(OrderTotal.class)
    OrderTotal selectSum(@Param("share") ShareReport share, @Param("page") Page<ShareReport> page);

    @SelectProvider(type=ShareReportDao.SqlProvider.class,method="selectAllList")
    @ResultType(ShareReport.class)
    List<ShareReport> exportDetailSelect(@Param("share") ShareReport share);


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            return  getSelectSql(param,1);
        }
        public String selectSum(final Map<String, Object> param) {
            return  getSelectSql(param,2);
        }
        public String getSelectSql(final Map<String, Object> param,int sta) {
            final ShareReport share = (ShareReport) param.get("share");
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(sta==1){
                sb.append(" report.id,report.bill_month,report.one_agent_no,report.total_amount, ");
                sb.append(" report.total_count,report.share_rate,report.share_amount,report.account_status, ");
                sb.append(" report.account_time,report.create_time, ");
                sb.append(" agent.agent_name oneAgentName ");

            }else if(sta==2){
                sb.append(" sum(report.share_amount) shareAmountTotal, ");
                sb.append(" sum(IF(report.account_status=1,report.share_amount,0)) shareAmountAccTotal, ");
                sb.append(" sum(IF(report.account_status!=1,report.share_amount,0)) shareAmountNoAccTotal ");
            }
            sb.append("from zjx_share_report report ");
            sb.append(" LEFT JOIN agent_info agent ON agent.agent_no=report.one_agent_no ");
            sb.append(" where 1=1 ");

            sb.append(" and agent.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{share.loginAgentNo})");

            if(StringUtils.isNotBlank(share.getBillMonth())){
                sb.append(" and report.bill_month = #{share.billMonth} ");
            }
            if(share.getAccountStatus()!=null){
                sb.append(" and report.account_status = #{share.accountStatus} ");
            }
            if (share.getAccountTimeBegin()!=null) {
                sb.append(" and  report.account_time>=#{share.accountTimeBegin}");
            }
            if (share.getAccountTimeEnd()!=null) {
                sb.append(" and  report.account_time<=#{share.accountTimeEnd}");
            }
            sb.append(" order by report.create_time desc ");
            return  sb.toString();
        }
    }
}

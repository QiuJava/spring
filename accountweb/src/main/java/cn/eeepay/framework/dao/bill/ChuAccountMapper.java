package cn.eeepay.framework.dao.bill;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutAccountTask;
import cn.eeepay.framework.model.bill.OutAccountTaskDetail;
import cn.eeepay.framework.model.bill.SettleTransfer;
import cn.eeepay.framework.model.bill.SettleTransferFile;

/**
 * 出账管理底层
 * @author Administrator
 *
 */
public interface ChuAccountMapper {

	/**
	 * 查询出  结算转账信息   
	 * @param settleTransferFile
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findSettleTransferFileList")
	@ResultMap("cn.eeepay.framework.dao.bill.SettleTransferFileMapper.BaseResultMap")
	List<SettleTransferFile> findSettleTransferFileList(@Param("settleTransferFile")SettleTransferFile settleTransferFile, @Param("sort")Sort sort, Page<SettleTransferFile> page);
	
	/**
	 * 通过 id 查询  结算转账  的详情
	 * @param id
	 * @return
	 */
	@Select("select sc.sys_name as settle_bank,total_num,total_amount,create_time,transfer_time,stf.status,summary "
			+ "FROM settle_transfer_file as stf,sys_dict as sc "
			+ "where stf.id=#{id} and stf.settle_bank = sc.sys_value and sc.sys_key = 'settle_transfer_bank'")
	@ResultMap("cn.eeepay.framework.dao.bill.SettleTransferFileMapper.BaseResultMap")
	SettleTransferFile findSettleTransferFileById(@Param("id")Integer id);
	
	/**
	 * 查询出  结算转账信息   
	 * @param settleTransferFile
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findSettleTransferList")
	@ResultMap("cn.eeepay.framework.dao.bill.SettleTransferMapper.BaseResultMap")
	List<SettleTransfer> findSettleTransferList(@Param("settleTransfer")SettleTransfer settleTransfer, @Param("sort")Sort sort, Page<SettleTransfer> page);
	
	
	@SelectProvider( type=SqlProvider.class,method="findSubmitChuKuanChannelList")
	@ResultMap("cn.eeepay.framework.dao.bill.SettleTransferFileMapper.BaseResultMap")
	List<SettleTransferFile> findSubmitChuKuanChannelList(@Param("settleTransferFile")SettleTransferFile settleTransferFile,
			@Param("createDate1")String createDate1,
			@Param("createDate2")String createDate2, 
			@Param("sort")Sort sort,
			Page<SettleTransferFile> page);
	
	
	public class SqlProvider{
		
		public String findSettleTransferFileList(final Map<String, Object> parameter) {
			final SettleTransferFile settleTransferFile = (SettleTransferFile) parameter.get("settleTransferFile");
			final String createTime1 = settleTransferFile.getCreateTime1();
			final String createTime2 = settleTransferFile.getCreateTime2();
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("stf.id,file_name,operator_name,sc.sys_name as settle_bank,CONCAT(total_num,'笔/',total_amount,'元') as total,create_time,stf.status,summary");
				FROM(" settle_transfer_file as stf,sys_dict as sc ");
				WHERE("stf.settle_bank = sc.sys_value and sc.sys_key = 'settle_transfer_bank' ");
				if (!StringUtils.isBlank(settleTransferFile.getSettleBank()) && !"ALL".equals(settleTransferFile.getSettleBank()) )
					WHERE(" settle_bank = #{settleTransferFile.settleBank} ");
				if (!StringUtils.isBlank(settleTransferFile.getStatus()) && !"ALL".equals(settleTransferFile.getStatus()) )
					WHERE(" stf.status = #{settleTransferFile.status} ");
				if (StringUtils.isNotBlank(createTime1) )
					WHERE(" stf.create_time >=  #{settleTransferFile.createTime1} ");
				if (StringUtils.isNotBlank(createTime2) )
					WHERE(" stf.create_time <=  #{settleTransferFile.createTime2} ");
				if (sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String findSettleTransferList(final Map<String, Object> parameter) {
			final SettleTransfer settleTransfer = (SettleTransfer) parameter.get("settleTransfer");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			return new SQL(){{
				SELECT(" st.id, "+
				" 	st.batch_id, "+
				" 	st.file_id, "+
				" 	st.seq_no, "+
				" 	st.file_name, "+
				" 	st.settle_bank, "+
				" 	st.in_acc_no, "+
				" 	st.in_acc_name, "+
				" 	st.in_settle_bank_no, "+
				" 	st.in_bank_no, "+
				" 	st.in_bank_name, "+
				" 	st.amount, "+
				" 	st.create_time, "+
				" 	st.`status`, "+
				" 	st.err_code, "+
				" 	st.err_msg, "+
				" 	st.bak1, "+
				" 	st.bak2 ");
				FROM(" settle_transfer AS st ");
				WHERE(" st.file_id = #{settleTransfer.fileId} ");
			}}.toString();
		}
		
		
		public String findSubmitChuKuanChannelList(final Map<String, Object> parameter) {
			final SettleTransferFile settleTransferFile = (SettleTransferFile) parameter.get("settleTransferFile");
			final String createDate1 = (String) parameter.get("createDate1");
			final String createDate2 = (String) parameter.get("createDate2");
			return new SQL(){{
				SELECT("stf.id,file_name,operator_name,sc.sys_name as settle_bank,CONCAT(total_num,'笔/',total_amount,'元') as total,create_time,stf.status,summary");
				FROM(" settle_transfer_file as stf,sys_dict as sc ");
				WHERE(" stf.settle_bank = sc.sys_value ");
				WHERE(" sc.sys_key = 'settle_transfer_bank' ");
				WHERE(" stf.status ='0' ");
				if (!StringUtils.isBlank(settleTransferFile.getSettleBank()) && !"ALL".equals(settleTransferFile.getSettleBank()) )
					WHERE(" settle_bank like  \"%\"#{settleTransferFile.settleBank}\"%\" ");
				if (StringUtils.isNotEmpty(createDate1))
					WHERE(" create_time >= #{createDate1} ");
				if (StringUtils.isNotEmpty(createDate2))
					WHERE(" create_time <= #{createDate2} ");
				/*if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}*/
			}}.toString();
		}
		
		public String findOutAccountTaskList(final Map<String, Object> parameter) {
			final OutAccountTask outAccountTask = (OutAccountTask) parameter.get("outAccountTask");
			//final Sort sord=(Sort)parameter.get("sort");
			//日期格式的处理，查询时去掉createTime 的时间部分
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			final String strCreateTime = outAccountTask.getCreateTime()==null?"":sdf.format(outAccountTask.getCreateTime()) ;
			return new SQL(){{
				SELECT("*");
				FROM(" out_account_task ");
				if (null!=outAccountTask.getCreateTime() )
					WHERE(" creat_time like  \"%"+strCreateTime+"%\" ");
				/*if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}*/
			}}.toString();
		}
		
		public String findOutAccountTaskUpdateList(final Map<String, Object> parameter) {
			final OutAccountTaskDetail outAccountTaskDetail = (OutAccountTaskDetail) parameter.get("outAccountTaskDetail");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT(" t.id, "+
					" 	t.out_account_task_id, "+
					" 	t.acq_org_id, "+
					" 	t.today_amount, "+
					" 	t.historical_balance, "+
					" 	t.today_balance, "+
					" 	t.out_account_amount, "+
					" 	t.sys_time, "+
					" 	t.create_time ");
				FROM("out_account_task_detail AS t ");
				
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"createTime","status","outAccountTaskId","acqOrgId","historicalBalance","todayBalance","outAccountAmount"};
		    final String[] columns={"create_time","status","out_account_task_id","acq_org_id","historical_balance","today_balance","out_account_amount"};
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

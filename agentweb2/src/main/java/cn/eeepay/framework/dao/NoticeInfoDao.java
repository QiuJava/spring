package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.NoticeInfo;

@WriteReadDataSource
public interface NoticeInfoDao {

    @Delete({
        "delete from notice_info_bak",
        "where nt_id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);
	
	@Insert("insert into notice_info(nt_id,title,content,attachment,create_time,issued_time,login_user,sys_type,receive_type,agent_role,agent_no,bp_id,status,issued_org,link,message_img)"
			+ "values(#{notice.ntId},#{notice.title},#{notice.content},#{notice.attachment},#{notice.createTime},#{notice.issuedTime},#{notice.loginUser},#{notice.sysType},#{notice.receiveType},"
			+ "#{notice.agentRole},#{notice.agentNo},#{notice.bpId},#{notice.status},#{notice.issuedOrg},#{notice.link},#{notice.messageImg})")
	int insert(@Param("notice")NoticeInfo notice);

    @Select({
        "select * from notice_info " +
        "where nt_id = #{id}"
    })
    @ResultType(NoticeInfo.class)
    NoticeInfo selectById(@Param("id")String id);
    
    @Update({
        "update notice_info set title = #{record.title}," +
          "content = #{record.content}," +
          "attachment = #{record.attachment}," +
          "sys_type = #{record.sysType}," +
          "status = #{record.status}," +
          "link = #{record.link}," +
          "message_img = #{record.messageImg}," +
          "receive_type = #{record.receiveType} " + 
        "where nt_id = #{record.ntId}"
    })
    int update(@Param("record")NoticeInfo record);
    
    @Update({
    	"update notice_info set status='2',issued_time=#{map.issuedTime} "
    	+ "where nt_id=#{map.id}"
    })
    int deliverNotice(@Param("map")Map<String, Object> map);

    @SelectProvider(type=SqlProvider.class,method="selectByParam")
    @ResultType(NoticeInfo.class)
	List<NoticeInfo> selectByParam(@Param("queryaram")NoticeInfo info,Page<NoticeInfo> page);
    
    @SelectProvider(type=SqlProvider.class,method="getNewNoticeByAgent")
	@ResultType(NoticeInfo.class)
	NoticeInfo getNewNoticeByAgent(@Param("receiveType")String receiveType);

	@SelectProvider(type=SqlProvider.class,method="selectReceiveNotices")
	@ResultType(NoticeInfo.class)
    List<NoticeInfo> selectReceiveNotices(@Param("notice") NoticeInfo notice,Page<NoticeInfo> page);

    public class SqlProvider {
    	public String selectReceiveNotices(final Map<String,Object> param){
    		final NoticeInfo info = (NoticeInfo)param.get("notice");
			SQL sql = new SQL() {
				{
					SELECT("nt_id,title, attachment, create_time, issued_time, sys_type, " +
							"bp_id, STATUS, issued_org, receive_type,strong,oem_list");
					FROM("notice_info");
					WHERE("msg_type = 0");	// 业务消息
					WHERE("sys_type = 2");	// 发送给代理商
					WHERE("STATUS = 2");		// 已经下发的
					WHERE("FIND_IN_SET(#{notice.oemType}, oem_type)");	 // 代理商的oem类型
					if (StringUtils.isNotBlank(info.getTitle())){
						WHERE("title like #{notice.title}");
					}
					if(StringUtils.isNotBlank(info.getStartIssuedTime())){
						WHERE("issued_time >= #{notice.startIssuedTime}");
					}
					if(StringUtils.isNotBlank(info.getEndIssuedTime())){
						WHERE("issued_time <= #{notice.endIssuedTime}");
					}
					if (StringUtils.equals("11", info.getReceiveType())){
						WHERE("receive_type in(1,2)");
					}else if(StringUtils.equals("21", info.getReceiveType())){
						WHERE("receive_type = 1");
					}
					ORDER_BY("strong DESC, issued_time DESC");
				}
			};
			return sql.toString();
		}
		public String selectByParam(final Map<String, Object> param) {
			final NoticeInfo info = (NoticeInfo) param.get("queryaram");
			return new SQL() {
				{
					SELECT("nt_id, title, content, attachment, create_time, issued_time, sys_type, bp_id, status, issued_org, receive_type");
					FROM("notice_info");
					WHERE("sys_type = 2 ");
					if (info.getLoginUser() != null && StringUtils.isNotBlank(info.getLoginUser())) {
						WHERE("login_user=#{queryaram.loginUser}");
					}
					if (info.getTitle() != null && StringUtils.isNotBlank(info.getTitle())) {
						WHERE("title like #{queryaram.title}");
					}
					if(info.getSysType() != null){
						String sysType = info.getSysType();
						if ( StringUtils.isNotBlank(sysType) && !"0".equals(sysType)) {
							WHERE("sys_type=#{queryaram.sysType}");
						}
					}
					if(info.getStatus() != null){
						String status = info.getStatus();
						if (StringUtils.isNotBlank(status) && !"0".equals(status)) {
							WHERE("status=#{queryaram.status}");
						}
					}
					if(info.getCreateTimeBegin() != null){
						WHERE("create_time >= #{queryaram.createTimeBegin}");
					}
					if(info.getCreateTimeEnd() != null){
						WHERE("create_time < #{queryaram.createTimeEnd}");
					}
					if(info.getIssuedTimeBegin() != null){
						WHERE("issued_time >= #{queryaram.issuedTimeBegin}");
					}
					if(info.getIssuedTimeEnd() != null){
						WHERE("issued_time < #{queryaram.issuedTimeEnd}");
					}
					if (info.getReceiveType() != null) {
						String rec = info.getReceiveType();
						if ("13".equals(rec)) {
							WHERE("receive_type in(1,3)");
						} else if ("12".equals(rec)) {
							WHERE("receive_type in(1,3)");
						} else if ("11".equals(rec)) {
							WHERE("receive_type in(1,2,3,4)");
						} else if ("23".equals(rec)) {
							WHERE("receive_type in(1,5,7)");
						} else if ("22".equals(rec)) {
							WHERE("receive_type in(1,5,7,8)");
						} else if ("21".equals(rec)) {
							WHERE("receive_type in(1,2,5,6)");
						}
					}
				}
			}.toString();
		}
		
		public String getNewNoticeByAgent(final Map<String, Object> param) {
			final String receiveType =  (String) param.get("receiveType");
			return new SQL() {
				{
					SELECT("title, content, attachment");
					FROM("notice_info");
					WHERE(" status=2 AND sys_type = 2 ");
					if (StringUtils.isNotBlank(receiveType)) {
						if ("13".equals(receiveType)) {
							WHERE("receive_type in(1,3)");
						} else if ("12".equals(receiveType)) {
							WHERE("receive_type in(1,3)");
						} else if ("11".equals(receiveType)) {
							WHERE("receive_type in(1,2,3,4)");
						} else if ("23".equals(receiveType)) {
							WHERE("receive_type in(1,5,7)");
						} else if ("22".equals(receiveType)) {
							WHERE("receive_type in(1,5,7,8)");
						} else if ("21".equals(receiveType)) {
							WHERE("receive_type in(1,2,5,6)");
						}
					}
					ORDER_BY("issued_time desc limit 1");
				}
			}.toString();
		}

		public String propertyMapping(String name, int type) {
			final String[] propertys = {"ntId", "title", "content", "attachment", "createTime", "issuedTime",
					"sysType", "bpId","status", "receiveType","agentNo","issuedOrg","loginUser"};
			final String[] columns = { "id", "nt_id", "title", "content", "attachment", "create_time", "issued_time",
					"sys_type", "bp_id","status","receive_type","agent_no","issued_org","login_user"};
			if (StringUtils.isNotBlank(name)) {
				if (type == 0) {// ���Բ���ֶ���
					for (int i = 0; i < propertys.length; i++) {
						if (name.equalsIgnoreCase(propertys[i])) {
							return columns[i];
						}
					}
				} else if (type == 1) {// �ֶ����������
					for (int i = 0; i < propertys.length; i++) {
						if (name.equalsIgnoreCase(columns[i])) {
							return propertys[i];
						}
					}
				}
			}
			return null;
		}
	}
	
}

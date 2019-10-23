package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BannerInfo;

@WriteReadDataSource
public interface BannerInfoDao {

	@Update("update banner_info set banner_status='1' where banner_id=#{bannerId}")
	int openStatus(@Param("bannerId")Long bannerId);
	
	@Update("update banner_info set banner_status='0' where banner_id=#{bannerId}")
	int closeStatus(@Param("bannerId")Long bannerId);
	
	/**
	 * 根据ID查询banner详情
	 * @param id
	 * @return
	 */
	@Select("select b.* ,t.team_name teamName,a.agent_name agentName from banner_info b left join team_info t on b.team_id=t.team_id "
			+ "left join agent_info a on b.agent_no=a.agent_no where b.banner_id=#{id}")
	@ResultType(BannerInfo.class)
	BannerInfo selectDetailById(@Param("id")String id);
	
	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(BannerInfo.class)
	List<BannerInfo> selectByCondition(@Param("banner")BannerInfo bannerInfo,Page<BannerInfo> page);
	
	public class SqlProvider{
		public String selectByCondition(Map<String, Object> param){
			final BannerInfo banner = (BannerInfo)param.get("banner");
			return new SQL(){{
				SELECT("banner_id,banner_name,online_time,offline_time,weight,"
						+ "banner_status");
				FROM("banner_info b");
				if(banner.getBannerName()!=null && StringUtils.isNotBlank(banner.getBannerName())){
					WHERE("b.banner_name like '%" + banner.getBannerName() + "%'");
				}
				if(banner.getBannerId() != null){
					WHERE("b.banner_id=#{banner.bannerId}");
				}
				if(banner.getBannerStatus() != null && !"2".equals(banner.getBannerStatus())){
					WHERE("b.banner_status=#{banner.bannerStatus}");
				}
			}}.toString();
		}
		
	}
	
	@Update("update banner_info set banner_name=#{banner.bannerName},weight=#{banner.weight},online_time=#{banner.onlineTime},"
			+ "offline_time=#{banner.offlineTime},team_id=#{banner.teamId},agent_no=#{banner.agentNo},banner_content=#{banner.bannerContent},"
			+ "banner_status=#{banner.bannerStatus},banner_attachment=#{banner.bannerAttachment},banner_link=#{banner.bannerLink} "
			+ "where banner_id=#{banner.bannerId}")
	int update(@Param("banner")BannerInfo banner);

	@Insert("insert into banner_info(banner_id,banner_name,weight,online_time,offline_time,team_id,agent_no,banner_content,"
			+ "banner_status,banner_attachment,banner_link) values(#{banner.bannerId},#{banner.bannerName},#{banner.weight},"
			+ "#{banner.onlineTime},#{banner.offlineTime},#{banner.teamId},#{banner.agentNo},#{banner.bannerContent},"
			+ "#{banner.bannerStatus},#{banner.bannerAttachment},#{banner.bannerLink})")
	int insert(@Param("banner")BannerInfo banner);

}

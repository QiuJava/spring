package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqMerQo;
import cn.eeepay.framework.model.AcqMerchantInfo;
import cn.eeepay.framework.util.WriteReadDataSource;

@WriteReadDataSource
public interface AcqMerchantInfoMapper {
	int deleteByPrimaryKey(Long id);

	int insert(AcqMerchantInfo record);

	int insertSelective(AcqMerchantInfo record);

	AcqMerchantInfo selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(AcqMerchantInfo record);

	int updateByPrimaryKey(AcqMerchantInfo record);

	AcqMerchantInfo findAcqMerInfoDetail(Long id);

	@SelectProvider(type = AcqMerchantInfoMapper.SqlProvider.class, method = "page")
	@ResultType(AcqMerchantInfo.class)
	List<AcqMerchantInfo> page(Page<AcqMerchantInfo> page, @Param("qo") AcqMerQo qo);

	public class SqlProvider {
		public String page(Map<String, Object> params) {
			final AcqMerQo qo = (AcqMerQo) params.get("qo");

			return new SQL() {
				{
					SELECT(" acq_mer.*,sys_d.sys_name AS into_source_name");
					FROM(" acq_merchant_info acq_mer JOIN sys_dict sys_d ON acq_mer.into_source = sys_d.sys_value ");
					WHERE(" sys_d.sys_key = 'ACQ_MER_INTO_SOURCE'");
					if (StringUtils.hasLength(qo.getAcqIntoNo())) {
						WHERE(" acq_mer.acq_into_no=#{qo.acqIntoNo}");
					}
					if (StringUtils.hasLength(qo.getMerchantName())) {
						WHERE(" acq_mer.merchant_name LIKE concat('%',#{qo.merchantName},'%')");
					}
					if (StringUtils.hasLength(qo.getLegalPerson())) {
						WHERE(" acq_mer.legal_person LIKE concat('%',#{qo.legalPerson},'%')");
					}
					if (StringUtils.hasLength(qo.getIntoSource())) {
						WHERE(" acq_mer.into_source=#{qo.intoSource}");
					}

					if (qo.getIntoStartTime() != null && qo.getIntoEndTime() != null) {
						WHERE(" acq_mer.create_time BETWEEN #{qo.intoStartTime}\r\n" + "AND #{qo.intoEndTime}");
					}

					if (qo.getAuditStartTime() != null && qo.getAuditEndTime() != null) {
						WHERE(" acq_mer.audit_time BETWEEN #{qo.auditStartTime}\r\n" + "AND #{qo.auditEndTime}");
					}
					WHERE(" acq_mer.agent_no=#{qo.agentNo}");

					if (qo.getAuditState() != -1) {
						WHERE(" acq_mer.audit_status=#{qo.auditState}");
					}
					if (qo.getMerchantType() != -1) {
						WHERE(" acq_mer.merchant_type=#{qo.merchantType}");
					}
					ORDER_BY("acq_mer.create_time DESC");
				}
			}.toString();
		}

	}

}
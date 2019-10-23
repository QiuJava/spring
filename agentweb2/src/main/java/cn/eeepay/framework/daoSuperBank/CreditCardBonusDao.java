package cn.eeepay.framework.daoSuperBank;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditCardBonus;
import cn.eeepay.framework.model.CreditcardSource;

public interface CreditCardBonusDao {

	@SelectProvider(type = SqlProvider.class, method = "selectCreditCardByPage")
	@ResultType(CreditCardBonus.class)
	List<CreditCardBonus> getCreditCardConf(@Param("creditCardConf") CreditCardBonus creditCardConf,
			@Param("pager") Page<CreditCardBonus> pager);
	
	 public class SqlProvider{

     public String selectCreditCardByPage(Map<String, Object> param){
           StringBuffer sql = new StringBuffer("");
           final CreditCardBonus creditCard = (CreditCardBonus) param.get("creditCardConf");
           sql.append(("select oi.org_id,oi.org_name,cs.BANK_NAME,"
//           		+ "cs.BANK_BONUS,cf.ORG_COST,"
           		+ "cf.ORG_PUSH_COST,cf.id,cf.source_id,cf.is_onlyone,(cs.BANK_BONUS-cf.ORG_COST) bankBonus"));
           sql.append((" from creditcard_bonus_conf AS cf , org_info AS oi ,creditcard_source cs "));
           sql.append((" where cf.ORG_ID=oi.org_id and cf.SOURCE_ID=cs.ID "));
            
           if(creditCard != null && creditCard.getSourceId() != null && creditCard.getSourceId() != -1L){
              sql.append(" and cf.source_id=#{creditCardConf.sourceId}");
           }
           sql.append(" and cf.org_id=#{creditCardConf.entityId}");
//           if(creditCard != null && creditCard.getOrgId() != null && creditCard.getOrgId() != -1L){
//               sql.append(" and cf.org_id=#{creditCardConf.orgId}");
//            }
            
           sql.append(" order by cf.id desc ");
           
           return sql.toString();
        }
    }
	
	@Select("select id,bank_name,show_order,bank_bonus from creditcard_source order by convert(bank_name using gbk)")
	@ResultType(CreditcardSource.class)
	List<CreditcardSource> allBanksList();
	
	@Update("update creditcard_bonus_conf set org_cost=#{orgCost},ORG_PUSH_COST=#{orgPushCost},is_onlyone=#{isOnlyone}, update_by=#{updateBy}, update_date=sysdate() where id=#{id}")
	int updCreditCardConf(CreditCardBonus creditCardConf);
	
	@Update("update creditcard_source set BANK_BONUS=#{bankBonus} where id=#{sourceId}")
	int updBankConf(CreditCardBonus creditCardConf);
	
	@Insert("insert into creditcard_source (id,bank_name,bank_bonus,status) values(#{id},#{bankName},#{bankBonus},'on')")
	int saveBank(@Param("id") long id,@Param("bankName") String bankName,@Param("bankBonus") String bankBonus);
	
	@Insert("insert into creditcard_bonus_conf (org_id,source_id,org_cost,org_push_cost,update_by,update_date,is_onlyone) values(#{orgId},#{sourceId},#{orgCost},#{orgPushCost},#{updateBy},sysdate(),#{isOnlyone})")
	int saveCreditCardConf(CreditCardBonus creditCardConf);
	
	@Select("select key_value from sys_id where key_id='creditcard_manage'")
	long getSysId();
	@Update("update sys_id set key_value=#{newId} where key_id='creditcard_manage' and key_value=#{oldId}")
	int updSysId(@Param("newId") long newId,@Param("oldId") long oldId);
	
	/**获取直营配置数据*/
	@Select("select cb.org_id,cb.source_id,cb.org_cost,cb.org_push_cost,cb.is_onlyone from creditcard_bonus_conf cb where cb.org_id=(select oi.org_id from org_info oi where oi.v2_orgcode=#{orgCode})")
	List<CreditCardBonus> getDirectSalesConf(@Param("orgCode") String orgCode);
	
	@Select("select count(id) from creditcard_bonus_conf where source_id=#{sourceId}")
	int isExist(@Param("sourceId") long sourceId);
}

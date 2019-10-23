package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.model.bill.AgentPreAdjust;
import cn.eeepay.framework.model.bill.AgentPreFreeze;
import cn.eeepay.framework.model.bill.AgentUnfreeze;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.service.bill.AgentsProfitAssemblyOrParsing;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Service("agentsProfitAssemblyOrParsing")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AgentsProfitAssemblyOrParsingImpl   implements AgentsProfitAssemblyOrParsing{
	private static final Logger log = LoggerFactory.getLogger(AgentsProfitAssemblyOrParsingImpl.class);

	@Autowired
	private AgentInfoService agentInfoService;

	@Override
	public Map<String, Object> resolvebatchPreAdjustFile(File temp, String uname) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		List<AgentPreAdjust> agentPreAdjusts = new ArrayList<AgentPreAdjust>();
		Workbook workbook = null;
	    workbook = WorkbookFactory.create(temp);
	    Sheet sheet = workbook.getSheetAt(0);
	    int rowCount = sheet.getLastRowNum() + 1;
	    log.info("导入账单的行数是：" + rowCount);
	    //从第2行开始逐条
		for(int i = 1; i < rowCount; i++){
			int rowflag=0;
			Row row = sheet.getRow(i);
			if (row == null) {
				break;
			}
			Cell cell0 = row.getCell(0); // 代理商编号
			Cell cell1 = row.getCell(1); // 代理商名称
			Cell cell2 = row.getCell(2); // 开通返现
			Cell cell3 = row.getCell(3); // 费率差异
			Cell cell4 = row.getCell(4); // 超级推成本
			Cell cell5 = row.getCell(5); // 风控扣款
			Cell cell6 = row.getCell(6); // 商户管理费
			Cell cell7 = row.getCell(7); // 保证金扣除
			Cell cell8 = row.getCell(8); // 其他
			Cell cell9 = row.getCell(9); // 备注
			AgentPreAdjust agentPreAdjust = new AgentPreAdjust();
			if(!StringUtils.isEmpty(getStringCell(cell0))){
				agentPreAdjust.setAgentNo(getStringCell(cell0).trim());
				agentPreAdjust.setAgentName(getStringCell(cell1));
				agentPreAdjust.setOpenBackAmount(StringUtils.isBlank(getStringCell(cell2)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell2)));
				agentPreAdjust.setRateDiffAmount(StringUtils.isBlank(getStringCell(cell3)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell3)));
				agentPreAdjust.setTuiCostAmount(StringUtils.isBlank(getStringCell(cell4)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell4)));
				agentPreAdjust.setRiskSubAmount(StringUtils.isBlank(getStringCell(cell5)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell5)));
				agentPreAdjust.setMerMgAmount(StringUtils.isBlank(getStringCell(cell6)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell6)));
				agentPreAdjust.setBailSubAmount(StringUtils.isBlank(getStringCell(cell7)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell7)));
				agentPreAdjust.setOtherAmount(StringUtils.isBlank(getStringCell(cell8)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell8)));
				agentPreAdjust.setRemark(getStringCell(cell9));
				agentPreAdjust.setApplicant(uname);

				if (agentPreAdjust.getOpenBackAmount().compareTo(BigDecimal.ZERO) != 0) {
					agentPreAdjust.setAdjustReason("open_return");
					rowflag =rowflag+1;
				}
				if (agentPreAdjust.getRateDiffAmount().compareTo(BigDecimal.ZERO) != 0) {
					agentPreAdjust.setAdjustReason("rate_variance");
					rowflag =rowflag+1;
				}
				if(agentPreAdjust.getTuiCostAmount().compareTo(BigDecimal.ZERO)!=0){
					agentPreAdjust.setAdjustReason("tui_cost_deduction");
					rowflag =rowflag+1;
				}
				if(agentPreAdjust.getRiskSubAmount().compareTo(BigDecimal.ZERO)!=0){
					agentPreAdjust.setAdjustReason("risk_deduction");
					rowflag =rowflag+1;
				}
				if(agentPreAdjust.getMerMgAmount().compareTo(BigDecimal.ZERO)!=0){
					agentPreAdjust.setAdjustReason("merchant_management_fee");
					rowflag =rowflag+1;
				}
				if(agentPreAdjust.getBailSubAmount().compareTo(BigDecimal.ZERO)!=0){
					agentPreAdjust.setAdjustReason("margin_deduction");
					rowflag =rowflag+1;
				}
				if(agentPreAdjust.getOtherAmount().compareTo(BigDecimal.ZERO)!=0){
					agentPreAdjust.setAdjustReason("other");
					rowflag =rowflag+1;
				}
				agentPreAdjust.setRowflag(rowflag);
				BigDecimal adjustAomunt = new BigDecimal(0);
				adjustAomunt = agentPreAdjust.getOpenBackAmount().add(agentPreAdjust.getRateDiffAmount()
						.add(agentPreAdjust.getTuiCostAmount().add(agentPreAdjust.getRiskSubAmount().add(agentPreAdjust.getMerMgAmount().add(agentPreAdjust.getBailSubAmount()
						.add(agentPreAdjust.getOtherAmount()))))));
				agentPreAdjust.setAdjustAmount(adjustAomunt);
			}
			agentPreAdjusts.add(agentPreAdjust);
		}
		map.put("agentPreAdjusts", agentPreAdjusts);
		map.put("status", true);
		return map;
	}
	
	private static String getStringCell(Cell cell) {
		if (cell != null)
			cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell != null ? cell.getStringCellValue() : null;
	}

	@Override
	public Map<String, Object> resolvebatchPreFreezeFile(File temp, String uname) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		List<AgentPreFreeze> agentPreFreezes = new ArrayList<AgentPreFreeze>();
		Workbook workbook = null;
		    workbook = WorkbookFactory.create(temp);
		    Sheet sheet = workbook.getSheetAt(0);
		    int rowCount = sheet.getLastRowNum() + 1;
		    log.info("导入账单的行数是：" + rowCount);
		    //从第2行开始逐条
			for(int i = 1; i < rowCount; i++){
				Row row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				Cell cell0 = row.getCell(0); // 代理商编号
				Cell cell1 = row.getCell(1); // 代理商名称
				Cell cell2 = row.getCell(2); // 机具款冻结
				Cell cell3 = row.getCell(3); // 其他预冻结金额
				Cell cell4 = row.getCell(4); // 备注
				AgentPreFreeze agentPreFreeze = new AgentPreFreeze();
				if(!StringUtils.isEmpty(getStringCell(cell0))){
					agentPreFreeze.setAgentNo(getStringCell(cell0).trim());
					agentPreFreeze.setAgentName(getStringCell(cell1));
					AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentPreFreeze.getAgentNo());
					if(agentInfo==null){		//代理商不存在
						throw new RuntimeException("代理商编号("+agentPreFreeze.getAgentNo()+")不存在");
					}
					if(!agentInfo.getAgentName().equals(agentPreFreeze.getAgentName())){		//代理商编号和名称不一致
						throw new RuntimeException("代理商编号("+agentPreFreeze.getAgentNo()+")和代理商名称不一致");
					}
					if(StringUtils.isNotBlank(getStringCell(cell2)) && StringUtils.isNotBlank(getStringCell(cell3))){
						throw new RuntimeException("批量预冻结错误：机具款预冻结金额和其他预冻结金额不能同时存在。");
					}
					agentPreFreeze.setTerminalFreezeAmount(StringUtils.isBlank(getStringCell(cell2)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell2)));
					agentPreFreeze.setOtherFreezeAmount(StringUtils.isBlank(getStringCell(cell3)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell3)));
					agentPreFreeze.setRemark(getStringCell(cell4));
					agentPreFreeze.setOperater(uname);
					agentPreFreezes.add(agentPreFreeze);
				}
			}
		map.put("agentPreFreezes", agentPreFreezes);
		map.put("status", true);
		return map;
	}

	@Override
	public Map<String, Object> resolvebatchUnfreezeFile(File temp, String uname) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		List<AgentUnfreeze> agentUnfreezes = new ArrayList<AgentUnfreeze>();
		Workbook workbook = null;
		
		    workbook = WorkbookFactory.create(temp);
		    Sheet sheet = workbook.getSheetAt(0);
		    int rowCount = sheet.getLastRowNum() + 1;
		    log.info("导入批量解冻的行数是：" + rowCount);
		    //从第2行开始逐条
			for(int i = 1; i < rowCount; i++){
				Row row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				Cell cell0 = row.getCell(0); // 代理商编号
				Cell cell1 = row.getCell(1); // 代理商名称
				Cell cell2 = row.getCell(2); // 解冻金额
				Cell cell3 = row.getCell(3); // 备注
				AgentUnfreeze agentUnfreeze = new AgentUnfreeze();
				if(!StringUtils.isEmpty(getStringCell(cell0))){
					log.info("导入批量解冻的代理商编号是：" + getStringCell(cell0));
					agentUnfreeze.setAgentNo(getStringCell(cell0).trim());
					agentUnfreeze.setAgentName(StringUtils.isBlank(getStringCell(cell1)) ? "":getStringCell(cell1).trim());
					AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentUnfreeze.getAgentNo());
					if(agentInfo==null){		//代理商不存在
						throw new RuntimeException("代理商编号("+agentUnfreeze.getAgentNo()+")不存在");
					}
					if(!agentInfo.getAgentName().equals(agentUnfreeze.getAgentName())){		//代理商编号和名称不一致
						throw new RuntimeException("代理商编号("+agentUnfreeze.getAgentNo()+")和代理商名称不一致");
					}
					agentUnfreeze.setAmount(StringUtils.isBlank(getStringCell(cell2)) ? BigDecimal.ZERO : new BigDecimal(getStringCell(cell2)));
					agentUnfreeze.setRemark(getStringCell(cell3));
					agentUnfreeze.setUnfreezeTime(new Date());
					agentUnfreeze.setOperater(uname);
					agentUnfreezes.add(agentUnfreeze);
				}

			}
 
		map.put("agentUnfreezes", agentUnfreezes);
		map.put("status", true);
		return map;
	}
}

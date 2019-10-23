package cn.eeepay.framework.service.impl.capitalInsurance;

import cn.eeepay.framework.dao.capitalInsurance.ShareReportDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;
import cn.eeepay.framework.service.capitalInsurance.ShareReportService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author liuks
 * 保险订单service 实现类
 */
@Service("shareReportService")
public class ShareReportServiceImpl implements ShareReportService {

    private static final Logger log = LoggerFactory.getLogger(ShareReportServiceImpl.class);

    @Resource
    private ShareReportDao shareReportDao;

    @Override
    public List<ShareReport> selectAllList(ShareReport share, Page<ShareReport> page) {
        return shareReportDao.selectAllList(share,page);
    }

    @Override
    public OrderTotal selectSum(ShareReport share, Page<ShareReport> page) {
        return shareReportDao.selectSum(share,page);
    }

    @Override
    public List<ShareReport> exportDetailSelect(ShareReport share) {
        return shareReportDao.exportDetailSelect(share);
    }

    @Override
    public void exportDetail(List<ShareReport> list, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "分润月报列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("billMonth",null);
            maps.put("oneAgentNo",null);
            maps.put("oneAgentName",null);
            maps.put("totalAmount",null);
            maps.put("totalCount",null);
            maps.put("shareRate",null);
            maps.put("shareAmount",null);
            maps.put("accountStatus",null);
            maps.put("accountTime",null);
            data.add(maps);
        }else{
            Map<String, String> accountStatusMap=new HashMap<String, String>();
            accountStatusMap.put("1","入账成功");
            accountStatusMap.put("2","入账失败");
            accountStatusMap.put("3","未入账");

            for (ShareReport or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("billMonth",or.getBillMonth()==null?null:or.getBillMonth());
                maps.put("oneAgentNo",or.getOneAgentNo()==null?null:or.getOneAgentNo());
                maps.put("oneAgentName",or.getOneAgentName()==null?null:or.getOneAgentName());
                maps.put("totalAmount",or.getTotalAmount()==null?"":or.getTotalAmount().toString());
                maps.put("totalCount",or.getTotalCount()==null?"":or.getTotalCount().toString());
                maps.put("shareRate",or.getShareRate()==null?null:or.getShareRate().toString());
                maps.put("shareAmount",or.getShareAmount()==null?"":or.getShareAmount().toString());
                maps.put("accountStatus",accountStatusMap.get(or.getAccountStatus().toString()));
                maps.put("accountTime", or.getAccountTime()==null?"":sdf1.format(or.getAccountTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"billMonth","oneAgentNo","oneAgentName","totalAmount",
                "totalCount","shareRate","shareAmount","accountStatus","accountTime"
        };
        String[] colsName = new String[]{"保单月份","一级代理商编号","一级代理商名称","保险订单总金额",
                "保单数","代理商分润百分比(单位:%)","代理商分润金额","分润入账状态","入账时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出分润月报列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}

package cn.eeepay.framework.service.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author  liuks
 * 保险订单service
 */
public interface ShareReportService {

    List<ShareReport> selectAllList(ShareReport share, Page<ShareReport> page);

    OrderTotal selectSum(ShareReport share, Page<ShareReport> page);

    List<ShareReport> exportDetailSelect(ShareReport share);

    void exportDetail(List<ShareReport> list, HttpServletResponse response) throws Exception;
}

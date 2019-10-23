package cn.eeepay.boss.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.NoticeInfo;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.NoticeInfoService;
import cn.eeepay.framework.util.DateEditor;
import cn.eeepay.framework.util.RespUtil;

/*
// 从boss复制
*/
@RestController
@RequestMapping(value = "/notice")
public class NoticeAction {

	private static final Logger log = LoggerFactory.getLogger(NoticeAction.class);

	@Resource
	private NoticeInfoService noticeInfoService;
	
	@Autowired
	private AgentInfoService agentInfoService;
	
	/**
	 * 下发公告查询
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectByParam.do")
	public @ResponseBody Object selectByParam(@RequestParam("baseInfo") String param,@ModelAttribute("page") 
		Page<NoticeInfo> page) throws Exception{
		try{
			NoticeInfo notice = JSONObject.parseObject(param, NoticeInfo.class);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			notice.setLoginUser(principal.getUserEntityInfo().getUserId());
			if (notice.getTitle() != null) {
				notice.setTitle("%"+notice.getTitle()+"%");
			}
			noticeInfoService.selectByParam(notice, page);
		} catch (Exception e){
			log.error("条件查询公告失败",e);
		}
		return page;
	}
	
	/**
	 * 收到的公告查询
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/selectByParam2.do")
//	public @ResponseBody Object selectByParam2(@RequestParam("baseInfo") String param,@ModelAttribute("page")
//		Page<NoticeInfo> page) throws Exception{
//		try{
//			NoticeInfo notice = JSONObject.parseObject(param, NoticeInfo.class);
//
//			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			//判断自己的身份
//			//1. 非直营代理商3级包括以下 ：只能接收1，5，7类型的公告
//			//2. 非直营代理商2级： 只能接收1，5，7，8类型的 公告
//			//3. 非直营代理商1级：只能接收1，2，5，6类型的公告
//			//4. 直营代理商3级包括以下：只能接收1,3类型的公告
//			//5. 直营代理商2级：只能接收1,3类型的公告
//			//6. 直营代理商1级：只能接收1,2,3,4类型的公告
//			AgentInfo agentInfo = agentInfoService.selectByPrincipal();
//			int level = agentInfo.getAgentLevel();
//			if (principal.getTeamId().equals("100010")) {
//				//直营
//				if (level >= 3) {
//					notice.setReceiveType("13");
//				} else if (level == 2){
//					notice.setReceiveType("12");
//				} else if (level == 1) {
//					notice.setReceiveType("11");
//				}
//			} else if (principal.getTeamId().equals("200010")){
//				//非直营
//				if (level >= 3) {
//					notice.setReceiveType("23");
//				} else if (level == 2){
//					notice.setReceiveType("22");
//				} else if (level == 1) {
//					notice.setReceiveType("21");
//				}
//			}
//			if (notice.getTitle() != null) {
//				notice.setTitle("%"+notice.getTitle()+"%");
//			}
//			noticeInfoService.selectByParam(notice, page);
//		} catch (Exception e){
//			log.error("条件查询公告失败",e);
//		}
//		return page;
//	}

    /**
     * 收到的公告查询
     */
    @RequestMapping(value = "/selectReceiveNotices.do")
    public @ResponseBody
    ResponseBean selectReceiveNotices(@RequestBody NoticeInfo notice,
                                      @ModelAttribute("page") Page<NoticeInfo> page){
        try{
            //判断自己的身份
            // 一级代理商,只能接收 1,2 类型的公告 (11)
            // 非一级代理商,只能接收 1 的公告     (21)

            AgentInfo agentInfo = agentInfoService.selectByPrincipal();
            int level = agentInfo.getAgentLevel();
            notice.setReceiveType(level == 1 ? "11" : "21");
            notice.setOemType(agentInfo.getOemType());
            if (notice.getTitle() != null) {
                notice.setTitle("%"+notice.getTitle()+"%");
            }
            noticeInfoService.selectReceiveNotices(notice, page);
            List<NoticeInfo> list = page.getResult();
            if ("11".equals(agentInfo.getAgentType()) && list.size() > 0) {
            	for (NoticeInfo noticeInfo : list) {
                	String oemList = noticeInfo.getOemList();
                	if (!oemList.contains(agentInfo.getAgentOem())) {
                		list.remove(noticeInfo);
                	}
                }
			}
        } catch (Exception e){
            log.error("条件查询公告失败",e);
        }
        return new ResponseBean(page.getResult(), page.getTotalCount());
    }

	@RequestMapping(value = "/saveNotice.do")
	public Map<String, Object> saveNotice(@RequestBody NoticeInfo notice) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			int ret = 0;
			Map<String, Object> data = new HashMap<>();
			data.put("notice", notice);
			ret= noticeInfoService.update(data);
			if (ret > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "修改失败");
			}

		} catch (Exception e) {
			log.error("添加 || 修改公告失败！", e);
			msg.put("status", false);
			msg.put("msg", "添加 || 修改公告失败！");
		}
		return msg;
	}

	/**
	 * 查询详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SystemLog(description = "查询公告详情", saveResult = false)
	@RequestMapping(value = "/selectById/{id}")
	@ResponseBody
	public ResponseBean selectById(@PathVariable("id") String id) throws Exception{
		try{
			AgentInfo loginAgentInfo = agentInfoService.selectByPrincipal();
			NoticeInfo noticeInfo = noticeInfoService.selectById(id);
			if ("11".equals(loginAgentInfo.getAgentType())) {
            	String oemList = noticeInfo.getOemList();
            	if (!oemList.contains(loginAgentInfo.getAgentOem())) {
            		noticeInfo = null;
            	}
            }
			return new ResponseBean(noticeInfo);
		} catch (Exception e){
			log.error("查询公告详情失败！",e);
			return new ResponseBean(e);
		}
	}
	
	/**
	 * 查询详情，以及修改页面需要携带的数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectInfoById/{id}")
	@ResponseBody
	public ResponseBean selectInfoById(@PathVariable("id") String id) throws Exception{
		System.out.println("根据公告ID查询详情，ID = " + id);
		try{
			return new ResponseBean(noticeInfoService.selectInfoById(id));
		} catch (Exception e){
			log.error("查询公告详情失败！",e);
			return new ResponseBean(e);
		}
	}

	@RequestMapping(value = "/deliverNotice/{id}")
	@ResponseBody
	public Map<String,Object> deliverNotice(@PathVariable("id") long id) {
		Map<String, Object> msg = new HashMap<>();
		if(id==0){
			msg.put("status", false);
			msg.put("msg", "下发失败");
			return msg;
		}
		int sum = noticeInfoService.deliverNotice(id);
		if(sum > 0){
			msg.put("status", true);
			msg.put("msg", "下发成功");
		}
		return msg;
	}
	
	@RequestMapping(value = "/addNotice.do", method = {RequestMethod.GET, RequestMethod.POST})
	public void add(HttpServletResponse response, @RequestBody NoticeInfo notice) {
		Map<String, Object> data = new HashMap<String, Object>();
//		JSONObject json = JSON.parseObject(param);
//		NoticeInfo notice = json.getObject("notice", NoticeInfo.class);
		int result = 0;
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal.getTeamId().equals("200010")) {
				notice.setLoginUser(principal.getUserEntityInfo().getUserId());
				notice.setCreateTime(new Date());
				notice.setStatus("1");
				notice.setAgentNo(principal.getUserEntityInfo().getEntityId());
				notice.setIssuedOrg(principal.getUserEntityInfo().getEntityId());
				result = noticeInfoService.insert(notice);
				
				if (result > 0) {
					data.put("success", true);
					data.put("msg", "新增公告成功");
				} else {
					data.put("success", false);
					data.put("msg", "新增公告失败");
				}
				
			} else {
				data.put("success", false);
				data.put("msg", "只有OEM组织中一级代理商才有下发公告功能");
			}
			
		} catch (Exception e) {
			log.error("新增下发公告出错: "+e);
			data.put("success", false);
		}
		
		RespUtil.renderJson(response, data);
	}
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

}

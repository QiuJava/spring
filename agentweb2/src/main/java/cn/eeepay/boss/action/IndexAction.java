package cn.eeepay.boss.action;

import cn.eeepay.boss.security.ClientTeamIdUtil;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.OemTypeEnum;
import cn.eeepay.framework.util.RandomValidateCodeUtils;
import cn.eeepay.framework.util.Sms;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Struct;
import java.util.*;

/**
 * 登录，首页管理
 * <p>
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 */

@Controller
@RequestMapping(value = "/")
public class IndexAction extends BaseAction {


	private static final Logger log = LoggerFactory.getLogger(IndexAction.class);
	@Resource
	public SysMenuService sysMenuService;
	@Resource
	public AgentInfoService agentInfoService;
	
	@Resource
	public NoticeInfoService noticeInfoService;

	@Resource
	public UserInfoService userInfoService;
	@Resource
	private PerAgentService perAgentservice;
	@Resource
	private SysDictService sysDictService;
    @Resource
    private RedisService redisService;

	@RequestMapping(value = "/welcome.do")
	public String welcome(ModelMap model, @RequestParam Map<String, String> params) throws Exception {

		System.out.println("welcome");
		String verNo = "2.0.001";
		SysDict sysDict = sysDictService.getByKey("VER_NUM_AGENTWEB");
		if(sysDict != null){
			verNo = sysDict.getSysValue();
		}
		model.addAttribute("verNo", verNo);
		final Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principalObj instanceof UserLoginInfo) {
			final UserLoginInfo principal = (UserLoginInfo) principalObj;
			model.addAttribute("username", principal.getRealName());
			model.addAttribute("entityInfo", principal.getUserEntityInfo());
			model.addAttribute("isAgent", principal.getUserEntityInfo().getIsAgent());
			model.addAttribute("teamId", principal.getTeamId());
			Set<String> permits = new HashSet<String>();
			for (GrantedAuthority item : principal.getAuthorities()) {
				permits.add(item.getAuthority());
			}
			model.addAttribute("permits", permits);
			model.addAttribute("permitsJSON", JSON.toJSONString(permits));
			final String entityId = principal.getUserEntityInfo().getEntityId();
			model.addAttribute("agentInfo", agentInfoService.selectByagentNo(entityId));
			model.addAttribute("oemType", principal.getOemTypeEnum());
			model.addAttribute("oemTransType", principal.getOemTypeEnum().getTransType());
			model.addAttribute("safePasswordFlag", principal.getOemTypeEnum().getHasSafePassword());
            if ("PERAGENT".equals(principal.getOemTypeEnum() + "")) {
                //查询登录代理商对应的人人代理用户信息
                PaUserInfo paUserInfo = perAgentservice.selectUserByAgentNo(entityId);
                if (paUserInfo != null) {
                    model.addAttribute("loginUserType", paUserInfo.getUserType());
                }
            }
		} else {
			model.addAttribute("permits", Collections.EMPTY_SET);
			model.addAttribute("permitsJSON", "[]");
			model.addAttribute("entityInfo", null);
			model.addAttribute("agentInfo", null);
			model.addAttribute("username", "");
			model.addAttribute("teamId", "");
			model.addAttribute("oemType", "");
			model.addAttribute("oemTransType", "DEFAULT");
            model.addAttribute("safePasswordFlag", false);
		}
		return "index";
	}

	@RequestMapping(value = "/login.do")
	public String loginPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		final boolean isNormalRequest = request.getParameter("logout") == null || request.getParameter("error") == null;
		if (!isNormalRequest && !ClientTeamIdUtil.isClientDefaultTeam(request)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("login.do");
			dispatcher.forward(request, response);
			return null;
		}

		if (isNormalRequest) {

//			ClientTeamIdUtil.setSessionTeamId(session, ClientTeamIdUtil.DEFAULT_TEAM_ID);
            ClientTeamIdUtil.setSessionTeamId(session, Constants.TEAM_ID);
        }

        ClientTeamIdUtil.findTeamIdFromSessionAndClient(request, session);
        ClientTeamIdUtil.saveTeamIdToCookie(session, response);
        return "login";
    }

    /**
     * ajax登录成功时，302到welcome.do
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/welcome.do", produces = {"application/json"})
    @ResponseBody
    public Object welcomeAjax(HttpServletRequest request, HttpServletResponse response) {
        System.out.print("PERAGENTMap=" + 112233);
        ModelMap model = new ModelMap();
        final Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principalObj instanceof UserLoginInfo) {
            final UserLoginInfo principal = (UserLoginInfo) principalObj;
            model.addAttribute("status", true);
            model.addAttribute("userid", principal.getUserEntityInfo().getUserId());
            model.addAttribute("username", principal.getRealName());
            model.addAttribute("teamId", principal.getTeamId());
            final String entityId = principal.getUserEntityInfo().getEntityId();
            model.addAttribute("entityId", entityId);
            model.addAttribute("oemType", principal.getOemTypeEnum());
            model.addAttribute("oemTransType", principal.getOemTypeEnum().getTransType());
            model.addAttribute("safePasswordFlag", principal.getOemTypeEnum().getHasSafePassword());
            Set<String> permits = new HashSet<String>();
            for (GrantedAuthority item : principal.getAuthorities()) {
                permits.add(item.getAuthority());
            }
            model.addAttribute("permits", permits);
            final AgentInfo agentInfo = agentInfoService.selectByagentNo(entityId);
            model.addAttribute("entityName", agentInfo == null ? "" : agentInfo.getAgentName());
            model.addAttribute("_csrf", request.getAttribute("_csrf"));

            if ("PERAGENT".equals(principal.getOemTypeEnum() + "")) {
                //查询登录代理商对应的人人代理用户信息
                PaUserInfo paUserInfo = perAgentservice.selectUserByAgentNo(entityId);
                if (paUserInfo != null) {
                    model.addAttribute("loginUserType", paUserInfo.getUserType());
                }
            }
            ClientTeamIdUtil.saveTeamIdToCookie(request.getSession(), response);
        } else {
            model.addAttribute("status", false);
            model.addAttribute("message", "匿名用户");
        }
        return model;
    }

    /**
     * ajax登录失败时，302到login.do?error或oemlogin.do?error
     *
     * @param request
     * @param response
     * @param error
     * @return
     */
    @RequestMapping(value = {"/login.do", "/oemlogin.do"}, produces = {"application/json"})
    @ResponseBody
    public Object loginPageAjax(HttpServletRequest request, HttpServletResponse response, String error) {
        JSONObject ret = new JSONObject();
        ret.put("message", "用户名或密码错误.");
        ret.put("status", false);
        if ("block".equals(request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION"))) {
            ret.put("message", "登录错误太多,30分钟后重试.");
        }
        return ret;
    }

    @RequestMapping(value = "/oemlogin.do")
    public String oemLoginPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        ClientTeamIdUtil.setSessionTeamId(session, Constants.TEAM_ID);
        ClientTeamIdUtil.saveTeamIdToCookie(session, response);
        return "oemlogin";
    }

    @SystemLog(description = "更新密码")
    @RequestMapping(value = "/findPwd/upPwd.do")
    @ResponseBody
    public Map<String, Object> upPwd(HttpServletRequest request, HttpServletResponse response) {
        boolean status = false;
        String title = "温馨提示";
        String body = "密码修改失败";
        String mobile = request.getParameter("mobile");
        String newPwd = request.getParameter("newPwd");
        String teamId = request.getParameter("teamid");
        String sign = request.getParameter("sign");
        String sign_session = (String) request.getSession().getAttribute("sign_session");
        String mobile_session = (String) request.getSession().getAttribute("mobile");
        if (!StringUtils.equals(mobile, mobile_session) || !StringUtils.equals(sign, sign_session)) {
            return getMsg(false, title, "密码修改失败");
        }
        Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
        String password = passEnc.encodePassword(newPwd, mobile);
        //System.out.println(String.format("手机号:%s;新密码:%s;密码密文：%s;",mobile,newPwd,password));
        String userId = request.getSession().getAttribute("userInfo").toString();
        UserInfo userInfo = userInfoService.findUserInfoByUserId(userId);
        if (userInfo != null) {
            userInfo.setPassword(password);
            int row = userInfoService.updateUserInfoByUserId(userInfo);
            if (row == 1) {
                status = true;
                body = "密码修改成功";
                //修改人人代理密码
                if (Constants.PER_AGENT_TEAM_ID.equals(teamId)) {
                    AgentInfo agent = agentInfoService.selectByMobilephoneAndTeamId(mobile, "998");
                    if (agent != null) {
                        perAgentservice.updatePassword(agent.getAgentNo(), password);
                    }
                }
            }
        }
        return getMsg(status, title, body);
    }

    @RequestMapping(value = "/findPwd/checkMsgCode.do")
    @ResponseBody
    public Map<String, Object> checkMsgCode(HttpServletRequest request, HttpServletResponse response) {
        boolean status = false;
        String title = "温馨提示";
        String body = "验证码错误";
        String mobile = request.getParameter("mobile");
        String check_code = request.getParameter("check_code");
        String mobile_session = "" + request.getSession().getAttribute("mobile");
        String check_code_session = "" + request.getSession().getAttribute("check_code");
        Long check_code_ex = Long.valueOf((String) request.getSession().getAttribute("check_code_ex"));
        //System.out.println(String.format("手机号：%s;验证码：%s;",mobile,check_code));
        if (check_code_session.equals(check_code) && mobile_session.equals(mobile) && System.currentTimeMillis() < check_code_ex) {
            status = true;
            String sign = Md5.md5Str(check_code_session + check_code_ex);
            request.getSession().setAttribute("sign_session", sign);
            body = sign;
        }
        return getMsg(status, title, body);
    }

    @SystemLog(description = "发送短信验证码")
    @RequestMapping(value = "/findPwd/sendMsgCode.do")
    @ResponseBody
    public Map<String, Object> sendMsgCode(HttpServletRequest request, HttpServletResponse response) {
        boolean status = false;
        String title = "温馨提示";
        String body = "账号不存在，无法操作";
        String teamid = request.getParameter("teamid");
        String mobile = request.getParameter("mobile");
        String check_code = "" + getRandom(6);

        //log.info(String.format("手机号：%s;验证码：%s;组织ID:%s;",mobile,check_code,teamid));

        UserInfo userInfo = userInfoService.findUserInfoByMobilePhoneAndTeamId(mobile, teamid);
        if (userInfo != null) {
            Sms.sendMsg(mobile, String.format("验证码%s，5分钟内有效。", check_code));
            status = true;
            body = "验证码已发送,5分钟内有效,请查收！";
            String check_code_ex = "" + (System.currentTimeMillis() + 1000 * 60 * 5);
            request.getSession().setAttribute("mobile", mobile);
            request.getSession().setAttribute("check_code", check_code);
            request.getSession().setAttribute("check_code_ex", check_code_ex);
            request.getSession().setAttribute("userInfo", userInfo.getUserId());
            log.info(String.format("这里是找回密码时发送验证码,手机号:%s;验证码:%s;UserId:%s;", mobile, check_code, userInfo.getUserId()));
        }
        return getMsg(status, title, body);
    }

    @RequestMapping(value = "/denied.do")
    public String deniedPage(@RequestHeader(value = "X-Requested-With", required = false) String xRequestWith, HttpServletResponse response) {
        if ("XMLHttpRequest".equalsIgnoreCase(xRequestWith)) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return "denied";
    }

//    @RequestMapping(value = "/getNewNotice.do")
//    @ResponseBody
//    public Map<String, Object> getNewNotice() throws Exception{
//    	Map<String, Object> msg = new HashMap<>();
//    	try {
//    		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    		if(principal != null){
//    			UserEntityInfo userEntityInfo = principal.getUserEntityInfo();
//    			//判断自己的身份
//    			//1. 非直营代理商3级包括以下 ：只能接收1，5，7类型的公告
//    			//2. 非直营代理商2级： 只能接收1，5，7，8类型的 公告
//    			//3. 非直营代理商1级：只能接收1，2，5，6类型的公告
//    			//4. 直营代理商3级包括以下：只能接收1,3类型的公告
//    			//5. 直营代理商2级：只能接收1,3类型的公告
//    			//6. 直营代理商1级：只能接收1,2,3,4类型的公告
//    			String receiveType = null;
//    			AgentInfo agentInfo = agentInfoService.selectByPrincipal();
//    			int level = agentInfo.getAgentLevel();
//    			if (principal.getTeamId().equals("100010")) {
//    				//直营
//    				if (level >= 3) {
//    					receiveType = "13";
//    				} else if (level == 2){
//    					receiveType = "12";
//    				} else if (level == 1) {
//    					receiveType = "11";
//    				}
//    			} else if (principal.getTeamId().equals("200010")){
//    				//非直营
//    				if (level >= 3) {
//    					receiveType = "23";
//    				} else if (level == 2){
//    					receiveType = "22";
//    				} else if (level == 1) {
//    					receiveType = "21";
//    				}
//    			}
//    			NoticeInfo notice = noticeInfoService.getNewNoticeByAgent(receiveType);
//    			if(notice !=null){
//    				msg.put("status", true);
//    				msg.put("notice", notice);
//    			} else {
//    				msg.put("status", false);
//    			}
//    		}
//		} catch (Exception e) {
//			msg.put("status", false);
//			log.error("代理商web查询最新公告失败");
//		}
//        return msg;
//    }

    @RequestMapping(value = "/getNewNotice.do")
    @ResponseBody
    public ResponseBean getNewNotice() throws Exception {

        try {
            //判断自己的身份
            // 一级代理商,只能接收 1,2 类型的公告 (11)
            // 非一级代理商,只能接收 1 的公告     (21)

            AgentInfo loginAgentInfo = agentInfoService.selectByPrincipal();
            NoticeInfo searchInfo = new NoticeInfo();
            searchInfo.setOemType(loginAgentInfo.getOemType());
            int level = loginAgentInfo.getAgentLevel();
            searchInfo.setReceiveType(level == 1 ? "11" : "21");
            List<NoticeInfo> noticeInfos = noticeInfoService.selectReceiveNotices(searchInfo, new Page<NoticeInfo>(1, 5));
            if (noticeInfos != null && !noticeInfos.isEmpty()) {
                NoticeInfo firstNotice = noticeInfoService.selectById(noticeInfos.get(0).getNtId() + "");
                if ("11".equals(loginAgentInfo.getAgentType())) {
                	String oemList = firstNotice.getOemList();
                	if (!oemList.contains(loginAgentInfo.getAgentOem())) {
                		firstNotice = null;
                	}
                }
                noticeInfos.set(0, firstNotice);
            }
            return new ResponseBean(noticeInfos);
        } catch (Exception e) {
            return new ResponseBean(e);
        }
    }

    @RequestMapping({"/views/common/navigation.html", "/navigation.do"})
    public String navigation(ModelMap model) {
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Set<String> permits = new HashSet<String>();
            for (GrantedAuthority item : principal.getAuthorities()) {
                permits.add(item.getAuthority());
            }
            System.out.println(permits);
            List<SysMenu> list = sysMenuService.findAllSysMenuAndChildrenList();
            Set<String> excludeSet = new HashSet<>();
            UserEntityInfo userEntityInfo = principal.getUserEntityInfo();
            oemMenuConfig(principal.getOemTypeEnum(), excludeSet);
            if (!"ALL".equals(userEntityInfo.getEntityId())) {
                AgentInfo agent = agentInfoService.selectByagentNo(userEntityInfo.getEntityId());
                AgentInfo oneLevelAgent = agentInfoService.selectByagentNo(agent.getOneLevelId());
                if (agent != null) {
                    if (oneLevelAgent.getCountLevel() != -1 && agent.getAgentLevel() >= oneLevelAgent.getCountLevel()) {
                        excludeSet.add("agent.addAgent");
                        excludeSet.add("agent");    // 整个代理商菜单都隐藏 卓水育和小红确认

                    }
                    boolean  show =    agentInfoService.getFunctionSwitch(agent,"051");
                    if(!show) {
                        excludeSet.add("merchant.addMer");
                    }
                    if (agent.getAgentLevel() != 1) {
//						excludeSet.add("myInfo.account");
//						excludeSet.add("myInfo.shareByDay");//给一级代理商添加显示每日分润报表权限tgh415
                        excludeSet.add("myInfo.preliminaryFreezeQuery");    //预冻结明细查询
                        excludeSet.add("myInfo.unFreezeQuery");                //解冻明细查询
                        excludeSet.add("myInfo.preliminaryAdjustQuery");    //预调账明细查询
                        excludeSet.add("superBank.openTwoAgent");//一级代理商才显
                        excludeSet.add("agent.functionSetting");//一级代理商才显
                        excludeSet.add("creditCardManager");//一级代理商才显 信用卡管家
                        excludeSet.add("safe");//一级代理商才显 账户资金损失险
                        excludeSet.add("perAgent.afterSale");//机构才显示
                        excludeSet.add("merchant.addAcqMer");//收单商户进件
                        excludeSet.add("merchant.queryAcqMer");//收单商户进件列表
                        excludeSet.add("active.happyBackActivityMerchant");//一级代理商才显,欢乐返活跃商户活动查询菜单
                        excludeSet.add("terminal.activityCheck");//一级代理商才显活动考核机具菜单
                        excludeSet.add("creditRepay.profitAdvance");//一级代理商才显分润预调账明细
                    }
                    if (agent.getAgentLevel() != 1 || agent.getTeamId() != 200010) {
                        excludeSet.add("more.addNotice");
                        excludeSet.add("more.sentNotices");
                        excludeSet.add("more.addBanner");
                        excludeSet.add("more.queryBanners");
                    }
                    if (agent.getAgentLevel() != 1 || agent.getIsApprove() != 1) {
                        excludeSet.add("merchant.auditMer");
                    }
                    // 该超级推菜单已经无效,不需要展示 lvsw
                    excludeSet.add("active.superPush");
                    //判断超级推菜单是否需求加添加权限tgh0508
                    if (!agentInfoService.findSuperPush("006")) {
                        excludeSet.add("superPush");
//						excludeSet.add("active.superPush");
//						excludeSet.add("merchant.termianlApplyQuery");
                    }
                    if (agent.getAgentLevel() != 1 && principal.getOemTypeEnum() == OemTypeEnum.PERAGENT) {
                        excludeSet.add("agent");
                    }

                    if("11".equals(agent.getAgentType())){
                        excludeSet.add("merchant.termianlApplyQuery");
                    }
                }
            }
            filterMenu(list, permits, excludeSet);
            model.addAttribute("menus", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "navigation";
    }

    /**
     * 针对不同的oem做不同的配置
     *
     * @param
     * @param excludeSet
     */
    private void oemMenuConfig(OemTypeEnum oemTypeEnum, Set<String> excludeSet) {
    	// 三方查询
    	excludeSet.add("threeData");
        if (oemTypeEnum == null || oemTypeEnum == OemTypeEnum.SQIANBAO || oemTypeEnum == OemTypeEnum.ADMIN) {
            excludeSet.add("perAgent");
            return;
        }
        excludeSet.add("active.activity");          //  欢乐送查询
        //excludeSet.add("merchant.addMer");          //  商户进件
//		excludeSet.add("more");					    //  公告
        excludeSet.add("superBank");                //  超级银行家
        excludeSet.add("creditRepay");              //  信用卡还款
        excludeSet.add("superPush");                //  微创业
        excludeSet.add("redemption");               //  积分兑换
        excludeSet.add("redemptionActive");         //  积分兑换激活版
        excludeSet.add("red");                      //  银行家-红包
        excludeSet.add("perAgent");                 //  人人代理
        switch (oemTypeEnum) {
        	case THREE:
        		excludeSet.remove("threeData");
            break;
            case YLSTCZB:
                excludeSet.add("creditCardManager");
                break;
            case ZHFPLUSPAY: // 中和付plus
                excludeSet.add("creditCardManager");                // 信用卡管家
                break;
            case ZHFPAY:    //中和付,中代宝
                excludeSet.remove("creditRepay");            //  信用卡还款
                break;
            case ZHBPAY:     // 中和宝,代理宝
                excludeSet.remove("creditRepay");            //  信用卡还款
                break;
            case ZHZFPAY:    // 中付支付(中付合伙人)
                excludeSet.remove("creditRepay");            //  信用卡还款
                excludeSet.add("active.merchantIncome");    // 商户收益查询
                break;
            case DIANFU:    // 点付
                excludeSet.remove("creditRepay");            //  信用卡还款
                excludeSet.remove("superPush");            // 微创业
                break;
            case REPAY:        // 信用卡超级还款
                excludeSet.remove("creditRepay");            //  超级还款
                break;
            case ZYFPAY:    // 自由付-自由代
                break;
            case YABPAY:    // 宏博宏宇-安信宝
                break;
            case YPOSPAY:       // 银pos 银惠宝
                excludeSet.add("active.activity");                 //  欢乐送查询
                excludeSet.add("active.superPush");                 //  超级推活动
                excludeSet.add("active.giftsRecord");                 //  赠送记录查询
//                excludeSet.add("active.happyBack");                 //  欢乐返查询
//                excludeSet.add("active.merchantIncome");                 //  商户收益查询
                excludeSet.add("active.activationCode");                 //  激活码管理
                excludeSet.add("active.activityOrderInfoQuery");                 //  购买记录查询
                excludeSet.add("creditCardManager");                 //  信用卡管家
                break;
            case SUPERBANK:    // 超级银行家
                excludeSet.remove("superBank");            //  超级银行家
                excludeSet.remove("red");                //  银行家-红包
                excludeSet.add("more");                        //  公告
                break;
            case REDEMACTIVE:   // 积分兑换激活版
                excludeSet.remove("redemptionActive");        // 积分兑换
               /* excludeSet.add("active");                // 业务活动
                excludeSet.add("activity");                // 业务活动
                excludeSet.add("merchant");                // 商户管理
                excludeSet.add("trade");                // 交易查询
                excludeSet.add("creditCardManager");                // 信用卡管家
                excludeSet.add("tradeByMerchant");                // 商户交易汇总
                excludeSet.add("terminal");                // 机具管理
                excludeSet.add("myInfo.shareByDay");                // 每日分润报表
                excludeSet.add("myInfo.tradeShare");                // 交易分润明细
                excludeSet.add("myInfo.preliminaryFreezeQuery");                // 预冻结明细查询
                excludeSet.add("myInfo.unFreezeQuery");                // 解冻明细查询
                excludeSet.add("myInfo.preliminaryAdjustQuery");                // 预调账明细查询
                excludeSet.add("sys.sysRole");                // 系统角色
                excludeSet.add("agent.functionSetting");//一级代理商才显
*/                break;
            case REDEM:        // 积分兑换
                excludeSet.remove("redemption");        // 积分兑换
              /*  excludeSet.add("active");                // 业务活动
                excludeSet.add("activity");                // 业务活动
                excludeSet.add("merchant");                // 商户管理
                excludeSet.add("agent");                // 下级代理商管理
                excludeSet.add("trade");                // 交易查询
                excludeSet.add("creditCardManager");                // 信用卡管家
                excludeSet.add("tradeByMerchant");                // 商户交易汇总
                excludeSet.add("terminal");                // 机具管理
                excludeSet.add("myInfo.shareByDay");                // 每日分润报表
                excludeSet.add("myInfo.tradeShare");                // 交易分润明细
                excludeSet.add("myInfo.preliminaryFreezeQuery");                // 预冻结明细查询
                excludeSet.add("myInfo.unFreezeQuery");                // 解冻明细查询
                excludeSet.add("myInfo.preliminaryAdjustQuery");                // 预调账明细查询
                excludeSet.add("sys.sysRole");                // 系统角色
                excludeSet.add("agent.functionSetting");//一级代理商才显
*/                break;
            case PERAGENT://人人代理
                excludeSet.add("creditCardManager");
                excludeSet.add("safe");
//                excludeSet.add("sys");
                excludeSet.remove("perAgent");//人人代理
                break;
            default:
                break;
        }
    }

    private void filterMenu(Collection<SysMenu> list, Set<String> permits, Set<String> excludeSet) {
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu menu = it.next();
            if (excludeSet != null && excludeSet.contains(menu.getMenuCode())) {
                it.remove();
                continue;
            }
            if (!permits.contains(menu.getMenuCode())) {
                it.remove();
                continue;
            }
            final List<SysMenu> children = menu.getChildren();
            if (children != null && !children.isEmpty())
                filterMenu(children, permits, excludeSet);
        }
    }

    @RequestMapping(value = "/getImageCode/{type}/{imageCodeId}")
    public void getImageCode(
            @PathVariable String type,
            @PathVariable String imageCodeId,
                             HttpServletResponse response) throws Exception {
        String code = RandomStringUtils.random(4, "0123456789");
        redisService.insertString(String.format("agentWeb2:imageCode:%s:%s", type, imageCodeId), code, 600L);
        BufferedImage randcodeImage = RandomValidateCodeUtils.getRandCodeImage(code);
        ImageIO.write(randcodeImage, "JPEG", response.getOutputStream());
    }
}

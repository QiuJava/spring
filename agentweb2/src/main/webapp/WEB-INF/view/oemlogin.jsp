<%@ page import="cn.eeepay.framework.util.Constants" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.logging.Logger" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
    /**
     * 这里是配置正确的oem登陆页面,
     * 主要是防止变量 Constants.ROUTE_CONFIG 出现配错的情况
     */
    Logger logger = Logger.getLogger("oemlogin.jsp");
    List<String> oemLoginPageList = Arrays.asList(
            "sqianbaologin.jsp",            // 盛钱包
            "yinposlogin.jsp",              // 银pos 银惠宝
            "dianfulogin.jsp",              // 点付
            "repaylogin.jsp",               // 超级还
            "ziyoufulogin.jsp",             // 自由付
            "axblogin.jsp",                 // 宏博宏宇-安信宝
            "superbanklogin.jsp",           // 超级银行家
            "redemlogin.jsp",               // 超级兑
            "redemActivelogin.jsp",         // 超级兑-激活版
            "peragentlogin.jsp",            // 超级盟主
            "ylstczblogin.jsp",              // 移联商通成长版

            "zhonghefupluslogin.jsp",       // 超代宝
            "zhonghebaologin.jsp",          // 中和宝
            "zhonghefulogin.jsp",           // 中和付
            "zhonghezhifulogin.jsp",        // 中和支付
            "threeQueryLogin.jsp",			// 三方查询
            "threeQueryCJMZLogin.jsp"			// 三方查询
    );
    Map<String,String> routeConfigMap = new HashMap<String,String>();
    String routeConfig = Constants.ROUTE_CONFIG;
    System.out.println("===>" + routeConfig);
    if (StringUtils.isNotBlank(routeConfig)){
        String[] split = routeConfig.split(";");
        Pattern pattern = Pattern.compile("([^:]+?):(.*?)");
        for (String temp : split){
            Matcher matcher = pattern.matcher(temp);
            if (matcher.matches()){
                routeConfigMap.put(matcher.group(1), matcher.group(2));
            }
        }
    }
    logger.info(routeConfigMap.toString());
    String hostName = request.getServerName();
    logger.info("主机名:" + hostName);

    String loginPath = routeConfigMap.get(hostName);
    logger.info("配置登陆页面:" + loginPath);
    if (StringUtils.isBlank(loginPath) || !oemLoginPageList.contains(loginPath)){
        loginPath = "sqianbaologin.jsp";
    }
    logger.info("实际登陆页面:" + loginPath);
    request.getRequestDispatcher(loginPath).forward(request,response);
%>
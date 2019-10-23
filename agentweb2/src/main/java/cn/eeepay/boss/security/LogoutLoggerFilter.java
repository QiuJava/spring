package cn.eeepay.boss.security;

import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.AgentOperLogBean;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.AgentOperLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutLoggerFilter extends GenericFilterBean {
    private String logoutUrl;

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutLoggerFilter.class);
    @Autowired
    private AgentInfoService agentInfoService;

    @Resource
    private AgentOperLogService agentOperLogService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            if (requiresLogout(request, response)) {
                AgentInfo agentInfo = agentInfoService.selectByPrincipal();
                if (agentInfo != null){
                    final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                            .getPrincipal();
                    String userInfoId = principal.getUserInfoId();
                    LOGGER.info("用户id {}, 代理商编号 {}, 代理商名称 {} 进行注销操作", userInfoId, agentInfo.getAgentNo(), agentInfo.getAgentName());
//
//                    AgentOperLogBean loginBean = new AgentOperLogBean();
//                    loginBean.setAgentName(agentInfo.getAgentName());
//                    loginBean.setAgentNo(agentInfo.getAgentNo());
//                    loginBean.setMethodDesc("注销操作");
//                    loginBean.setOperIp(getRemoteAddr(request));
//                    agentOperLogService.insertLog(loginBean);
                }
            }
        }finally {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    private String getRemoteAddr(HttpServletRequest request){
        try {
            String xforwardedFor = request.getHeader("x-forwarded-for");
            String xRealIp = request.getHeader("X-Real-IP");
            logger.info("请求头数据: key = x-forwarded-for, value = " + xforwardedFor);
            logger.info("请求头数据: key = X-Real-IP, value = " + xRealIp);
            if (StringUtils.isNotBlank(xforwardedFor)) {
                return xforwardedFor.split(",")[0];
            }
            if (StringUtils.isNotBlank(xRealIp)) {
                return xRealIp;
            }
            return request.getRemoteAddr();
        }catch (Exception e){
            logger.error("getRemoteAddr ==> " + e);
            return "";
        }
    }

    private boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(logoutUrl))
            return false;
        String path = request.getRequestURI();
        return path.contains(logoutUrl);
    }
}

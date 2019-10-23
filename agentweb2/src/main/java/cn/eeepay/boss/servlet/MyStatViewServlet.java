package cn.eeepay.boss.servlet;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kco1989
 * @email kco1989@qq.com
 * @date 2019-06-26 09:35
 */
public class MyStatViewServlet extends StatViewServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.service(request, response);
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith(".html")) {
            response.setContentType("text/html;charset=utf-8");
        }

    }
}

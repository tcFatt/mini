package nusiss.mini.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter{

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest)request;
        HttpServletResponse httpResp = (HttpServletResponse)response;
        HttpSession sess = httpReq.getSession();
        String username = (String)sess.getAttribute("username");
        while ((null == username) || (username.trim().length() <= 0)) {
            httpResp.sendRedirect("/");
            return;
        }
        chain.doFilter(request, response);
    }
   
}

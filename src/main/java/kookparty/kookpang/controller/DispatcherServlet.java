package kookparty.kookpang.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 모든 사용자 요청을 처리할 진입점 Controller의 역할
 */
@WebServlet(urlPatterns = "/front" , loadOnStartup = 1)
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 50
)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Controller> map;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext application = config.getServletContext();
		map = (Map<String, Controller>) application.getAttribute("map");
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key = request.getParameter("key");
		String methodName = request.getParameter("methodName");
		
		if (key==null || key.equals("")) {
			key="recipe";
		}
		
		if (methodName==null || methodName.equals("")) {
			methodName="selectAll";
		}
		
		try {
			Controller con = map.get(key);
			Class<?> clz = con.getClass();
			Method method = clz.getMethod(methodName, HttpServletRequest.class , HttpServletResponse.class);
			
			ModelAndView mv = (ModelAndView)method.invoke(con, request, response);
			
			if(mv.isRedirect()) {
				response.sendRedirect( mv.getViewName() );
			} else {
				request.getRequestDispatcher(mv.getViewName()).forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = (e.getCause() != null && e.getCause().getMessage() != null) 
			                ? e.getCause().getMessage() 
			                : e.getMessage();
			request.setAttribute("errorMsg", errMsg);
			request.getRequestDispatcher("error/error.jsp").forward(request, response);
		}
		
	} //service end
}
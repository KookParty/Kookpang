package kookparty.kookpang.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *  사용자의 모든 요청을 처리할 진입점 Controller (FrontController의 역할)
 */
@WebServlet(urlPatterns = "/ajax" , loadOnStartup = 1)
public class AjaxDispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
     Map<String, Controller> map;
    
 	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext application = config.getServletContext();
		map = (Map<String, Controller>) application.getAttribute("ajaxMap");
	}
   
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		
		String key = request.getParameter("key");
		String methodName = request.getParameter("methodName");
		
		if (key ==null || key.equals("")) {
			key="product";
			methodName="selectAll";
		}
		
		try {
			Controller controller = map.get(key);
			Class<?> clz = controller.getClass();
			
			Method method = clz.getMethod(methodName, HttpServletRequest.class , HttpServletResponse.class);
			
			Object obj = method.invoke(controller, request , response);
			
			
			Gson gson = new Gson();
			String data = gson.toJson(obj);
			System.out.println("data = " + data);
			
			response.getWriter().print(data);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	} // service end
}
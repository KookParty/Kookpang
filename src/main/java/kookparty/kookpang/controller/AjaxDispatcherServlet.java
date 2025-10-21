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
		map = (Map<String, Controller>) application.getAttribute("map");
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
			// 추가: 예외 발생 시 클라이언트에게 에러 JSON 응답
            // e.getCause()를 사용해 실제 비즈니스 예외 메시지를 가져옵니다.
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            String errorMsg = cause.getMessage() != null ? safe(cause.getMessage()) : "알 수 없는 서버 오류";
            
            // HTTP 상태 코드를 BAD_REQUEST(400) 또는 INTERNAL_SERVER_ERROR(500)로 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); 
            
            // Gson을 사용해 에러 객체를 JSON으로 변환하여 응답
            Map<String, Object> errorOut = Map.of("ok", false, "msg", errorMsg);
            Gson gson = new Gson();
            String errorData = gson.toJson(errorOut);
            response.getWriter().print(errorData);
		}
	} // service end
    
    // safe 헬퍼 함수 추가 (UserController에서 가져옴)
    private static String safe(String s){ return (s==null? "서버 오류" : s.replace("\"","\\\"")); 
    }

			
		
	} // service end

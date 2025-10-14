package kookparty.kookpang.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import kookparty.kookpang.controller.Controller;

/**
 * 서버가 시작될때 각 Conroller의 구현객체를 미리 생성해서 Map에 저장한후
 * ServletContext영역에 map을 저장한다.
 *
 */
@WebListener
public class HandlerMappingListener implements ServletContextListener {
	
    public void contextInitialized(ServletContextEvent e)  { 
    	// 로딩
    	ServletContext application = e.getServletContext();
    	ResourceBundle rb = ResourceBundle.getBundle("actionMapping");

    	try {
    		
    		Map<String, Controller> map = new HashMap<String, Controller>();
    		
	    	for(String key :  rb.keySet()){
	    		String value = rb.getString(key);
	    		Class<?> className = Class.forName(value);
	    		Controller con = (Controller)className.getDeclaredConstructor().newInstance();
	    		map.put(key, con);
	    		
	    		System.out.println(key +" = " + con);
	    	}
	    	
	    	//현재 프로젝트의 모든 영역에서 map을 사용할수 있도록 ServletContext영역에 저정한다.
	    	application.setAttribute("map", map);
	    	application.setAttribute("path",  application.getContextPath() ); //${path}
	    	
    	} catch (Exception ex) {
			ex.printStackTrace();
		}
    	
    }//methodEnd
	
}//classEnd

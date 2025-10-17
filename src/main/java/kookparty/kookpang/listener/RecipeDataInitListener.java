package kookparty.kookpang.listener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.infra.RecipeApiClient;
import kookparty.kookpang.service.RecipeService;
import kookparty.kookpang.service.RecipeServiceImpl;
import kookparty.kookpang.util.DbUtil;

/**
 * 레시피 데이터를 API로부터 받아와 초기화하는 리스너
 */
@WebListener
public class RecipeDataInitListener implements ServletContextListener {


	/**
     * recipes 테이블이 체크 후 데이터 가져오기
     */
    public void contextInitialized(ServletContextEvent sce)  { 
	    
    	try {
    		Connection con = DbUtil.getConnection();
			System.out.println("con = " + con);
			
			RecipeService service = RecipeServiceImpl.getInstance();
			List<RecipeDTO> recipes = RecipeApiClient.fetchRecipes();
			
			
			for (RecipeDTO recipe : recipes) {
				System.out.println(recipe);
				service.insertRecipe(recipe);
				
				// 재료ingredients 저장
				// TODO
			
				// 조리법steps 저장
				// TODO
			}
			System.out.println("레시피 데이터 불러오기 완료");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}

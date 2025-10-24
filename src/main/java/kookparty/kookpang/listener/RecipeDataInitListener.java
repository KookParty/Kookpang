package kookparty.kookpang.listener;

import java.util.List;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.infra.RecipeApiClient;
import kookparty.kookpang.service.RecipeService;
import kookparty.kookpang.service.RecipeServiceImpl;

/**
 * 레시피 데이터를 API로부터 받아와 초기화하는 리스너
 */
@WebListener
public class RecipeDataInitListener implements ServletContextListener {
	/**
     * recipes 테이블에 레코드 있는지 체크 후 데이터 가져오기
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	try {
    		RecipeService service = RecipeServiceImpl.getInstance();
    		
			if (service.selectByOptions("", "base", "", 0).size() != 0) {
				// recipes 테이블에 레코드가 있을 시
				System.out.println("recipes 데이터 확인됨");
				System.out.println("recipes size = " + service.selectByOptions("", "base", "", 0).size());
			} else {
				// recipes 테이블에 레코드가 하나도 없을 시
				// API로부터 레시피 데이터 가져오기
				List<RecipeDTO> recipes = RecipeApiClient.fetchRecipes();
				
				for (RecipeDTO recipe : recipes) {
					//System.out.println(recipe);
					service.insertRecipe(recipe);
				}
				
				System.out.println("레시피 데이터 불러오기 완료");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}

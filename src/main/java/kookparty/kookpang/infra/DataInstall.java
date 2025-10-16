package kookparty.kookpang.infra;

import java.util.List;

import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.service.RecipeService;
import kookparty.kookpang.service.RecipeServiceImpl;

/**
 * RecipeApiClient로부터 데이터를 받아와 등록하는 역할
 * 초기 데이터 구축용/한 번만 실행
 */
public class DataInstall {

	public static void main(String[] args) {
		try {
			RecipeService service = RecipeServiceImpl.getInstance();
			List<RecipeDTO> recipes = RecipeApiClient.fetchRecipes();
			
			/*
			for (RecipeDTO recipe : recipes) {
				service.insertRecipe(recipe);
				System.out.println(recipe);
			}
			System.out.println("레시피 데이터 불러오기 완료");
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}

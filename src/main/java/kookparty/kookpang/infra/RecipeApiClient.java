package kookparty.kookpang.infra;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kookparty.kookpang.common.RecipeType;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.service.RecipeService;
import kookparty.kookpang.service.RecipeServiceImpl;

/**
 * 레시피 데이터를 가져오기 위한 클래스
 */
public class RecipeApiClient {
	private static String key = "019742b6694d4e8ea36e";
	private static String start = "1";
	private static String end = "5";
	
	/**
	 * 레시피 데이터를 Open API 사용해서 가져온다.
	 * JSON 응답을 파싱해 List<RecipeDTO> 형태로 변환해서 리턴한다.
	 */
	public static List<RecipeDTO> fetchRecipes() throws Exception {
		List<RecipeDTO> recipes = new ArrayList<>();
		
		String url = "http://openapi.foodsafetykorea.go.kr/api/"+key+"/COOKRCP01/json/"+start+"/"+end;
		
		// gson 라이브러리 사용
		// json 객체로 변환 (json = {"COOKRCP01": {...}} 로 들어감)
		JsonObject json = JsonParser.parseReader(new InputStreamReader(new URL(url).openStream())).getAsJsonObject();
		// COOKRCP01 키에 해당하는 객체 안의 row에 해당하는 객체 배열 꺼냄 (실제 레시피 데이터)
		JsonArray rows = json.getAsJsonObject("COOKRCP01").getAsJsonArray("row");
		
		Gson gson = new Gson();
		// json 객체 -> recipeDTO 매칭해서 DB에 넣기
		for (JsonElement e: rows) {
			// RecipeDTO에서 키와 멤버필드 매핑해놔서 바로 넣어도 됨
			RecipeDTO recipeDTO = gson.fromJson(e, RecipeDTO.class);
			recipeDTO.setUserId(1); // TODO: admin user id 으로 설정
			recipeDTO.setDescription(recipeDTO.getTitle()); // TODO: 일단 제목과 동일하게 설정
			recipeDTO.setRecipeType(RecipeType.BASE);
			recipes.add(recipeDTO);
		}
		
		return recipes;
	}
}

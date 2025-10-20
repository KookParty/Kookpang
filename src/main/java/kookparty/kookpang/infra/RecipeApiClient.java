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
import kookparty.kookpang.dto.IngredientDTO;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.dto.StepDTO;

/**
 * 레시피 데이터를 가져오기 위한 클래스
 */
public class RecipeApiClient {
	private static String key = "019742b6694d4e8ea36e";
	private static String start = "1";
	private static String end = "50";
	
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
			JsonObject obj = e.getAsJsonObject();
			// RecipeDTO에서 키와 멤버필드 매핑해놔서 바로 넣어도 됨
			RecipeDTO recipeDTO = gson.fromJson(obj, RecipeDTO.class);
			
			recipeDTO.setUserId(1); // (임시) admin user id 으로 설정
			recipeDTO.setRecipeType(RecipeType.BASE);
			//System.out.println("fetchRecipes() recipeDTO fin");
			
			/** 재료(ingredients) 저장 */
			List<IngredientDTO> ingredients = new ArrayList<>();
			if (obj.has("RCP_PARTS_DTLS")) {
				String parts = obj.get("RCP_PARTS_DTLS").getAsString();
				
				// ● 기준으로 섹션 분리
				String[] sections = parts.split("●");
				for (String section : sections) {
					if (section.isEmpty()) continue;
					
					String sectionName, items;
					// : 있는 경우 (소분류 : 재료 나열)
					if (section.contains(" : ")) {
						String[] splitSection = section.split(" : ");
						sectionName = splitSection[0];
						items = splitSection[1];
					}
					// : 없는 경우 (일반)
					else {
						// 첫 줄은 요리명, 나머지는 재료
						String[] lines = section.split("\n", 2);
						if (lines.length == 2) {
							sectionName = lines[0];
							items = lines[1];
						} else {
							// 한 줄만 있다면 전부 재료
							items = section;
						}
					}
					
					// 재료 수량 파싱 "," 또는 줄바꿈으로 나누기
					String[] itemArr = items.split(",\\s|\\n");
					for (String item : itemArr) {
						if (item.isEmpty()) continue;
						
						String[] detail = item.split("\\(|\\)");	// 재료 (수량) 형식 (재료에 띄어쓰기 포함 미포함 둘 다 존재)
						if (detail.length <= 1) continue;	// 수량이 없는 경우: 재료로 언급X
						IngredientDTO ingredientDTO = new IngredientDTO(recipeDTO.getRecipeId(), detail[0], detail[1]);
						//ingredientDTO.setProductId(0); // 식재료 id 매핑 나중에
						ingredients.add(ingredientDTO);
					}
				}
			}
			recipeDTO.setIngredients(ingredients);
			//System.out.println("fetchRecipes() ingredients fin");
			
			/** 조리법(steps) 저장 */
			List<StepDTO> steps = new ArrayList<>();
			for (int i = 1; i <= 20; i++) {
				String manualKey = String.format("MANUAL%02d", i);
				String imgKey = String.format("MANUAL_IMG%02d", i);
			
				// 각 key키 존재 여부 확인하고 키에 해당하는 값
				if (obj.has(manualKey)) {
					String manual = obj.get(manualKey).getAsString();
					String img = obj.get(imgKey).getAsString();
					if (manual.isEmpty() && img.isEmpty()) continue;	// API에 MANUAL이 01부터 시작하지 않는 경우 존재
					manual = manual.replace("\n", " ");
					steps.add(new StepDTO(manual, img));
				}
			}
			recipeDTO.setSteps(steps);
			//System.out.println("fetchRecipes() steps fin");
			 
			recipes.add(recipeDTO);
		}
		
		return recipes;
	}
}

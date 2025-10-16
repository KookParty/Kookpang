package kookparty.kookpang.infra;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kookparty.kookpang.dto.RecipeDTO;

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
		String apiUrl = "http://openapi.foodsafetykorea.go.kr/api/"+key+"/COOKRCP01/json/"+start+"/"+end;
		
		// URL 객체 생성
		URL url = new URL(apiUrl);
		
		// URL과 연결하는 객체 생성
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		// 요청 옵션
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-type", "application/json");
		
		// 데이터 읽어오기
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		// StringBuilder 객체에 담기
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		
		// 리소스 해제
		br.close();
		con.disconnect();
		
		System.out.println(sb.toString());
		
		return null;
	}
}

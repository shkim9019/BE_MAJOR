package com.sist.booking.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WeatherController implements Initializable   {
	@FXML Text rainfall;	//강수확률
	@FXML Text humidity;	//습도
	@FXML Text lowest;		//최저기온
	@FXML Text highest;		//최고기온
	@FXML Text windDir;		//풍향
	@FXML Text windSp;		//풍속
	@FXML ImageView Logo;
	@FXML Button back;
	
	
	// 상단 로고 클릭시 메인페이지로 이동
		public void MoveToMainPg(MouseEvent event) {
			try {

				Parent root = FXMLLoader.load(getClass().getResource("/com/sist/mainpage/view/MainPage.fxml"));
				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) Logo.getScene().getWindow();
				primaryStage.setScene(scene);
				primaryStage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	//돌아가기 버튼 클릭시 시설목록페이지로 돌아가기
		public void ReturnToPage(ActionEvent event) {
			try {

				Parent root = FXMLLoader.load(getClass().getResource("/com/sist/booking/view/Booking02_list.fxml"));
				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) back.getScene().getWindow();
				primaryStage.setScene(scene);
				primaryStage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//어제 날짜 구하기
		Calendar c1 = new GregorianCalendar();
		c1.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷 
		String yesterDay = sdf.format(c1.getTime()).replace("-", ""); 
		
		String apiUrl = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData";
		// 홈페이지에서 받은 키
		String serviceKey = "9R%2BGo78xOlODxssyEDAEkWRo6pdVBAOpYl%2Fdc3p1KKiDtSVnvJhiLjFmT3Dlr8YPTJwjtM12%2F8Yk8KBZgVyQwg%3D%3D";
		String nx = "60";	//위도
		String ny = "127";	//경도
		String baseDate = yesterDay;	//조회하고싶은 날짜 ->어제 날짜 입력해 주면 됨(제공시간과 맞추어서)
		String baseTime = "2300";	//API 제공 시간을 입력
		String type = "json";	//타입 xml, json 등등 ..
		String numOfRows = "153";	//한 페이지 결과 수 
		
		//전날 23시 부터 153개의 데이터를 조회하면 오늘과 내일의 날씨를 알 수 있음
		
		
		StringBuilder urlBuilder = new StringBuilder(apiUrl);
		try {
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey);
		urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
		urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
		urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
		urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
		urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));	/* 타입 */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));	/* 한 페이지 결과 수 */
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		URL url;
		try {
				url = new URL(urlBuilder.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				System.out.println(url);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-type", "application/json");
				System.out.println("Response code: " + conn.getResponseCode());
				BufferedReader rd;
				
				if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				} else {
					rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				}
				
				StringBuilder sb = new StringBuilder();
				String line;
				
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				
				rd.close();
				conn.disconnect();
				String result= sb.toString();
				System.out.println(result);
				
				// Json parser를 만들어 만들어진 문자열 데이터를 객체화 
				JSONParser parser = new JSONParser(); 
				JSONObject obj = (JSONObject) parser.parse(result); 
				// response 키를 가지고 데이터를 파싱 
				JSONObject parse_response = (JSONObject) obj.get("response"); 
				// response 로 부터 body 찾기
				JSONObject parse_body = (JSONObject) parse_response.get("body"); 
				// body 로 부터 items 찾기 
				JSONObject parse_items = (JSONObject) parse_body.get("items");
		
				// items로 부터 itemlist 를 받기 
				JSONArray parse_item = (JSONArray) parse_items.get("item");
				String category;
				JSONObject weather; 	// parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용
				// 카테고리와 값만 받아오기
				String day="";
				String time="";
				for(int i = 0 ; i < parse_item.size(); i++) {
					weather = (JSONObject) parse_item.get(i);
					Object fcstValue = weather.get("fcstValue");
					Object fcstDate = weather.get("fcstDate");
					Object fcstTime = weather.get("fcstTime");
					//double형으로 받고싶으면 아래내용 주석 해제
					//double fcstValue = Double.parseDouble(weather.get("fcstValue").toString());
					category = (String)weather.get("category"); 
					// 출력
					if(!day.equals(fcstDate.toString())) {
						day=fcstDate.toString();
					}
					if(!time.equals(fcstTime.toString())) {
						time=fcstTime.toString();
						System.out.println(day+"  "+time);
					}
	//				System.out.print("\tcategory : "+ category);
	//				System.out.print(", fcst_Value : "+ fcstValue);
	//				System.out.print(", fcstDate : "+ fcstDate);
	//				System.out.println(", fcstTime : "+ fcstTime);
					
					if(category.contains("POP")) {
						rainfall.setText(fcstValue+"%");
					}
					if(category.contains("REH")) {
						humidity.setText(fcstValue+"%");
					}
					if(category.contains("TMN")) {
						lowest.setText(fcstValue+"도");
					}
					if(category.contains("TMX")) {
						highest.setText(fcstValue+"도");
					}
					if(category.contains("VEC")) {
						windDir.setText(fcstValue+"m/s");
					}
					if(category.contains("WSD")) {
						windSp.setText(fcstValue+"");
					}
					
					
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/*
		 * 항목값	항목명	단위
		 * POP	강수확률	 %
		 * PTY	강수형태	코드값
		 * R06	6시간 강수량	범주 (1 mm)
		 * REH	습도	 %
		 * S06	6시간 신적설	범주(1 cm)
		 * SKY	하늘상태	코드값
		 * T3H	3시간 기온	 ℃
		 * TMN	아침 최저기온	 ℃
		 * TMX	낮 최고기온	 ℃
		 * UUU	풍속(동서성분)	 m/s
		 * VVV	풍속(남북성분)	 m/s
		 * WAV	파고	 M
		 * VEC	풍향	 m/s
		 * WSD	풍속	1

		 */
			
			
			
	}
}
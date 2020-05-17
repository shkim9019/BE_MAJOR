package com.sist.mypage.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.sist.booking.dao.BookingDAO;
import com.sist.booking.dao.BookingVO;
import com.sist.dto.workdiv.BE_Major;
import com.sist.facility.controller.MyBrowser;
import com.sist.hr.member.domain.MemberVO;
import com.sist.mypage.dao.BookingSP;
import com.sun.javafx.collections.SetListenerHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class MyPage02_BookingListController implements Initializable {

	@FXML
	Button bookinglistbtn;
	@FXML
	Button userinfobtn;
	@FXML
	Button bookinghistorybtn;
	@FXML
	TableView<BookingSP> tableView;
	@FXML
	ImageView imageView;
	@FXML
	ImageView Logo;
	@FXML
	Button logOut;
	@FXML
	Button main;
	@FXML
	MenuButton comboBox;
	@FXML
	MenuItem hyper01, hyper02;

	public static List<BookingVO> bookinglist;
	public static final String FILE_PATH = "csv/booking.csv";

	public static String userId; // 회원 아이디
	public static String userPw; // 회원 비밀번호
	public static String userNm; // 회원 이름
	public static String userMail; // 회원 이메일
	public static String userAdd; // 회원 주소
	public static String userCellphone; // 회원 번호
	public static String userAuth; // 회원모드(이용자,관리

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LoginController.loginStatus = BE_Major.LOGIN;
		
		// 로고 이미지 클릭시 메인페이지로 이동
		Logo.setOnMouseClicked(event -> MoveToMainPg(event));
		// 로그아웃 후 메인페이지 이동
		logOut.setOnAction(event -> logOutBtn(event));

		// 메인페이지버튼 클릭시 메인페이지로 이동(로그인상태)
		main.setOnAction(event -> myPgToMain(event));
	   

		BookingDAO bookingdao = new BookingDAO();
		bookinglist = new ArrayList<BookingVO>();
		bookinglist = readFile(FILE_PATH);

		System.out.println("BE_Major:" + BE_Major.vo);

		// BookingVO id와 로그인DB id 같으면 booking.csv에 있는 facilityName, BookingDate 데이터 가져오기

		List<BookingSP> list = new ArrayList<BookingSP>();
		for (int i = 0; i < bookingdao.booking.size(); i++) {
			BookingVO vsVO = bookingdao.booking.get(i);
			// 아이디비교
			if (vsVO.getUserId().equals(LoginController.userId)) {
				// list.add(0, vsVO.getFacilityName());
				// list.add(vsVO.getFacilityName(),vsVO.getBookingDate());
				list.add(new BookingSP(vsVO.getFacilityName(), vsVO.getBookingDate(), vsVO.getUserName()));

				// imageView.setImage(vsVO.getImagePath());
			}
		}

		ObservableList phoneList = FXCollections.observableList(list);

		// data<-> Table Column연결
		TableColumn tcSmartPhone = tableView.getColumns().get(0); // column 배열0부터 시작
		tcSmartPhone.setCellValueFactory(new PropertyValueFactory("facilityName"));
		tcSmartPhone.setStyle("-fx-alignment: CENTER");

		TableColumn tcbookingdate = tableView.getColumns().get(1);
		tcbookingdate.setCellValueFactory(new PropertyValueFactory("BookingDate"));
		tcbookingdate.setStyle("-fx-alignment: CENTER");

		TableColumn tcImage = tableView.getColumns().get(2);
		tcImage.setCellValueFactory(new PropertyValueFactory("Image"));
		tcImage.setStyle("-fx-alignment: CENTER");

		tableView.setItems(phoneList);

	}

	public List<BookingVO> readFile(String filePath) {
		List<BookingVO> facilityData = new ArrayList<BookingVO>();
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				// SEO001,서울특별시,쌍용종합운동장,송파구 마천동 산 1-5,20191230,false,user1
				String[] dataArray = line.split(",");
				BookingVO vo = new BookingVO(dataArray[0], dataArray[1], dataArray[2], dataArray[3], dataArray[4],
						Boolean.valueOf(dataArray[5]), dataArray[6], dataArray[7], dataArray[8], dataArray[9],
						dataArray[10]);
				facilityData.add(vo);

			} // --while

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // --if
		}

		return facilityData;
	}

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

	// 회원정보 버튼 클릭 시 해당페이지로 이동
	public void userinfoAction(ActionEvent event) throws Exception {

		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/com/sist/mypage/view/MyPage01_UserInfo.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		Stage main = (Stage) userinfobtn.getScene().getWindow();
		main.close();

	}

	// 나의 예약내역 버튼 클릭 시 해당페이지로 이동
	public void bookinglistAction(ActionEvent event) throws Exception {

		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/com/sist/mypage/view/MyPage02_BookingList.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		Stage main = (Stage) bookinglistbtn.getScene().getWindow();
		main.close();

	}
	
	 // 상단 메인페이지버튼 클릭시 메인페이지로 이동(로그인상태)
	   public void myPgToMain(ActionEvent event) {
		   Parent myPageLoad;
			try {
				myPageLoad = FXMLLoader.load(getClass().getResource("/com/sist/mainpage/view/MainPage.fxml"));
				Scene scene = new Scene(myPageLoad);
				Stage primaryStage = (Stage) main.getScene().getWindow();
				primaryStage.setScene(scene);
				primaryStage.show();
				} catch (IOException e) {
					e.printStackTrace();
		   }
		  
	   }
	   
	// 로그아웃 버튼 클릭시 로그아웃 된 후 메인페이지로 이동
	   public void logOutBtn(ActionEvent event) {
		try {
			LoginController.loginStatus = 0;
			Parent myPageLoad = FXMLLoader.load(getClass().getResource("/com/sist/mainpage/view/MainPage.fxml"));
			Scene scene = new Scene(myPageLoad);
			Stage primaryStage = (Stage) logOut.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
	   }
	  }
	   // 상단 '관련사이트'클릭시 해당 사이트 팝업창 띄우기
   	public void Hyperlink01(ActionEvent event) {

   		WebView web01 = new WebView();
   		WebEngine engine = web01.getEngine();

   		MyBrowser mybrowser = new MyBrowser();

   		Popup pop = new Popup();
   		Stage stage = (Stage) comboBox.getScene().getWindow();
   		// pop.setWidth(1200);
   		// pop.setHeight(800);
   		pop.getContent().add(mybrowser);
   		pop.setAutoHide(true);
   		pop.show(stage);

   	}
   	// 상단 '관련사이트'클릭시 해당 사이트 팝업창 띄우기
   	public void Hyperlink02(ActionEvent event) {

   		WebView web01 = new WebView();
   		WebEngine engine = web01.getEngine();

   		MyBrowser mybrowser = new MyBrowser();

   		Popup pop = new Popup();
   		Stage stage = (Stage) comboBox.getScene().getWindow();

   		pop.getContent().add(mybrowser);
   		pop.setAutoHide(true);
   		pop.show(stage);

   	}

}

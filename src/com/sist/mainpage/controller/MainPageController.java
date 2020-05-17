package com.sist.mainpage.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.sist.dto.workdiv.BE_Major;
import com.sist.mypage.controller.LoginController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainPageController implements Initializable {

	@FXML private Button FacilityInfoPgBtn;
	@FXML private Button BookingPgBtn;
	@FXML private Button LoginPgBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(LoginController.loginStatus==BE_Major.LOGIN) {//로그인상태
			LoginPgBtn.setText("로그아웃");
		}else {
			LoginPgBtn.setText("로그인");
		}
		FacilityInfoPgBtn.setOnAction(event -> MoveToFacilityPg(event));
		BookingPgBtn.setOnAction(event -> MoveToBookingPg(event));
		LoginPgBtn.setOnAction(event -> MoveToLoginPg(event));

	}

	//'시설목록' 클릭시 시설목록 페이지로 이동
	public void MoveToFacilityPg(ActionEvent event) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("/com/sist/facility/view/Facility01_List.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) FacilityInfoPgBtn.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.show();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//'예약하기' 클릭시 예약하기 페이지로 이동
	public void MoveToBookingPg(ActionEvent event) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("/com/sist/booking/view/Booking02_list.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) FacilityInfoPgBtn.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.show();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//'로그인/로그아웃' 클릭시 로그인/메인 페이지로 이동
	public void MoveToLoginPg(ActionEvent event) {
		try {
			if(LoginController.loginStatus!=BE_Major.LOGIN) {//로그아웃 상태(로그인버튼)
				Parent root = FXMLLoader.load(getClass().getResource("/com/sist/mypage/view/LoginPage.fxml"));
				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) LoginPgBtn.getScene().getWindow();
				primaryStage.setScene(scene);
				primaryStage.show();
				
			}else {//로그인 상태(로그아웃 버튼)
				LoginController.loginStatus=0;
				Parent root = FXMLLoader.load(getClass().getResource("/com/sist/mainpage/view/MainPage.fxml"));
				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) LoginPgBtn.getScene().getWindow();
				primaryStage.setScene(scene);
				primaryStage.show();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


}





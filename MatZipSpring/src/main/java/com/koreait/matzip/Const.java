package com.koreait.matzip;

public class Const {
	public static final String TEMPLATE = "template";
	public static final String VIEW = "view";
	public static final String TITLE = "title";
		
	// 세션값으로 사용될 로그인 유저
	public static final String LOGIN_USER = "loginUser";
	
	public static final int SUCCESS = 1;	// 로그인성공 (참고 : UserService)
	public static final int NO_ID = 2;	// 아이디 없을시 2번값 반환(참고 : UserService)
	public static final int NO_PW = 3; // 비번 없을시 3번값 반환(참고 UserService)
	
}

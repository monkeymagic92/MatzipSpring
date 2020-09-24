package com.koreait.matzip;

public class Const {
	
	public static String realPath = null;	// 최초 realPath 값을 const로 박아줄거임 앞으로 이것만 쓸거
											// static이기때문에 계속 상단에 상주하고있을꺼니까 
	
	public static final String TEMPLATE = "template";
	public static final String VIEW = "view";
	public static final String TITLE = "title";
		
	// 세션값으로 사용될 로그인 유저
	public static final String LOGIN_USER = "loginUser";
	
	public static final int FAIL = 0;
	public static final int SUCCESS = 1;	// 로그인성공 (참고 : UserService)
	public static final int NO_ID = 2;	// 아이디 없을시 2번값 반환(참고 : UserService)
	public static final int NO_PW = 3; // 비번 없을시 3번값 반환(참고 UserService)
	
	
	
}

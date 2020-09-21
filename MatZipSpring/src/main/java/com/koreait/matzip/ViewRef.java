package com.koreait.matzip;

public class ViewRef {
	public static final String URI_USER = "user";
	public static final String URI_RESTAURANT = "restaurant";
	
	//   주소구분할때 / 2개가 겹쳐도 이클립스가 알아서 1개로 인식해줌(지금 servlet.context 쪽 있는데 일부로 테스트용으로 여기에도 / 넣어준거임) 
	public static final String TEMP_DEFAULT = "/template/default";
	public static final String TEMP_MENU_TEMP = "/template/menuTemp"; //상위, 하위
}

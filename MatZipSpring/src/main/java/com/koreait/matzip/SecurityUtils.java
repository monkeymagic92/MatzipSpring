package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import com.koreait.matzip.user.model.UserVO;

public class SecurityUtils {
	
	
	public static int getLoginUserPk(HttpServletRequest request) {
		return getLoginUserPk(request.getSession());
	}
	
	// 세션이 박힌 로그인유저 pk값 반환 
	public static int getLoginUserPk(HttpSession hs) {
		UserVO loginUser = (UserVO)hs.getAttribute(Const.LOGIN_USER);
		return loginUser == null ? 0 : loginUser.getI_user();
		
	}
	
	// 세션키값 : loginUser 에 담긴 값을 반환하는 메소드
	public static UserVO getLoginUser(HttpServletRequest request) {
		HttpSession hs = request.getSession();
		return (UserVO)hs.getAttribute(Const.LOGIN_USER);
	}
	
	// 
	public static boolean isLogout(HttpServletRequest request) {
		HttpSession hs = request.getSession();
		return getLoginUser(request) == null;
		
	}

	public static String generateSalt() {
		return BCrypt.gensalt();
	}
	
	public static String getEncrypt(String pw, String salt) {
		return BCrypt.hashpw(pw, salt);
	}
	

}

package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


// HandlerInterceptorAdapter (얘는 스프링자체적으로 있는 내장객체)
public class LoginCheckInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String uri = request.getRequestURI();
		String[] uriArr = uri.split("/");
		
		if(uriArr[1].equals("res")) { // resource(js, css, img) 그냥 통과 	 
			return true;
		} else if (uriArr.length < 3) { // 주소가 이상하면 return 되면서 바로 제일하단 response.sendRedirect 문구가 실행됨
										// 주소가 3차가 안되는순간 걍 false !
			return false;
		}
		
		System.out.println("! ! !인 터 셉 터 잘 되 는 지 확 인 ! ! !");		
		boolean isLogout = SecurityUtils.isLogout(request);		
		
		switch(uriArr[1]) {
		case ViewRef.URI_USER:// 1차 주소값이 user라면 무조건 로그인이 되어있어야 함
			switch(uriArr[2]) {
			case "login": case "join": // 로그인이 되어있다면 login,join 페이지못들어가고 rest/map 으로 가겠다
				if(!isLogout) { // 로그인 되어있는 상태  (!가 붙어있으니, 위에 isLogout 변수참고)
					response.sendRedirect("/rest/map");
					return false;
				}
			}
			
		case ViewRef.URI_REST: // 1차 주소값이 rest 라면... (위에글읽어라)
			switch(uriArr[2]) {
			case "restReg":
				if(isLogout) { // 로그아웃 상태
					response.sendRedirect("/user/login");
					return false;
				}
			}
		}
		
		return true;  // true면 주소값 그대로감  false 면 막아버림 
	}
}

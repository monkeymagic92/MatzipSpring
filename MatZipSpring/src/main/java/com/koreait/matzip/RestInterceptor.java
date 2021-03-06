package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.koreait.matzip.rest.RestMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

public class RestInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private RestMapper mapper;
	
	
	// preHandel은 Controller 가기전 존재하는 녀석
	// true면 쭉~ 다이렉트 진행  / false면 딱 잡히가꼬 끝남
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {	
		System.out.println("rest - interceptor");
		
		String uri = request.getRequestURI();
		System.out.println("uri : " + uri);
		String[] uriArr = uri.split("/");
		
		String[] checkKeywords = {"del", "Del", "upd", "Upd"};
		for(String keyword: checkKeywords) {
			if(uriArr[2].contains(keyword)) {
				int i_rest = CommonUtils.getIntParameter("i_rest", request);
				if(i_rest == 0) {
					return false;
				}
				
				int i_user = SecurityUtils.getLoginUserPk(request);  // 로그인한 사람의 i_user값 가져오기
				
				boolean result = _authSuccess(i_rest, i_user);
				System.out.println("=== auth result : " + result);
				return result;
			}
		}
		return true;
	}
	
	private boolean _authSuccess(int i_rest, int i_user) {
		RestPARAM param = new RestPARAM();
		param.setI_rest(i_rest);
		
		RestDMI dbResult = mapper.selRest(param);
		if(dbResult == null || dbResult.getI_user() != i_user) {
			return false;
		}
		
		return true;
	}
}
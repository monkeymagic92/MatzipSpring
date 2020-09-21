package com.koreait.matzip;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// @Controller 가 붙어야지 @requestMapping을 사용할수 있다

@Controller // 주소값이랑 매핑할수 있는 친구다 ( 컨트롤러는 애노테이션 Controller 라고 주기)
@RequestMapping(value = "/hehe")	// 이렇게할경우 페이지에 접근할때 localhost:8089/hehe/hello 를 해야지 첫번째 hello 메소드 jsp파일에 접근할수 있음
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET) // 주소 localhost:8089/hello 로하겠다  즉, src-main-webinf-views-  hello.jsp 로 보내겠다는 뜻 
	public String hello(HttpServletRequest request, Model model) { // 예전처럼 HttpServletRequest request 해도됨(오리지날버전 request ,  스프링버전 Model.addAttribute
		model.addAttribute("myName","용가리"); 	// model로 했을때 리퀘스트하는법
		request.setAttribute("age",29);	// request로 했을때 리퀘스트 하는법 Spring에서 request를 안쓰는이유는 굳이 무거운메소드를 쓸필요없다 왜냐? getParameter를 안하기 때문에  
		return "hello";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)	// MatZip할때 핸들러맵퍼 라고 보면됨 (컨트롤러 있는 페이지에 Requestmapping 애노테이션 달아야됨 )
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);	
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		model.addAttribute("aa","aa");
		model.addAttribute("serverTime", formattedDate );	// request.setAttribute("",n) 이라고 보면됨  ( views - home.jsp 파일에 값이 넘어갔음 ) 이제는 addAttribute 로 쓰면됨 (spring.ver)
		
		return "home";	// src - main - web-inf - views 안에 home.jsp 를 열꺼임
	}
	
}

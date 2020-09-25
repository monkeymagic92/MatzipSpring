package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/") // 기본index페이지에서 바로 주소값경로를 담고 갈수있게끔 
						// 지도경로 안거치고 그냥가면 파일경로가 안담김
	public String index(HttpServletRequest req) {		
		if(Const.realPath == null) {
			Const.realPath = req.getServletContext().getRealPath("");
		}
		System.out.println("root!!");
		return "redirect:/rest/map";
	}
	 
}

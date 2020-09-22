package com.koreait.matzip.rest;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

@Controller
@RequestMapping("/rest") // 쉽게생각해서 예전에 matzip 버전할때 맵퍼 swich문 1차로 rest를 하겠다란 뜻
public class RestController {
	
	@Autowired // 9.21 이제는 왜 이거쓰는지 알겠지 ? (난 내가 이해하고 내스스로 적었으니 까먹지마라)
	private RestService service;
	
	
	@RequestMapping(value="/map") // "/map" 이거만 할경우 굳이 value 안해도됨 value넣으면 그뒤에 method = RequestMethod.GET
								 // value키워드 쓸때는 method = 어쩌고 기능 사용할때 쓰는것
	public String restMap(Model model) {
		model.addAttribute(Const.TITLE,"지도보기");
		model.addAttribute(Const.VIEW,"rest/restMap"); // 그안에 지도 jsp가 들어감 ( 메모장참고 )
		
		return ViewRef.TEMP_MENU_TEMP;	// header, footer 부분에 주소값 들어감
	}
	
	
	// AJAX관련 
	// produces="application/json; charset=UTF-8"    (Json파일에서 UTF-8설정하는 코드)
													// 리턴할떄  	cahrset=UTF-8 로 인코딩 해주자! 란것
	@RequestMapping(value="/ajaxGetList", produces="application/json; charset=UTF-8")
	@ResponseBody 
	public List<RestDMI> ajaxGetList(RestPARAM param) {
		System.out.println("restMap.jsp ajax(get)으로 보낸 값 제대로 받는지 확인 ");
		System.out.println("sw_lat : " + param.getSw_lat());
		System.out.println("sw_lng : " + param.getSw_lng());
		System.out.println("ne_lat : " + param.getNe_lat());
		System.out.println("ne_lng : " + param.getNe_lng());
		System.out.println("--------------------------------");
		
		return service.selRestList(param);
	}
	
	
	
	// 가게등록 페이지 메소드 (get)
	@RequestMapping(value="/reg", method = RequestMethod.GET)
	public String restReg(Model model) {
		model.addAttribute("categoryList",service.selCategoryList());
		
		model.addAttribute(Const.TITLE,"가게등록");
		model.addAttribute(Const.VIEW,"rest/restReg");
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	// jsp에서 입력받은 값을 실제 등록시키는 메소드 (post)
	// jsp에서 값이 넘어올때 param = jsp(name)값을 자동으로 긁어옴 
	@RequestMapping(value="/reg", method = RequestMethod.POST)
	public String insRestList(RestPARAM param, HttpSession hs) {
		param.setI_user(SecurityUtils.getLoginUserPk(hs)); // 로그인유저 세션값 받아오는 것 		
		
		int result = 1; 
		service.insRest(param);
		
		System.out.println("--- param값 테스트 ---");
		System.out.println(param.getNm());
		System.out.println(param.getAddr());
		System.out.println(param.getR_dt());
		System.out.println(param.getCd_category());
		System.out.println("--- param값 테스트 ---");
		
		return "redirect:/rest/map";
	}
	
	
	// 디테일 화면 띄우는 메소드
	@RequestMapping("/detail")
	public String detail(Model model, RestPARAM param) {
		
		RestDMI data = service.selRest(param);
		model.addAttribute(Const.TITLE,data.getNm()); // service에서 넘어온 가게명 값을 타이틀로 박겠다 
		model.addAttribute(Const.VIEW,"rest/restDetail");
		model.addAttribute("data",data);
		
		return ViewRef.TEMP_MENU_TEMP;		
	}
		
		
}

package com.koreait.matzip.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreait.matzip.Const;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestVO;

@Controller
@RequestMapping("/rest") // 쉽게생각해서 예전에 matzip 버전할때 맵퍼 swich문 1차로 rest를 하겠다란 뜻
public class RestController {
	
	@Autowired // 9.21 이제는 왜 이거쓰는지 알겠지 ? (난 내가 이해하고 내스스로 적었으니 까먹지마라)
	private RestService service;
	
	
	@RequestMapping(value="/map")
	public String restMap(Model model) {
		model.addAttribute(Const.TITLE,"지도보기");
		model.addAttribute(Const.VIEW,"rest/restMap"); // 그안에 지도 jsp가 들어감 ( 메모장참고 )
		
		return ViewRef.TEMP_MENU_TEMP;	// header, footer 부분에 주소값 들어감
	}
	
	@RequestMapping("/ajaxGetList")
	@ResponseBody 
	public String ajaxGetList(RestPARAM param) {
		System.out.println("restMap.jsp ajax(get)으로 보낸 값 제대로 받는지 확인 ");
		System.out.println("sw_lat : " + param.getSw_lat());
		System.out.println("sw_lng : " + param.getSw_lng());
		System.out.println("ne_lat : " + param.getNe_lat());
		System.out.println("ne_lng : " + param.getNe_lng());
		System.out.println("--------------------------------");
		
		return service.selRestList(param);
	}
	
	
	
	@RequestMapping(value="/reg", method = RequestMethod.GET)
	public String insRestList(Model model) {
		model.addAttribute(Const.TITLE,"가게등록");
		model.addAttribute(Const.VIEW,"rest/restReg");
		
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	@RequestMapping(value="/reg", method = RequestMethod.POST)
	public String insRestList(RestVO vo) {
		
		return null;
	}
	
	
	
	
}

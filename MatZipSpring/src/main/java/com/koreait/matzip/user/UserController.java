package com.koreait.matzip.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

/*
 *  클래스이름 위에 @.... 적는건 99% bean 등록이라고 보면됨
 */

// Controller (맵핑담당)
@Controller
@RequestMapping("/user")
public class UserController {
	
	
	// UserService 클래스 보면 @Service 라고 해놨기에 걔를 찾아냄
	@Autowired  
	private UserService service;
	
	// 로그아웃
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpSession hs) {
		hs.invalidate();
		return "redirect:/"; // 우리는 로그인안해도 되는 서비스이니까 
							// 로그아웃했을시에는 그냥 지도화면 띄우게끔
							// 왜냐 ? 지도화면이 src-resource-index.jsp 보면 됨 (여기가 메인페이지니까)
	}
	
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute(Const.TITLE,"로그인");
		model.addAttribute(Const.VIEW,"user/login");
		//model.addAttribute("msg",err); // 학원 마지막 시간에 일단 login POST에서 날라온값 받아오는 역활 해놨음 
		return ViewRef.TEMP_DEFAULT;
	}
	
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(UserPARAM param, HttpSession hs, RedirectAttributes ra) { // RedirectAttributes 스트링값 보내주는 녀석 
		int result = service.login(param);
		
		if(result == Const.SUCCESS) {
			hs.setAttribute(Const.LOGIN_USER, param); // service.login에서 넘어온 param값을 세션화 한다 
			return "redirect:/";	// redirect: 는 실제로 스프링 내부에 구현되어있음 
									// ( 쉽게 그냥 response.sendRedirect 로 생각하면 될듯 ) ViewResolver는 jsp로 가는거니까 X!!
		}
		
		String msg = null;
		if (result == Const.NO_ID) {
			msg = "아이디를 확인해 주세요";
		} else if (result == Const.NO_PW) {
			msg = "비밀번호를 확인해 주세요";
		}
		
		param.setMsg(msg); 	// msg값을 받아서 setMsg에 박아둔다음
		
		ra.addFlashAttribute("data", param);	 // 스트링으로 값을 보내줌(즉  jsp로 값을 다시 날림) request.setAttribute라고 보면됨
												// 지금 addFlash.. 는 session처럼 값이 박히고 다쓰고나면 죽음 ( 우리가알던 세션의 구버전느낌? 짝퉁세션느낌이라 보면됨 )
		return "redirect:/user/login";	// login doGet메소드로 보내는 것
	}
	
	
	@RequestMapping(value="/join", method = RequestMethod.GET)
	public String join(Model model, @RequestParam(required = false, defaultValue="0") int err) { // @RequestParam(value="err", required=false) Integer error 이런식으로 (참고로 Integer준이유는 NULL 값 까지 받기위해서)
															// post에서 get으로 보냈을시 쿼리스트링 값이 틀리면 위에주석방식대로 지정해줘야됨 
															// 참고로 쿼리스트링은 문자열이 날라오는데 int 매개변수 주면 굳이 Integer.parseInt 안해줘도됨
		if(err > 0) {
			model.addAttribute("msg","에러가 발생하였습니다");
		}
		
		model.addAttribute(Const.TITLE,"회원가입");
		model.addAttribute(Const.VIEW,"user/join");
		System.out.println("err값 : " + err);
		return ViewRef.TEMP_DEFAULT;
	}
	
	
	
	@RequestMapping(value="/join", method = RequestMethod.POST)
	public String join(UserVO param, RedirectAttributes ra) { //join jsp에서 submit을 하면 스프링이 알아서 UserVO에 있는 이름값과 (셋터) 를 확인한후 바로 넣어줌
										// 이제 앞으로 getParameter 할필요없이 스피링이 지알아서 처리함  (값이 안들어갔다면 이름이 다르거나, 셋터메소드 없다는 뜻 )
		int result = service.join(param);
		
		if(result == 1) {
			return "redirect:/user/login";	// 즉, 이경우 login 이란 이름이 2개인데 그중에 get으로감 ( BoardWeb4 생각하면됨 / 같은맵핑이름 2개면 GET 메소드로감 )
		}
		
		ra.addAttribute("err", result);	// 쿼리스트링 밑에 return 문자열에 nnn?nn= 어쩌고 하는걸 이렇게 깔끔하게 보내는것임 
		
		return "redirect:/user/join";
		// return "redirect:/user/join?err="+result; 위에 ra.addAttribute("err", result);
		// 안해주면 이렇게 직접 쿼리스트링 값 보내야되지만 ra.addAttribute() 하면 굳이 쿼리스트링 안하고 저렇게 메소드를 통해서 쿼리스트링값 보내면됨 
	}
	
	
	// 아이디 중복체크 
	@RequestMapping(value="/ajaxIdChk", method=RequestMethod.POST)
	@ResponseBody	// 결과물, 얘를 안붙이면 jsp로 날라가든, get메소드로 날라가든 할거
					// return 값 자체가 그냥 응답이구나 즉, AJAX할때 @ResponseBody 해주면됨 (중요한애임 잘알아두기)
					// 앱이든, 웹이든 나중에는 얘를 사용함	
	public String ajaxIdChk(@RequestBody UserPARAM param) {	// ajax로 날라온 값 받는 애 : @RequestBody
		
		System.out.println("uesr_id : " + param.getUser_id());
		int result = service.login(param);
		return String.valueOf(result);	// redirect (자바) / ViewRef (jsp) 로 날리는것이 아닌 바로 값을 박아넣는 것
			// String.valueOf() = String 정수값을 Integer.parseInt() 로 하는거와 같음
	}
	
	
	
	@RequestMapping(value="/ajaxToggleFavorite", method=RequestMethod.GET)
	@ResponseBody	
	public int ajaxToggleFavorite(UserPARAM param, HttpSession hs) {	
		int i_user = SecurityUtils.getLoginUserPk(hs);
		param.setI_user(i_user);
		return service.ajaxToggleFavorite(param);
		
	}
	
}

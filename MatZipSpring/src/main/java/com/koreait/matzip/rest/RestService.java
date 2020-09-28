package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.Const;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestFile;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;
import com.koreait.matzip.user.model.UserPARAM;




@Service
public class RestService {
	
	@Autowired
	private RestMapper mapper;
	
	@Autowired
	private CommonMapper cMapper; // 음식(분류 한식, 중식, 일식 그런거 하기위한 mapper)
	
	// 지도에서 음식점 지점을 나타내는 메소드 
	public List<RestDMI> selRestList(RestPARAM param) {
		return mapper.selRestList(param);
	} 
	
	
	public List<CodeVO> selCategoryList() {
		CodeVO p = new CodeVO();
		p.setI_m(1); // 카테고리 코드 = 1 ( c_code_m 테이블)
		
		return cMapper.selCodeList(p);
	}
	
	// 가게 위치설정과 함께 등록하는 메소드
	public int insRest(RestPARAM param) {
		return mapper.insRest(param);
	}
	
	
	
	public RestDMI selRest(RestPARAM param) {
		return mapper.selRest(param);		
	}
	
	
	@Transactional // 트랜잭션 사용하기위한 어노테이션
				   // 하나하나 실행해서 에러가 터지면 rollback 잘되면 commit 하겠다
	public void delRestTran(RestPARAM param) {
		System.out.println("-- 서비스 --");
		System.out.println(param.getI_rest());
		System.out.println(param.getI_user());
		System.out.println("-	-	-");
		mapper.delRestRecMenu(param);
		mapper.delRestMenu(param);
		mapper.delRest(param);
	}
	
	public int delRestRecMenu(RestPARAM param) {
		return mapper.delRestRecMenu(param);
	}
	
	
	//-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-
	
	// 파일 추가하기
	public int insRecMenus(MultipartHttpServletRequest mReq) {
		
		int i_user = SecurityUtils.getLoginUserPk(mReq.getSession());		
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		if(_authFail(i_rest, i_user)) {
			return Const.FAIL;
		}
		
		List<MultipartFile> fileList = mReq.getFiles("menu_pic");
		String[] menuNmArr = mReq.getParameterValues("menu_nm");
		String[] menuPriceArr = mReq.getParameterValues("menu_price");
		 
		// Const.realPath = Const.java  ,  IndexController.java 참고 (기본패키지)
		String path = Const.realPath + "/resources/img/rest/" + i_rest + "/rec_menu/";
		
		List<RestRecMenuVO> list = new ArrayList();
		
		for(int i=0; i<menuNmArr.length; i++) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);
			
			String menu_nm = menuNmArr[i];
			int menu_price = CommonUtils.parseStringToInt(menuPriceArr[i]);
			vo.setMenu_nm(menu_nm);
			vo.setMenu_price(menu_price);
			vo.setI_rest(i_rest);
			
			// 파일 값 저장
			MultipartFile mf = fileList.get(i);
			
			if(mf.isEmpty()) { continue; }
			
			String originFileNm = mf.getOriginalFilename();
			String ext = FileUtils.getExt(originFileNm);
			String saveFileNm = UUID.randomUUID() + ext;
			
			try {
				mf.transferTo(new File(path + saveFileNm));
				vo.setMenu_pic(saveFileNm);				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		for(RestRecMenuVO vo : list) {
			mapper.insRestRecMenu(vo);
		}
		
		return i_rest;
	}
	

	// 저장위치 : menu   (기존에는 rec_menu 였음 (추천메뉴))
	// db테이블명 : restaurant_menu	
	public int insRestMenu(RestFile param, int i_user) {		
		if(_authFail(param.getI_rest(), i_user)) {
			return Const.FAIL;
		}
		
		String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";
		System.out.println("2 path : " + path);
		
		List<RestRecMenuVO> list = new ArrayList();
		
		for(MultipartFile mf : param.getMenu_pic()) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);			
			
			String saveFileNm = FileUtils.saveFile(path, mf);
			vo.setMenu_pic(saveFileNm);
			vo.setI_rest(param.getI_rest());
		}
		
		for(RestRecMenuVO vo : list) {
			mapper.insRestMenu(vo);
			System.out.println("3 rest : " + vo.getI_rest());
			System.out.println("4 i_user : " + vo.getMenu_pic());
		}
		
		return Const.SUCCESS;
	}
	

	
	
	// 파일 다중 등록하는 메소드 
	//selRestRecMenu
	// n번 레스토랑 메뉴 띄우기 
	public List<RestRecMenuVO> selRestRecMenu(RestPARAM param) {		
		return mapper.selRestRecMenus(param);
	}
	
	
	
	// detail.jsp 에서 ajax로 값 삭제하는 메소드
	// RestController.java 의 ajaxDelRecMenu() 메소드 와 연동
	public int delRecMenu(RestPARAM param, String realPath) {
		//파일 삭제
		List<RestRecMenuVO> list = mapper.selRestRecMenus(param);
		if(list.size() == 1) {
			RestRecMenuVO item = list.get(0);
			
			if(item.getMenu_pic() != null && !item.getMenu_pic().equals("")) { //이미지 있음 > 삭제!!
				File file = new File(realPath + item.getMenu_pic());
				if(file.exists()) {
					if(file.delete()) {
						return mapper.delRestRecMenu(param);
					} else {
						return 0;
					}
				}
			}
		}		
		
		return mapper.delRestRecMenu(param);
	}
	
	
	
	public int delRestMenu(RestPARAM param) {
		if(param.getMenu_pic() != null && !"".equals(param.getMenu_pic())) {
			String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";
			
			
			
		    if(FileUtils.delFile(path + param.getMenu_pic())) {		    	
		    	return mapper.delRestMenu(param);
		    	
		    } else {		    	
		    	return Const.FAIL;
		    }
		}
		return mapper.delRestMenu(param);
	}
	
	
	
	
	// 메뉴나타내는 곳     (추천메뉴 X!!)
	public List<RestRecMenuVO> selRestMenus(RestPARAM param) {
		return mapper.selRestMenus(param);
	}
	
	
	
	
	// 유저pk값이랑 쓴글쓴이랑 맞으면 true반환하고 insRestMenu()메뉴 발동함
	private boolean _authFail(int i_rest, int i_user) {
		RestPARAM param = new RestPARAM();
		param.setI_rest(i_rest);
		
		RestDMI dbResult = mapper.selRest(param);
		if(dbResult == null || dbResult.getI_user() != i_user) {
			return true;
		}
		
		return false;
	}
	
	
	
	
	// 조회수 기능 ( user pk값으로 막으려면 테이블 하나 더 빼서 boardWeb4 버전으로 만들어야 됨)
	
	// 현재 내 ip로 접속했을경우 그 ip를 확인하고 null 이거나 현재 마지막 i_rest번의 게시글에
	// 들어있는 ip값이 아니라면 hits를 올림  그리고 올린후 다시 마지막 ip주소값을 currentReadIp에다가 담음
	// 코드 잘읽어보면 이해될거임
	public void updAddHits(RestPARAM param, HttpServletRequest req) {
		
		// 접속한 현재 ip주소값을 가져오는 것
		String myIp = req.getRemoteAddr();	// ( a라는컴터로 들어오면 a컴터 ip, b 컴터 는 b컴터 ip )
		ServletContext ctx = req.getServletContext();
		 
		int i_user = SecurityUtils.getLoginUserPk(req);
		
		//ServletContext = 서블릿에서 Application 이라고 보면됨 ( 어플 = 서버1개당 1개의 객체만 존재 ) 
		
		
		// 5번방, 10번방 이렇게 getI_rest()식으로 생성되어 어플케이션한테 물어보고 null 이라면 처음들어간 페이지란 뜻 ( 즉, 조회수 올리자 ! ) 
		String currentReadIp = (String)ctx.getAttribute(Const.CURRENT_REST_READ_IP + param.getI_rest());
		if(currentReadIp == null || !currentReadIp.equals(myIp)) {
			
			param.setI_user(i_user); // 내가 쓴글이면 조회수 안 올라가게 쿼리문으로 막기용
			
			// 조회수 올림 처리 할때
			mapper.updAddHits(param);
			ctx.setAttribute(Const.CURRENT_REST_READ_IP + param.getI_rest(), myIp);
		}
	}
	
		
	
}

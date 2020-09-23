package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;


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
	
	public int delRestMenu(RestPARAM param) {
		return mapper.delRestMenu(param);
	}
	//-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-
	
	
	public int insRecMenus(MultipartHttpServletRequest mReq) {
				
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		List<MultipartFile> fileList = mReq.getFiles("menu_pic");
		String[] menuNmArr = mReq.getParameterValues("menu_nm");
		String[] menuPriceArr = mReq.getParameterValues("menu_price");
		 
		String path = mReq.getServletContext().getRealPath("/resources/img/rest/" + i_rest + "/rec_menu/");
		
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
	
	
}

package com.koreait.matzip.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestVO;


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
	
	
	
}

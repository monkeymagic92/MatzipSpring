package com.koreait.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;


// interface는  public 굳이 뺴도 상관없음( 자동으로 들어감 ) 
@Mapper
public interface RestMapper {
	// 레스토랑 전체 띄우기(아작스)
	public List<RestDMI> selRestList(RestPARAM param);
	
	// n번 레스토랑 메뉴리스트 띄우기
	public List<RestRecMenuVO> selRestRecMenus(RestRecMenuVO param);
	
	public int insRest(RestPARAM param);
	
	public RestDMI selRest(RestPARAM param);
	
	// 삭제 부분
	public int delRestRecMenu(RestPARAM param);
	public int delRestMenu(RestPARAM param);
	public int delRest(RestPARAM param);
	
	// 메뉴이름,가격,사진 등록
	public int insRestRecMenu(RestRecMenuVO param);
	
}

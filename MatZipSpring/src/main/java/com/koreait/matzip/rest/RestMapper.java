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
	int insRest(RestPARAM param);
	int insRestRecMenu(RestRecMenuVO param);
	List<RestDMI> selRestList(RestPARAM param);
	RestDMI selRest(RestPARAM param);
	List<RestRecMenuVO> selRestRecMenus(RestPARAM param);
	int delRestRecMenu(RestPARAM param);
	int delRestMenu(RestPARAM param);
	int delRest(RestPARAM param);
	
}

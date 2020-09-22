package com.koreait.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;


// interface는  public 굳이 뺴도 상관없음( 자동으로 들어감 ) 
@Mapper
public interface RestMapper {
	public List<RestDMI> selRestList(RestPARAM param);
	public int insRest(RestPARAM param);
	public RestDMI selRest(RestPARAM param);
}

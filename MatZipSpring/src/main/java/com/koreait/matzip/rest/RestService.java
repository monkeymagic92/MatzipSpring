package com.koreait.matzip.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;


@Service
public class RestService {
	
	@Autowired
	private RestMapper mapper;
	
	
	public String selRestList(RestPARAM param) {
		List<RestDMI> list = mapper.selRestList(param);
		System.out.println("size : " + list.size()); // null 넘어오는지 0이 넘어오는지 확인용
		Gson gson = new Gson();
		return gson.toJson(list);
	}
	
	public int isnRestList(RestPARAM param) {
		int result = mapper.intRestList(param);
		return result;
	}
}

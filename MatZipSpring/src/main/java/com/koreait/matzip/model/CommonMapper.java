package com.koreait.matzip.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CommonMapper {
	// Interface는 굳이 public 안붙여도 됨 붙이든 말든 자유 
	List<CodeVO> selCodeList(CodeVO p);
}

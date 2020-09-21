package com.koreait.matzip.user;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

// 이부분은 전에 DAO부분이라고 보면됨 
// 그리고 그 DAO는 UserMapper.xml 을 보면 이해될거임 

// Mapper 얘는 bean 등록이아니라 마이바티스에서 사용하는 명령어 (Mapper 등록하는것)
// DAO 만드는용임
@Mapper
public interface UserMapper {
	public int insUser(UserVO param);
	public UserDMI selUser(UserPARAM param);
}

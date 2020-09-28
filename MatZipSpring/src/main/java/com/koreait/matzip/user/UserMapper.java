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
	// 좋아요 눌렀을떄 추가하는 메소드
	public int insFavorite(UserVO param);
	
	// 좋아요 눌렀을때 값 빼는 메소드
	public int delFavorite(UserPARAM param);
	
	
	public int insUser(UserVO param);
	public UserDMI selUser(UserPARAM param);
}


 
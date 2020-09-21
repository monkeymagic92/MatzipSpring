package com.koreait.matzip.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Service
@RequestMapping("/user")
public class UserService {
	
	@Autowired
	private UserMapper mapper;
	
	// 1번: 로그인 성공, 2번: 아이디 없음, 3번: 비번 틀림
	public int login(UserPARAM param) {
		if(param.getUser_id().equals("")) {
			return Const.NO_ID;
		}
		UserDMI dbUser = mapper.selUser(param);
		
		if(dbUser == null) {
			return Const.NO_ID;
		}
		
		String cryptPw = SecurityUtils.getEncrypt(param.getUser_pw(), dbUser.getSalt());
		if(!cryptPw.equals(dbUser.getUser_pw())) {return Const.NO_PW;} 
		
		
		// 위에서 걸러줄거 다걸러주고 최종적으로 되었을때 이렇게 모든 완료작업을 해주는것 ↓
		
		param.setUser_pw(null); // 굳이 비번은 알필요없으니 확인만 한후 다시 null값 줌
		param.setNm(dbUser.getNm());
		param.setProfile_img(dbUser.getProfile_img());
		return Const.SUCCESS;
		
	}
	
	// 회원가입시 비밀번호 암호화 (salt version)
	public int join(UserVO param) {
		String pw = param.getUser_pw();
		String salt = SecurityUtils.generateSalt();
		String cryptPw = SecurityUtils.getEncrypt(pw, salt);
		
		param.setSalt(salt);
		param.setUser_pw(cryptPw);
		
		return mapper.insUser(param);
	}

	
}

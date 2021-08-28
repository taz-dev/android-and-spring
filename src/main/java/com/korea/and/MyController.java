package com.korea.and;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.UserDAO;
import vo.UserVO;

@Controller
public class MyController {

	UserDAO userDao;
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@RequestMapping("/regi.do")
	@ResponseBody //결과를 Ajax로 return값을 반환
	public String register( UserVO vo ) {
		
		System.out.println(vo.getId());
		System.out.println(vo.getPwd());
		
		return userDao.register(vo);
	}
	
}

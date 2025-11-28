package com.ssafy.chillandcode.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.UserDao;
import com.ssafy.chillandcode.model.dto.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	//회원 가입 (등록)
	@Override
	public boolean insertUser(User user) {
		return userDao.insertUser(user) == 1;
	}
	
	//회원 정보 조회
	@Override
	public User selectUser(long userId) {
		return userDao.selectUser(userId);
	}
	
	//회원 정보 수정
	@Override
	public boolean updateUser(User user) {
		return userDao.updateUser(user) == 1;
	}
	
	//회원 정보 삭제(탈퇴)
	@Override
	public boolean softDelete(long userId) {
		return userDao.softDelete(userId) == 1;
	}
	
	//로그인
	@Override
	public User login(User user) {
		return userDao.login(user);
	}

}

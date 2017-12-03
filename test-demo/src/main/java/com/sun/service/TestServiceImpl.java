package com.sun.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.dao.TestDao;
import com.sun.entity.User;
import com.sun.service.TestService;
@Service
public class TestServiceImpl implements TestService {
	/*@Resource
private TestDao testDao;*/

	private	TestService testService;
	@Override
	public User getUserById(int id) {
	
	User userById = testService.getUserById(id);
		return userById;
	}

}

package com.sun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/logins")
public class TestController {
	@RequestMapping(value="/ss")
	public String addView(){
		return "add/login";
	}

}

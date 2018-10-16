package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JmeterTestController {
	
	private Integer count = 0;
	
	@RequestMapping("/jmeter")
	public String test(){
			int y = ++count;
			System.out.println("我是你二爷！"+y);
		return "login";
	}
}

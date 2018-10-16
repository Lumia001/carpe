package com.taotao.service.impl;

import org.springframework.stereotype.Service;

import com.taotao.service.TestService;

@Service
public class TestServiceImpl implements TestService {

	@Override
	public String queryNow() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*@Autowired
	private TestMapper testMapper;
	@Override
	public String queryNow() {
		//注入mapper
		//调用mapper的方法 返回
		return testMapper.queryNow();
	}
*/
}

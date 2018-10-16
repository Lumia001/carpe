package com.taotao.test.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {
	
	@Test
	public void testhelper() {
		//2.初始化spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//3.获取mapper的代理对象
		TbItemMapper itemMapper = context.getBean(TbItemMapper.class);
		//设置查询条件
		TbItemExample example = new TbItemExample();
		//1.需要设置分页信息
		//紧跟着的第一个查询才会被分页
		PageHelper.startPage(1, 3);
		//4.调用mapper的方法查询数据
		List<TbItem> list = itemMapper.selectByExample(example);
		List<TbItem> list2 = itemMapper.selectByExample(example);
		
		//取分页信息
		PageInfo<TbItem> info = new PageInfo<>(list);
		System.out.println("第一个分页的list的集合的长度："+list.size());
		System.out.println("第2个分页的list的集合的长度："+list2.size());
		
		//5.循环遍历结果集 打印
		System.out.println("查询的总记录数："+info.getTotal());
		for (TbItem tbItem : list) {
			System.err.println(tbItem.getId()+">>>"+tbItem.getTitle());
		}
	}
}

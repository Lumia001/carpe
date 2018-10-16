package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;

//商品相关的处理的service接口
public interface ItemService {
	
	//获取商品信息
	//根据当前的页码和每页的行数进行分页查询
	public EasyUIDataGridResult getItemList(Integer page,Integer rows);
}

package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

//dataGrid展示数据的 pojo 包括商品的pojo
public class EasyUIDataGridResult implements Serializable {

//	private static final long serialVersionUID = 5835666890950375843L;
	
	private Integer total;
	
	private List rows;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}
	
}

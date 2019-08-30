package com.rixuv5.vo;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = -3114151990984530594L;

	//当前页
	private int pageNum;
	//每页的数量
	private int pageSize;
	//总记录数
	protected long total;
	//结果集
	protected List<T> list;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}

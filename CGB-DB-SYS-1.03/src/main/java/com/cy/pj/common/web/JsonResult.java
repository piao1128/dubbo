package com.cy.pj.common.web;

import java.io.Serializable;


import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class JsonResult implements Serializable{
	
	private static final long serialVersionUID = 1657817873682864965L;
	/**状态码*/
	private int state=1;//1表示SUCCESS,0表示ERROR
	/**状态信息*/
	private String message="ok";
	/**正确数据*/
	private Object data;
	
	public JsonResult(String message){
		this.message=message; 
	}
	public JsonResult(Object data){
		this.data=data; 
	}
	public JsonResult(Throwable e){
		this.message=e.getMessage();
		this.state=0;
	}
}

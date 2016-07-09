package com.bfd.ca.controller;

import com.alibaba.fastjson.JSONObject;
import com.bfd.ca.util.CaMongodbUtil;
import com.bfd.ca.util.JsonUtils;
import com.bfd.ca.util.LogUtil;
import com.bfd.ca.util.RspJsonHelper;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@SuppressWarnings("all")
public class TodolistController {
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/createTodolist.do")
	public String createTodolist(@RequestParam String todolist) {
		JSONObject result;
		
		Map<String, Object> map;
		try {
			map = (Map<String, Object>) JsonUtils.parse(todolist);
		} catch (Exception e) {
			LogUtil.getLogger(TodolistController.class).error(e);
			result = RspJsonHelper.getInstance().getFailJson();
			return result.toJSONString();
		}
		
		result = RspJsonHelper.getInstance().getSuccessJson();
		Document document = new Document(map);
		CaMongodbUtil.insertData("todolist",document);
		return result.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/queryTodolist.do")
	public String queryTodolist(String mid,String uid) {
		JSONObject result;
		
		BasicDBObject object = new BasicDBObject();
		if(mid==null||"".equals(mid))
			object.put("plist.uid",uid);
		else
			object.put("mid",mid);
		List<Document> list = CaMongodbUtil.queryData("todolist", object);
		result = RspJsonHelper.getInstance().getSuccessJson();
		result.put("data", list);
		return result.toJSONString();
	}
}

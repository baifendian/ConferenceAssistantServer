package com.bfd.ca.controller;

import java.io.File;
import java.util.*;

import com.bfd.ca.entity.Meeting;
import com.bfd.ca.entity.User;
import com.bfd.ca.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bfd.ca.util.CaMongodbUtil;
import com.bfd.ca.util.LogUtil;
import com.bfd.ca.util.MongodbUtil;
import com.bfd.ca.util.PropertiesUtil;
import com.bfd.ca.util.RspJsonHelper;
import com.mongodb.BasicDBObject;

@SuppressWarnings("all")
@Controller
public class MeetingController {
	private static final String INFORM_TIME = PropertiesUtil.getStringValue("inform.time");
    @ResponseBody
    @RequestMapping("/queryMeetingList.do")
    public String queryMeetingList(@RequestParam String uid, int type) {
		JSONObject result;
		List<Document> list1 = Lists.newArrayList();
		List<Document> list2 = Lists.newArrayList();
		BasicDBObject object = new BasicDBObject();
		//"我"发起的
		if (type != 2) {
			object.put("user.uid", uid);
			list1 = CaMongodbUtil.queryData("meeting", object);
		}
		//"我"参与的
		if (type != 1) {
			object = new BasicDBObject();
			object.put("plist.uid", uid);
			list2 = CaMongodbUtil.queryData("meeting", object);
		}
		Set<Document> set = new HashSet<>();
		set.addAll(list1);
		set.addAll(list2);
		if (set.size() == 0) {
			result = RspJsonHelper.getInstance().getSuccessJson();
			result.put("data", set);
			return result.toJSONString();
		}
		List<Document> list = Lists.newArrayList();
		list.addAll(set);
		Collections.sort(list, new Comparator<Document>() {
			@Override
			public int compare(Document o1, Document o2) {
				return o1.getLong("st") > o2.getLong("st") ? -1 : 1;
			}
		});
		result = RspJsonHelper.getInstance().getSuccessJson();
		result.put("data", list);
		return result.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/queryNotStartMeetingList.do")
	public String queryNotStartMeetingList() {
		JSONObject result;
		BasicDBObject object = new BasicDBObject();
		List<Document> list = CaMongodbUtil.queryData("meeting", object);
		List<Document> newList = new ArrayList<Document>();
		long currentTimestamp = System.currentTimeMillis();
		for(Document doc:list){
			long st = doc.getLong("st");
			if(st - currentTimestamp <= Long.parseLong(INFORM_TIME) && st - currentTimestamp >=0 ){
				newList.add(doc);
			}
		}
		result = RspJsonHelper.getInstance().getSuccessJson();
		result.put("data", newList);
		return result.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/createMeeting.do")
	public String createMeeting(@RequestParam String meeting) {
		JSONObject result;
		List<User> plist;
		Map<String, Object> map;
		try {
			JSONObject json = JSONObject.parseObject(meeting);
			map = Maps.newHashMap(json);
			plist = JSON.parseArray(json.getString("plist"), User.class);
		} catch (Exception e) {
			LogUtil.getLogger(MeetingController.class).error(e);
			result = RspJsonHelper.getInstance().getFailJson();
			return result.toJSONString();
		}

		result = RspJsonHelper.getInstance().getSuccessJson();
		Document document = new Document(map);
		CaMongodbUtil.insertData("meeting",document);
		StringBuffer sendToEmailList = new StringBuffer();
		for(User user:plist){
			sendToEmailList.append(user.getEmail()==null?"":user.getEmail()).append(",");
		}
		if(sendToEmailList.length()>0){
			String sendToStr = sendToEmailList.substring(0, sendToEmailList.length()-1).toString();
			Meeting meetingObj = JSON.parseObject(meeting, Meeting.class);
			TemplateUtil.getDoc("data", meetingObj);
			File file = new File(TemplateUtil.emailFilePath);
			String templateStr = TemplateUtil.getStringFromFile(file,"UTF-8");
			MailUtil mail = new MailUtil(document.getString("title"),templateStr,sendToStr);
			mail.sendout();
		}
		return result.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/updateMeeting.do")
	public String updateMeeting(@RequestParam String meeting) {
		JSONObject result;

		HashMap<String, Object> map;
		try {
			JSONObject json = JSONObject.parseObject(meeting);
			map = Maps.newHashMap(json);
		} catch (Exception e) {
			LogUtil.getLogger(MeetingController.class).error(e);
			result = RspJsonHelper.getInstance().getFailJson();
			return result.toJSONString();
		}
		String mid = (String) map.get("mid");
		if (mid==null){
			result = RspJsonHelper.getInstance().getFailJson();
			return result.toJSONString();
		}
		result = RspJsonHelper.getInstance().getSuccessJson();
		HashMap<String,Object> condition = Maps.newHashMap();
		condition.put("mid",mid);
		map.remove("mid");
		MongodbUtil.getInstance().update(MongodbUtil.MONGODB_DB,"meeting", condition,map);
		return result.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/queryOneMeeting.do")
	public String queryOneMeeting(@RequestParam String mid) {
		JSONObject result;
		
		BasicDBObject object = new BasicDBObject();
		object.put("mid",mid);
		List<Document> list = CaMongodbUtil.queryData("meeting", object);
		if(list.size() == 0){
			result = RspJsonHelper.getInstance().getSuccessJson();
			result.put("data", list);
			return result.toJSONString();
		}
		
		result = RspJsonHelper.getInstance().getSuccessJson();
		result.put("data", list);
		return result.toJSONString();
	}
	@ResponseBody
	@RequestMapping("/share.do")
	public void share(@RequestParam String meeting,@RequestParam String shareList) {
		List<User> users = JSON.parseArray(shareList,User.class);
		String sendToList = "";
		for(User user:users){
			sendToList += user.getEmail()==null?"":user.getEmail()+",";
		}
		if(sendToList.length()>0)
			sendToList = sendToList.substring(0, sendToList.length()-1);
		Meeting meetingModel = JSON.parseObject(meeting, Meeting.class);
		TemplateUtil.getDoc("data", meetingModel);
		String shareContent = TemplateUtil.getStringFromFile();
		MailUtil mail = new MailUtil("会议分享:"+meetingModel.getTitle(),shareContent,sendToList);
		mail.sendout();
	}
}

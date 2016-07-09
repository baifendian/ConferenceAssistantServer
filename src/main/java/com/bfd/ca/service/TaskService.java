package com.bfd.ca.service;


import com.alibaba.fastjson.JSON;
import com.bfd.ca.entity.User;
import com.bfd.ca.util.*;
import com.mongodb.BasicDBObject;
import org.bson.Document;

import java.util.List;

@SuppressWarnings("all")
public class TaskService {
	public static boolean RUNNING_FLAG = false;
	private static final String INFORM_TIME = PropertiesUtil.getStringValue("inform.time");
	public synchronized void oneMinTask() {
		long currTime = System.currentTimeMillis();
		if (!RUNNING_FLAG) {
			RUNNING_FLAG = true;
			BasicDBObject object = new BasicDBObject();
			try{
				List<Document> list = CaMongodbUtil.queryData("meeting", object);
				long currentTimestamp = System.currentTimeMillis();
				for(Document doc:list){
					Long st = doc.getLong("st");
					String mid = doc.getString("mid");
					LogUtil.getLogger(getClass()).info("st:"+st+" ct:"+currentTimestamp+" info:"+Long.parseLong(INFORM_TIME));
					if(((st - currentTimestamp) <= Long.parseLong(INFORM_TIME)) && ((st - currentTimestamp) >=0 )){
						if(mid!= null && (!Constant.MEETING_MESSAGE_STATUS.containsKey(mid))){
							try{
								String plistStr = JSON.toJSONString(doc.get("plist"));
								List<User> users = JSON.parseArray(plistStr, User.class);
								String sendToList = "";
								for(User user:users){
									sendToList += user.getEmail()==null?"":user.getEmail()+",";
								}
								String msg = "会议开始时间:"+CalendarUtil.getSimpleDate(doc.getLong("st"))+",请提前入场!";
								MailUtil mail = new MailUtil(doc.getString("title"),msg,sendToList);
								mail.sendout();
								Constant.MEETING_MESSAGE_STATUS.put(mid, String.valueOf(currTime));
								LogUtil.getLogger(getClass()).info("提醒邮件已经发送成功");
							}catch(Exception e){
								e.printStackTrace();
							}
							
						}
					}else{
						if(st - currentTimestamp<0){
							if(mid!= null)
								Constant.MEETING_MESSAGE_STATUS.remove(mid);
						}
					}
				}	
				LogUtil.getLogger(getClass()).info("无符合条件数据");
			}catch(Exception e){
				LogUtil.getLogger(getClass()).info(e);
				e.printStackTrace();
			}finally{
				RUNNING_FLAG = false;
			}

		}
	}
}

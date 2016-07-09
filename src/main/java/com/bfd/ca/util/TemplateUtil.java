package com.bfd.ca.util;

import com.bfd.ca.entity.Meeting;
import com.bfd.ca.entity.TemplateDataModel;
import com.bfd.ca.entity.User;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("all")
public class TemplateUtil {

	public static String templatePath = PropertiesUtil.class.getClassLoader().getResource(PropertiesUtil.getStringValue("APP_SERVER_EMAIL_TEMPLATE_NAME")).getPath();
	public static String templateDir = templatePath.substring(0, templatePath.lastIndexOf("/"));
	public static String emailFilePath = templateDir+"/"+PropertiesUtil.getStringValue("APP_SERVER_EMAIL_DOC_NAME");

	public static String getStringFromFile(){
		File file = new File(emailFilePath);
		String str = getStringFromFile(file,"UTF-8");
		return str;
	}

	public static String getStringFromFile(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file),encoding);
			} else {
				reader = new InputStreamReader(new FileInputStream(file),PropertiesUtil.getStringValue("APP_SERVER_EMAIL_DEFAULT_ENCODING"));
			}
			// 将输入流写入输出流
			char[] buffer = new char[PropertiesUtil.getIntegerValue("APP_SERVER_EMAIL_CONTENT_DEFAULT_SIZE")];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		// 返回转换结果
		return writer.toString();
	}
	public static void getDoc(String key,Meeting data){
		getDoc(key,convertMeetingModelToTemplateModel(data));
	}
	public static void getDoc(String key,TemplateDataModel data) {
		FileOutputStream fos = null;
		BufferedWriter writer = null;
		try {
			Properties properties = new Properties();
			//指定生成静态文档需要的模板文件所在的目录
			properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,templateDir);
			VelocityEngine engine = new VelocityEngine();
			//初始化模板引擎
			engine.init(properties);
			//根据API_TEMPLATE_FILE读取生成静态文档需要的模板文件
			Template template = engine.getTemplate(PropertiesUtil.getStringValue("APP_SERVER_EMAIL_TEMPLATE_NAME"), PropertiesUtil.getStringValue("APP_SERVER_EMAIL_DEFAULT_ENCODING"));
			VelocityContext context = new VelocityContext();
			//将生成文档需要的数据apiModel放入模板引擎的上下文中
			context.put(key, data);
			//确定静态文档在共享文件目录的完整存储路径
			File file = new File(emailFilePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			fos = new FileOutputStream(file);
			writer = new BufferedWriter(new OutputStreamWriter(fos,  PropertiesUtil.getStringValue("APP_SERVER_EMAIL_DEFAULT_ENCODING")));// 设置写入的文件编码,解决中文问题
			//将数据与模板merge，并写入到静态文档
			template.merge(context, writer);
		} catch (Exception e) {
			e.printStackTrace();
			//打印日志
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					//打印日志
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					//打印日志
				}
			}
		}
	}
	private static TemplateDataModel convertMeetingModelToTemplateModel(Meeting meeting){
		TemplateDataModel model = new TemplateDataModel();
		model.setFromUserName(meeting.getUser().getName());
		String toUserNameList = "";
		List<User> plist = meeting.getPlist();
		for(User user:plist){
			toUserNameList += user.getName()==null?"":user.getName()+"  ";
		}
		if(toUserNameList.length()>0){
			model.setToUserName(toUserNameList);
		}
		model.setTitle(meeting.getTitle());
		model.setContent(meeting.getContent());
		model.setRemark(meeting.getRemark());
		model.setMeetingAddr(meeting.getAddr());
		model.setStartMeetingTime(CalendarUtil.getSimpleDate(meeting.getSt()));
		model.setEndMeetingTime(CalendarUtil.getSimpleDate(meeting.getEt()));
		return model;
	}
	
	public static void main(String[] args) {

	}
}
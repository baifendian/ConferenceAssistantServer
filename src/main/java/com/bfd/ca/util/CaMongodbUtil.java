package com.bfd.ca.util;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * mongodb 操作简化工具
 */
@SuppressWarnings("all")
public class CaMongodbUtil {
    private static final String MONGODB_HOST = PropertiesUtil.getStringValue("mongodb.ip");
    private static final int MONGODB_PORT = PropertiesUtil.getIntegerValue("mongodb.port");
    private static final String MONGODB_DB = PropertiesUtil.getStringValue("database.info");
    //用户名
    public static final String MONGODB_USER = PropertiesUtil.getStringValue("mongodb.user");
    //密码
    public static final String MONGODB_PASSWORD = PropertiesUtil.getStringValue("mongodb.password");
    private static MongoClient mongoClient;

    static {
        String MONGODB_PASSWORD_DECRYPT = null;
        try {
            MONGODB_PASSWORD_DECRYPT = DESUtils.decrypt(MONGODB_PASSWORD);
        } catch (Exception e) {
            LogUtil.getLogger(MongodbUtil.class).error(e);
            e.printStackTrace();
        }
        ServerAddress serverAddress = new ServerAddress(MONGODB_HOST, MONGODB_PORT);
        List<ServerAddress> adders = new ArrayList<ServerAddress>();
        adders.add(serverAddress);
        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
        MongoCredential credential = MongoCredential.createScramSha1Credential(MONGODB_USER, MONGODB_DB, MONGODB_PASSWORD_DECRYPT.toCharArray());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(credential);
        //通过连接认证获取MongoDB连接
        mongoClient = new MongoClient(adders, credentials);
    }

    public static List<Document> getListByCollection(String collectionName) {
        List<Document> list = new ArrayList<Document>();
        try {
            MongoDatabase mdb = mongoClient.getDatabase(MONGODB_DB);
            MongoCollection<Document> collection = mdb.getCollection(collectionName);
            MongoCursor<Document> mCursor = collection.find().iterator();
            while (mCursor.hasNext()) {
                Document document = mCursor.next();
                list.add(document);
            }
        } catch (Exception e) {
            LogUtil.getLogger(CaMongodbUtil.class).error("exception in getListByCollection,collectionName is " + collectionName, e);
        }
        return list;
    }

    public static void insertData(String collectionName, Document data) {
        try {
            MongoDatabase mdb = mongoClient.getDatabase(MONGODB_DB);
            MongoCollection<Document> collection = mdb.getCollection(collectionName);
            collection.insertOne(data);
        } catch (Exception e) {
            LogUtil.getLogger(CaMongodbUtil.class).error("exception in insertData,collectionName is "
                    + collectionName + ",data is " + data, e);
        }
    }

    public static List<Document> queryData(String collectionName, Bson filter) {
        List<Document> list = new ArrayList<Document>();
        try {
            MongoDatabase mdb = mongoClient.getDatabase(MONGODB_DB);
            MongoCollection<Document> collection = mdb.getCollection(collectionName);
            MongoCursor<Document> mCursor = collection.find(filter).iterator();
            while (mCursor.hasNext()) {
                Document document = mCursor.next();
                list.add(document);
            }
        } catch (Exception e) {
            LogUtil.getLogger(CaMongodbUtil.class).error("exception in queryData,collectionName is "
                    + collectionName + ",filter is " + filter, e);
        }
        return list;
    }

    public static long countData(String collectionName, Bson filter) {
        long count = 0;
        try {
            MongoDatabase mdb = mongoClient.getDatabase(MONGODB_DB);
            MongoCollection<Document> collection = mdb.getCollection(collectionName);
            count = collection.count(filter);
        } catch (Exception e) {
            LogUtil.getLogger(CaMongodbUtil.class).error("exception in countData,collectionName is "
                    + collectionName + ",filter is " + filter, e);
        }
        return count;
    }
}

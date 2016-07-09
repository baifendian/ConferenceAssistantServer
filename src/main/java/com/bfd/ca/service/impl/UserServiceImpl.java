package com.bfd.ca.service.impl;

import com.bfd.ca.entity.User;
import com.bfd.ca.service.UserService;
import com.bfd.ca.util.MongodbUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jinwei.li on 2016/6/27.
 */
@Service("userService")
@SuppressWarnings("all")
public class UserServiceImpl implements UserService {
    @Override
    public boolean addUser(User user) {
        try {
            MongodbUtil.getInstance().insert(MongodbUtil.MONGODB_DB, MongodbUtil.MONGODB_USERINFO, user);
            return  true;
        }
        catch (Exception e){

        }
        return false;
    }

    @Override
    public boolean updateUser(HashMap<String, Object> map, User user) {
        return MongodbUtil.getInstance().update(MongodbUtil.MONGODB_DB, MongodbUtil.MONGODB_USERINFO, map, user);
    }

    @Override
    public User getUser(HashMap<String, Object> condition) {
        List<User> list = MongodbUtil.getInstance().getByCondition(MongodbUtil.MONGODB_DB, MongodbUtil.MONGODB_USERINFO, condition, User.class);
        if (list != null && list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    @Override
    public List<User> getAllUser(HashMap<String, Object> condition) {
        List<User> list = MongodbUtil.getInstance().getByCondition(MongodbUtil.MONGODB_DB, MongodbUtil.MONGODB_USERINFO, condition, User.class,"name","email");
        return list;
    }

    @Override
    public boolean isAvailable(HashMap<String, Object> condition) {
        return getUser(condition) != null;
    }

    @Override
    public String uploadPic(MultipartFile file) {
        return null;
    }

    @Override
    public String loadPic(HashMap<String, Object> condition) {
        User user = null;
        if ((user = getUser(condition)) != null) {
            return user.getImgurl();
        }
        return null;
    }
}

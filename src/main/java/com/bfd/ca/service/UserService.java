package com.bfd.ca.service;

import com.bfd.ca.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jinwei.li on 2016/6/27.
 */
@SuppressWarnings("all")
public interface UserService {

    //增加用户
    public boolean addUser(User user);

    /**
     * 修改用户
     *
     * @param map  查询条件
     * @param user
     * @return
     */
    public boolean updateUser(HashMap<String, Object> map, User user);

    //获取用户
    public User getUser(HashMap<String,Object> condition);

    //获取用户
    public List<User> getAllUser(HashMap<String,Object> condition);

    //验证用户
    public boolean isAvailable(HashMap<String,Object> condition);

    //上传头像
    public String uploadPic(MultipartFile file);

    //查看头像
    public String loadPic(HashMap<String, Object> condition);
}

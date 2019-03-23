package org.redrock.aopdemo.controller;

import org.redrock.aopdemo.annotation.ClearCache;
import org.redrock.aopdemo.annotation.GetCacheable;
import org.redrock.aopdemo.annotation.UpdateCacheEvict;
import org.redrock.aopdemo.dao.UrMappingRepository;
import org.redrock.aopdemo.dao.UserRepository;

import org.redrock.aopdemo.model.UrMapping;
import org.redrock.aopdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.management.timer.Timer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

/**
 * @author: Shiina18
 * @date: 2019/3/15 19:40
 * @description:
 */
@RestController
public class MainController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UrMappingRepository urMappingRepository;

    User userTest = new User();
    /**
     * 创建一个新用户
     * @param username
     * @param password
     * @param roleId
     * @return
     */
    @PostMapping("/user/create")//5
    public User Create(String username, String password,int roleId) {

        User user = new User();
        user.setUserPassword(password);
        user.setUserName(username);

        user = userRepository.save(user);

        UrMapping urMapping =  new UrMapping();
        urMapping.setUserId(user.getUserId());
        urMapping.setRoleId(roleId);
        urMappingRepository.save(urMapping);

        return user;
    }

    /**
     * 实现登陆功能
     * @param request
     * @param username
     * @param password
     * @return
     */

    @PostMapping("/user/login")
    public boolean Login(HttpServletRequest request, String username, String password){
        User user = userRepository.findByUserName(username);
        if(user == null) return false;

        HttpSession session = request.getSession(true);
        session.setAttribute("user",user);

        if(user.getUserPassword().equals(password))
            return true;
        else return false;

    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @PostMapping(value = "/user/delete")//2
    public boolean Delete(int userId){
         userRepository.deleteByUserId(userId);
         return true;
    }

    /**
     * 修改用户的角色
     * @param userId
     * @param roleNewId
     * @return
     */
    @PostMapping(value = "/user/update")//4
    public Optional<User> UserRoleChange(int userId, int roleNewId){
        userRepository.UserRoleChange(roleNewId,userId);
       return userRepository.findById(userId);
    }

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @PostMapping(value = "/user/find")//1,3
    public Optional<User> Find(int userId) {
        return userRepository.findById(userId);
    }

    /**
     * 创建用户新的角色
     * @param userId
     * @param roleId
     * @return
     */
   @PostMapping(value = "/user/change")//6
    public boolean UserRoleCreate(int userId,int roleId) {
       UrMapping urMapping = new UrMapping();
       urMapping.setUserId(userId);
       urMapping.setRoleId(roleId);
       urMappingRepository.save(urMapping);
       return true;
   }

    /**
     * 缓存测试
     * @param userId
     * @return
     */
    @GetCacheable
    @GetMapping(value = "/user/find")
    public User TestFind(int userId){
         userTest.setUserId(userId);
        try {
            Thread.sleep(5 * Timer.ONE_SECOND);//模拟计算和对关系型数据库访问的开销
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         return  userTest;
//        try {
//            Thread.sleep(5 * Timer.ONE_SECOND);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return userRepository.findById(userId);
    }

    @UpdateCacheEvict
    @GetMapping(value = "/user/update")
    public boolean TestUpdate(int userId, String userName) {
        userTest.setUserName(userName);
//        return userRepository.UserNameChange(userName,userId);
        return true;
    }


    @ClearCache
    @GetMapping(value = "/user/clear")
    public void  clearCache() {
        return;
}
}

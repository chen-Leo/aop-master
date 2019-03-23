package org.redrock.aopdemo.dao;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.redrock.aopdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 删除一个用户
     * @param userId
     */
    @Modifying
    @Transactional
    @Query("delete from User where user_id = ?1")
    void deleteByUserId(int userId);


    /**
     *  更改用户角色
     * @param roleNewId
     * @param userId
     */
    @Modifying
    @Transactional
    @Query("update User set role_id = ?1 where user_id = ?2")
    void UserRoleChange(int roleNewId, int userId);

    User findByUserName(String username);

    @Modifying
    @Transactional
    @Query("update User set userName = ?1 where user_id = ?2")
    void UserNameChange(String userName, int userId);


}
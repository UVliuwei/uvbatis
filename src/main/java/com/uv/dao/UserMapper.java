package com.uv.dao;
/*
 * @author liuwei
 * @date 2019/2/19 14:54
 *
 */

import com.uv.entity.User;
import java.util.List;

public interface UserMapper {

    User findUserById(int id);
    List<User> findUserList();

}

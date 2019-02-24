package com.uv;

import com.uv.dao.UserMapper;
import com.uv.entity.User;
import com.uv.session.UVSqlSession;
import com.uv.session.UVSqlSessionFactory;

//<liuwei> [2019/2/19 15:15]
public class Main {

    public static void main(String[] args) {
        UVSqlSessionFactory factory = new UVSqlSessionFactory();
        UVSqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.findUserById(1);
        System.out.println(user.getName());
    }
}

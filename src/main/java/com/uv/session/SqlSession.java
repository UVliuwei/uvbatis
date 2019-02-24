package com.uv.session;
/*
 * @author liuwei
 * @date 2019/2/19 15:03
 * sqlSession
 */

import com.uv.config.MapperStatement;
import java.util.List;

public interface SqlSession {

    <T> T getOne(MapperStatement statement, Object param);

    <T> List<T> getAll(MapperStatement statement, Object param);

    <T> List<T> getAll(MapperStatement statement);

    <T> T getMapper(Class<T> clazz);

}

package com.uv.proxy;
/*
 * @author liuwei
 * @date 2019/2/19 16:13
 * mapper接口代理类
 */

import com.uv.config.Configuration;
import com.uv.config.MapperStatement;
import com.uv.session.UVSqlSession;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

public class MapperProxy implements InvocationHandler {

    private UVSqlSession sqlSession;
    private Configuration configuration;

    public MapperProxy(UVSqlSession sqlSession, Configuration configuration) {
        this.sqlSession = sqlSession;
        this.configuration = configuration;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String nameSpace = method.getDeclaringClass().getName(); //全路径类名
        String methodName = method.getName(); //方法名
        MapperStatement statement = configuration.getStatementMap().get(nameSpace + "." + methodName);
        if(Collection.class.isAssignableFrom(method.getReturnType())) { //返回集合类型
            if(args == null) {
                return sqlSession.getAll(statement, args);
            }
            return sqlSession.getAll(statement, args[0]);//特殊处理，默认当一个处理了(有时间再处理)
        }
        return sqlSession.getOne(statement, args[0]);
    }
}

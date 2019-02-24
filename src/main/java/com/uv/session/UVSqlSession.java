package com.uv.session;
/*
 * @author liuwei
 * @date 2019/2/19 15:23
 *
 */

import com.uv.config.Configuration;
import com.uv.config.MapperStatement;
import com.uv.executor.Executor;
import com.uv.executor.UVExecutor;
import com.uv.proxy.MapperProxy;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.List;

public class UVSqlSession implements SqlSession, Serializable {

    private static final long serialVersionUID = -4535150305573160272L;

    private Configuration configuration;
    private Executor executor; //执行器

    public UVSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.executor = new UVExecutor(configuration);
    }

    @Override
    public <T> T getOne(MapperStatement statement, Object param) {
        List<T> list = getAll(statement, param);
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("too many results!");
        }
        return list.get(0);
    }

    @Override
    public <T> List<T> getAll(MapperStatement statement, Object param) {
        return executor.query(statement, param);
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {
        try {
            MapperProxy mapperProxy = new MapperProxy(this, configuration);
            return (T) Proxy.newProxyInstance(MapperProxy.class.getClassLoader(), new Class[]{clazz}, mapperProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

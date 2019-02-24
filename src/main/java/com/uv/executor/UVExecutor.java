package com.uv.executor;
/*
 * @author liuwei
 * @date 2019/2/19 16:38
 * 执行器，查询sql
 */

import com.alibaba.druid.pool.DruidDataSource;
import com.uv.config.Configuration;
import com.uv.config.MapperStatement;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UVExecutor implements Executor {

    private Configuration configuration;
    private DruidDataSource dataSource;

    private void initDataSource() {
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName(configuration.getJdbcDriver());
        dataSource.setUrl(configuration.getJdbcUrl());
        dataSource.setUsername(configuration.getJdbcUserName());
        dataSource.setPassword(configuration.getJdbcPassword());
    }

    public UVExecutor(Configuration configuration) {
        this.configuration = configuration;
        initDataSource();
    }

    @Override
    public <T> List<T> query(MapperStatement statement, Object param) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement(statement.getSql());
            if(param != null) { //暂时就当一个参数处理
                prepareStatement.setObject(1, param);
            }
            ResultSet resultSet = prepareStatement.executeQuery();
            return resolveResult(statement, resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //处理查询结果
    private <T> List<T> resolveResult(MapperStatement statement, ResultSet set) {
        try {
            List<T> list = new ArrayList<>();
            String resultType = statement.getResultType();
            Class<?> clazz = Class.forName(resultType);
            T instance = (T) clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            while (set.next()) {
                for (Field field : fields) {
                    processValue(instance, field, set.getString(field.getName().toLowerCase()));
                }
                list.add(instance);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //处理结果类型
    private <T> void processValue(T instance, Field field, String value) throws IllegalAccessException, InstantiationException {
        field.setAccessible(true);
        String className = field.getType().getName();
        switch (className) {
            case "int":
            case "java.lang.Integer":
                field.setInt(instance, Integer.valueOf(value));
                break;
            case "double":
            case "java.lang.Double":
                field.setDouble(instance, Double.valueOf(value));
                break;
            case "java.lang.String":
                field.set(instance, value);
                break;

            //其它类型......
        }
    }

}

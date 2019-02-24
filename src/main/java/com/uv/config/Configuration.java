package com.uv.config;
/*
 * @author liuwei
 * @date 2019/2/19 15:06
 * 配置文件类，保存配置文件信息
 */

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUserName;
    private String jdbcPassword;
    //保存所有的mapper.xml配置信息，key为(namespace + methodname)
    private Map<String, MapperStatement> statementMap = new HashMap();

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUserName() {
        return jdbcUserName;
    }

    public void setJdbcUserName(String jdbcUserName) {
        this.jdbcUserName = jdbcUserName;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public Map<String, MapperStatement> getStatementMap() {
        return statementMap;
    }

    public void setStatementMap(Map<String, MapperStatement> map) {
        this.statementMap = map;
    }
}

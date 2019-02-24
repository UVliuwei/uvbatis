package com.uv.session;
/*
 * @author liuwei
 * @date 2019/2/19 15:13
 * SqlSessionFactory实现类
 */

import com.uv.config.Configuration;
import com.uv.config.MapperStatement;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class UVSqlSessionFactory {

    //配置文件信息
    private final String CONFIG_NAME = "db.properties";
    private final String JDBC_DRIVER = "jdbc.driver";
    private final String JDBC_URL = "jdbc.url";
    private final String JDBC_USERNAME = "jdbc.username";
    private final String JDBC_PASSWORD = "jdbc.password";
    //mapper.xml信息
    private final String NAME_SPACE = "namespace";
    private final String ID = "id";
    private final String RESULT_TYPE = "resultType";

    private final Configuration configuration;

    //初始化
    public UVSqlSessionFactory() {
        configuration = new Configuration();
        initConfiguration();
        initMapperConfiguration();
    }

    //加载配置文件
    private void initConfiguration() {
        InputStream inputStream = UVSqlSessionFactory.class.getClassLoader().getResourceAsStream(CONFIG_NAME);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("load configration error!");
        }
        configuration.setJdbcDriver(properties.getProperty(JDBC_DRIVER));
        configuration.setJdbcUrl(properties.getProperty(JDBC_URL));
        configuration.setJdbcUserName(properties.getProperty(JDBC_USERNAME));
        configuration.setJdbcPassword(properties.getProperty(JDBC_PASSWORD));
    }

    //加载mapper文件
    private void initMapperConfiguration() {
        URL url = UVSqlSessionFactory.class.getClassLoader().getResource("mapper");
        File root = new File(url.getFile());
        if (root == null || !root.isDirectory()) {
            throw new RuntimeException("load mapper error!");
        }
        try {
            //文件夹,遍历文件夹下的文件
            File[] files = root.listFiles();
            for (File file : files) {
                resolveMapperXml(file); //解析mapper.xml
            }
        } catch (Exception e) {
            throw new RuntimeException("load mapper error!");
        }
    }

    //解析mapper.xml
    private void resolveMapperXml(File file) throws DocumentException {
        Map<String, MapperStatement> statementMap = configuration.getStatementMap();
        MapperStatement statement = null;
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element rootElement = document.getRootElement(); //取到<mapper>节点
        String nameSpace = rootElement.attributeValue(NAME_SPACE);
        for (Object object : rootElement.elements()) { //mapper下的子节点
            Element element = (Element) object;
            statement = new MapperStatement();
            statement.setNameSpace(nameSpace); //空间名
            statement.setTagName(element.getName()); //标签名
            statement.setId(element.attributeValue(ID)); //id属性
            statement.setResultType(element.attributeValue(RESULT_TYPE)); //返回结果属性
            statement.setSql(element.getTextTrim()); //获取sql
            statementMap.put(statement.getNameSpace() + "." + statement.getId(), statement);
        }
    }

    public UVSqlSession openSession() {
        return new UVSqlSession(configuration);
    }
}

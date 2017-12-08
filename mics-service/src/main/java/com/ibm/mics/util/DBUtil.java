package com.ibm.mics.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBUtil {
	
	private static volatile DBUtil _instance;
	private static final String RESOURCE = "SqlMapConfig.xml";
	private static SqlSessionFactory sqlSessionFactory;
	
	private DBUtil() {
		if (sqlSessionFactory == null) {
			initFactory();
		}
	}
	
	public static DBUtil getInstance() {
		if(_instance == null) {
    		synchronized (DBUtil.class) {
				if (_instance == null) {
					_instance = new DBUtil();
				}
			}
    	}
		return _instance;
	}
	
	public void initFactory() {
		try {
			InputStream stream = Resources.getResourceAsStream(RESOURCE);
	        sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SqlSession getSession() {
		return sqlSessionFactory.openSession();
    }
    
}
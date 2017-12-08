package com.ibm.mics.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtil {
	
	private static Logger logger = Logger.getLogger(PropertiesUtil.class);
	
	private static Properties properties = null;
	private static volatile PropertiesUtil _instance;
	
	private static String serviceConfigPath = "/service.properties";
	
	private static String App_Prefix = "APP_";
	private static String Err_Prefix = "ERR_";

	///////////////////////////////////////////////
	///////////////APP SERVICE SETTING/////////////
	///////////////////////////////////////////////
	public static String ResetPW = "RESET_PW";
	public static String EncodePW = "ENCODE_PW";
	public static String SessionTimeout = "SESSION_TIMEOUT";
	
	///////////////////////////////////////////////
	///////////////ERR MAPPING/////////////////////
	///////////////////////////////////////////////
	public static String IOExceptionId = "IO_EXCEPTION_ID";
	public static String IOExceptionMsg = "IO_EXCEPTION_MSG";
	public static String DBExceptionId = "DB_EXCEPTION_ID";
	public static String DBExceptionMsg = "DB_EXCEPTION_MSG";
	public static String DBInsertFailId = "DB_INSERT_FAIL_ID";
	public static String DBInsertFailMsg = "DB_INSERT_FAIL_MSG";
	public static String DBUpdateFailId = "DB_UPDATE_FAIL_ID";
	public static String DBUpdateFailMsg = "DB_UPDATE_FAIL_MSG";
	public static String DBDeleteFailId = "DB_DELETE_FAIL_ID";
	public static String DBDeleteFailMsg = "DB_DELETE_FAIL_MSG";
	public static String DBDataNotExistId = "DB_DATA_NOT_EXIST_ID";
	public static String DBDataNotExistMsg = "DB_DATA_NOT_EXIST_MSG";
	
	public static String OtherExceptionId = "OTHER_EXCEPTION_ID";
	public static String OtherExceptionMsg = "OTHER_EXCEPTION_MSG";
	public static String ArgMissMappingId = "ARG_MISS_MAPPING_ID";
	public static String ArgMissMappingMsg = "ARG_MISS_MAPPING_MSG";
	public static String ArgMissInvalidId = "ARG_MISS_INVALID_ID";
	public static String ArgMissInvalidMsg = "ARG_MISS_INVALID_MSG";
	public static String ExternalPlatformId = "EXTERNAL_PLATFORM_ID";
	public static String ExternalPlatformMsg = "EXTERNAL_PLATFORM_MSG";
	
	public static String UserNotRegisterId = "USER_NOT_REGISTER_ID";
	public static String UserNotRegisterMsg = "USER_NOT_REGISTER_MSG";
	public static String UserWrongPWId = "USER_WRONG_PW_ID";
	public static String UserWrongPWMsg = "USER_WRONG_PW_MSG";
	
	private PropertiesUtil() {
		if (properties == null) {
			properties = new Properties();
			loadConf();
		}
	}
	
	public void loadConf() {
		try {
			InputStream fileInputStream = null;
			
			fileInputStream = this.getClass().getResourceAsStream(serviceConfigPath);
			properties.load(fileInputStream);
			
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			logger.error(String.format("Service Config File: %s", e.getMessage().toString()));
		} catch (IOException e) {
			logger.error(String.format("Service Config File: %s", e.getMessage().toString()));
		}
	}

	public static PropertiesUtil getInstance() {
		if(_instance == null) {
    		synchronized (PropertiesUtil.class) {
				if (_instance == null) {
					_instance = new PropertiesUtil();
				}
			}
    	}
		return _instance;
	}

	private static String getProperty(String key) {
		try {
			if(properties.getProperty(key) == null)
				return "";
			
			String value = new String(properties.getProperty(key).getBytes(
					"ISO-8859-1"), "UTF-8");// "UTF-8"
			if (value == null || value.trim().equals(""))
				return "";
			else
				return value.trim();
		} catch (Exception e) {
			logger.error(String.format("Error: %s", e.getMessage().toString()));
			return "";
		}
	}
	
	private static String getProperty(String key, String[] args) {
		String prop = getProperty(key);
		return MessageFormat.format(prop, args);
	}
	
	//APP
    public String getAppPropertyStr(String propName) {
		return getProperty(App_Prefix + propName);
	}
    
    public int getAppPropertyInt(String propName) {
        return Integer.parseInt(getProperty(App_Prefix + propName));
    }
    
    public boolean getAppPropertyBoolean(String propName) {
        return getProperty(App_Prefix + propName).equals("true") ? true : false;
    }
    
	//ERR
    public String getErrId(String errName) {
		return getProperty(Err_Prefix + errName);
	}
    
    public String getErrMsg(String errName) {
		return getProperty(Err_Prefix + errName);
	}
    
    public String getErrMsg(String errName, String[] args) {
		return getProperty(Err_Prefix + errName, args);
	}
    
}

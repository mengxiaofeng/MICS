package com.ibm.mics.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;  
  
public final class JacksonUtil {  
  
    public static ObjectMapper objectMapper;  
  
    /** 
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。 
     * (1)转换为普通JavaBean：readValue(json,Student.class) 
     * (2)转换为List,如List<Student>,将第二个参数传递为Student 
     * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List 
     *  
     * @param jsonStr 
     * @param valueType 
     * @return 
     */  
    public static <T> T readValue(String jsonStr, Class<T> valueType) {  
        if (objectMapper == null) {  
            objectMapper = new ObjectMapper();  
        }  
  
        try {  
            return objectMapper.readValue(jsonStr, valueType);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return null;  
    }  
      
    /** 
     * json数组转List 
     * @param jsonStr 
     * @param valueTypeRef 
     * @return 
     */  
    public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef){  
        if (objectMapper == null) {  
            objectMapper = new ObjectMapper();  
        }  
  
        try {  
            return objectMapper.readValue(jsonStr, valueTypeRef);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return null;  
    }  
  
    /** 
     * 把JavaBean转换为json字符串 
     *  
     * @param object 
     * @return 
     */  
    public static String toJSon(Object object) {  
        if (objectMapper == null) {  
            objectMapper = new ObjectMapper();  
        }  
  
        try {  
            return objectMapper.writeValueAsString(object);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return null;  
    }  
    
    public static String extractJsonValue(String msgJson, String key) {
		JSONObject jsonObject = new JSONObject().fromObject(msgJson);
		
		if(!jsonObject.has(key) || jsonObject.getString(key) == null)
			return "";
		else
			return jsonObject.getString(key);
	}
    
    public static <T> T extractJsonArray(String msgJson, String key, Class<T> valueType) {
		JSONObject jsonObject = new JSONObject().fromObject(msgJson);
		
//		JSONArray jsonArray = JSONArray.fromObject(data.toString());
		if(!jsonObject.has(key) || jsonObject.getString(key) == null)
			return null;
		else {
			String value = jsonObject.getString(key).toString();
			if(value == null || value.equals(""))
				value = "[]";
			return readValue("{\"" + key + "\":" + value + "}", valueType);
		}
	}

}
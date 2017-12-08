package com.ibm.mics.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SessionUtil {

	private static Logger logger = Logger.getLogger(SessionUtil.class);
	
	public static boolean isSessionValid(HttpServletRequest request) {
		
		//获取session
		HttpSession session = request.getSession(false);
		if(session == null)
			return false;
		else
			return true;
	}
	
	public static boolean verifyUser(HttpServletRequest request, String id) {
		
		if(isSessionValid(request)) {
			//获取session
			HttpSession session = request.getSession(false);
			String userId = (String) session.getAttribute("userId");
			logger.debug("Verify - User: " + userId + " ; SessionId: "+ session.getId());
			if(userId.equals(id))
				return true;
			else
				return false;
			
		} else
			return false;
	}
	
	public static boolean verifyOrg(HttpServletRequest request, String id) {
		
		if(isSessionValid(request)) {
			//获取session
			HttpSession session = request.getSession(false);
			String orgId = (String) session.getAttribute("orgId");
			logger.debug("Verify - Org: " + orgId + " ; SessionId: "+ session.getId());
			if(orgId.equals(id))
				return true;
			else
				return false;
			
		} else
			return false;
	}
}

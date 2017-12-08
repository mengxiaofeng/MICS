package com.ibm.mics.services;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.ibm.mics.entity.base.Constants;
import com.ibm.mics.entity.base.Error;
import com.ibm.mics.entity.base.StrMessage;
import com.ibm.mics.entity.user.User;
import com.ibm.mics.entity.user.UserParameters;
import com.ibm.mics.sql.DAO.UserDAO;
import com.ibm.mics.util.JacksonUtil;
import com.ibm.mics.util.PropertiesUtil;
import com.ibm.mics.util.SessionUtil;

@Path("user")
public class UserServices {
	private static Logger logger = Logger.getLogger(UserServices.class);
	
	private static final String resetPW = PropertiesUtil.getInstance().getAppPropertyStr(PropertiesUtil.ResetPW);
	private static final int sessionTimeOut = PropertiesUtil.getInstance().getAppPropertyInt(PropertiesUtil.SessionTimeout); // minutes
	private static final boolean encodePW = PropertiesUtil.getInstance().getAppPropertyBoolean(PropertiesUtil.EncodePW);
	
	/* User Login */
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
//	@Consumes("application/x-www-form-urlencoded")
//	@Produces("text/plain")
	public String login(String reqMsgJson, @Context HttpServletRequest request) {
		String resMsgJson = "";
		StrMessage msg = new StrMessage();
		List<Error> errors = new ArrayList<Error>();
		Error error = new Error();
		
		logger.info(String.format("请求报文: %s", reqMsgJson));
		
		UserParameters paras = JacksonUtil.readValue(reqMsgJson, UserParameters.class);
		String userId = paras.getUserId();
		String password = paras.getPassword();
		
		if(encodePW)
			password = mdPassword(password, userId);
		
		UserDAO db = new UserDAO();
		User user = new User();
		try {
			user = db.getUserByID(userId);
			
			if(user == null) {
				logger.info(String.format("%s : %s", PropertiesUtil.getInstance().getErrMsg(PropertiesUtil.UserNotRegisterMsg), userId));
				
				error.setCode(PropertiesUtil.getInstance().getErrId(PropertiesUtil.UserNotRegisterId));
				error.setStatement(PropertiesUtil.getInstance().getErrMsg(PropertiesUtil.UserNotRegisterMsg));
				errors.add(error);
				
				msg.setRetCode(Constants.FAIL);
				msg.setTranCode("USER0001");
				msg.setError(errors);
				msg.setDebug(PropertiesUtil.getInstance().getErrMsg(PropertiesUtil.UserNotRegisterMsg));
				
				resMsgJson = JacksonUtil.toJSon(msg);
				logger.info(String.format("返回报文: %s", resMsgJson));
				return resMsgJson;
			}
		} catch (Exception e) {
			logger.error(PropertiesUtil.getInstance().getErrMsg(PropertiesUtil.DBExceptionMsg,
					new String[] { e.getMessage().toString() }));

			error.setCode(PropertiesUtil.getInstance().getErrId(PropertiesUtil.DBExceptionId));
			error.setStatement(PropertiesUtil.getInstance().getErrMsg(PropertiesUtil.DBExceptionMsg,
					new String[] { e.getMessage().toString() }));
			errors.add(error);
			
			msg.setRetCode(Constants.FAIL);
			msg.setTranCode("USER0001");
			msg.setError(errors);
			msg.setDebug("登录失败");
			
			resMsgJson = JacksonUtil.toJSon(msg);
			logger.info(String.format("返回报文: %s", resMsgJson));
			return resMsgJson;
		}
		
		logger.info(String.format("本地数据库校验用户密码: %s", userId));
		if(!user.getPassword().equals(password)) {
			logger.info(String.format("%s : %s", PropertiesUtil.getInstance().getErrMsg(PropertiesUtil.UserWrongPWMsg), userId));
			
			error.setCode(PropertiesUtil.getInstance().getErrId(PropertiesUtil.UserWrongPWId));
			error.setStatement(PropertiesUtil.getInstance().getErrMsg(PropertiesUtil.UserWrongPWMsg));
			errors.add(error);
			
			msg.setRetCode(Constants.FAIL);
			msg.setTranCode("USER0001");
			msg.setError(errors);
			msg.setDebug(PropertiesUtil.getInstance().getErrMsg(PropertiesUtil.UserWrongPWMsg));
			
			resMsgJson = JacksonUtil.toJSon(msg);
			logger.info(String.format("返回报文: %s", resMsgJson));
			return resMsgJson;
		}
		
		// Set Session
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(sessionTimeOut*60); // seconds
		session.setAttribute("userId", userId);;
//		String sessionID = session.getId();
//		logger.info("Login - User: " + userId + " ; SessionId: "+ sessionID);
		
		msg.setRetCode(Constants.SUCCESS);
		msg.setTranCode("USER0001");

		List<String> dataStrList = new ArrayList<String>();
		dataStrList.add(JacksonUtil.toJSon(user));
		msg.setData(dataStrList);
//		msg.setDebug(sessionID);
		
		// remind to force to change password when first login
		String initPW = mdPassword(resetPW, userId);
		if(password.equals(initPW))
			msg.setDebug("登陆成功");
		else
			msg.setDebug("请修改您的初始密码");
		
		resMsgJson = JacksonUtil.toJSon(msg);
		logger.info(String.format("返回报文: %s", resMsgJson));
		
		return resMsgJson;
	}
	
	/* User Logout */
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
//	@Consumes("application/x-www-form-urlencoded")
//	@Produces("text/plain")
	public String logout(@Context HttpServletRequest request) {
		String resMsgJson = "";
		StrMessage msg = new StrMessage();
		
		if(SessionUtil.isSessionValid(request)) {
			//获取session
			HttpSession session = request.getSession(false);
			logger.debug("Logout - User: " + session.getAttribute("userId") + " ; SessionId: "+ session.getId());
			session.invalidate();
		}
		
		msg.setRetCode(Constants.SUCCESS);
		msg.setTranCode("USER0002");
		msg.setDebug("注销成功");
		
		resMsgJson = JacksonUtil.toJSon(msg);
		logger.info(String.format("返回报文: %s", resMsgJson));
		
		return resMsgJson;
	}
	
	private static String mdPassword(String password, String userId) {
		String md = "";
		try {
			String passw = "{dhjdfu34i34u34-zmew8732dfhjd-";
			String useri = "dfhjdf8347sdhxcye-ehjcbeww34}";

			String pass = password + passw + userId + useri;

			md = md5(pass);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return md;
	}
	
	private static String md5(String str) {
		try {
			byte[] returnByte = str.getBytes("utf-8");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			returnByte = md5.digest(str.getBytes("utf-8"));
			StringBuffer buf = new StringBuffer("");
			int i;
			for (int offset = 0; offset < returnByte.length; offset++) {
				i = returnByte[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
}
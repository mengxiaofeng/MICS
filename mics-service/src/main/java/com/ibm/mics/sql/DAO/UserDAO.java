package com.ibm.mics.sql.DAO;

import org.apache.ibatis.session.SqlSession;

import com.ibm.mics.entity.user.User;
import com.ibm.mics.util.DBUtil;

public class UserDAO {

	///////////////////////////////////////////////////////////////////////////
	/// User
	///////////////////////////////////////////////////////////////////////////
	
	public User getUserByID(String userID) throws Exception {
		SqlSession session = DBUtil.getInstance().getSession();
		String statement = "com.ibm.mics.sql.mapper.UserMapper.selectUserByID";
		User user;
		try {
			user = session.selectOne(statement, userID);
            session.rollback(true);  // just a select; rollback
        } finally {
            session.close();
        }
		return user;
	}
	
}

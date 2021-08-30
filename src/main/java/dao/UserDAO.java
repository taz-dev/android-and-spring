package dao;

import org.apache.ibatis.session.SqlSession;

import vo.UserVO;

public class UserDAO {

	SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession; 
	}
	
	public String register(UserVO vo) {

		try { 
			//회원가입이 되었을 때 success라는 값을 돌려줌
			sqlSession.insert("u.insert_user", vo);
			return "{res:[{'result':'success'}]}";
		
		}catch (Exception e) {
			//아이디가 중복되었을 때 fail이라는 값을 돌려줌
			return "{res:[{'result':'fail'}]}";
		}
		
	}//register()

	public String login(UserVO vo) {
	
		UserVO user = sqlSession.selectOne("u.login_user", vo);
		
		if( user == null ) {
			return "{res:[{'result':'fail'}]}";
		}else{
			return "{res:[{'result':'success'}]}";
		}
		
	}//login()
	
	
	
}

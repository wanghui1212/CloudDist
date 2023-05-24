package com.wanghui.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.wanghui.pojo.User;

public class UserDao {

	

	// 获得连接
	public static Connection getConnection() {
		
        String url = "jdbc:mysql://192.168.137.128:3306/panUser?serverTimezone=UTC&useSSL=false";

        String name = "root";
        String password = "root";
        Connection conn = null;
       
        	try {
    			Class.forName("com.mysql.cj.jdbc.Driver");
    			conn = DriverManager.getConnection(url, name, password);
    			System.out.println("conn = "+ conn);
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
     
		
		return conn;
	}
//	关闭连接
	public void closeConnection(Connection con) throws Exception{
        if(con!=null){
            con.close();
        }
    }
	// 根据用户名查找用户密码
	public User findUser(String username) {

		String sql = "select * from panuser where user=?";
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = new User();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user.setUser(rs.getString("user"));
				user.setPassword(rs.getString("password"));
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
//		closeCon(con);
		return user;
	}

	// 添加用户
	public boolean addUser(String username, String psw) {
		if(findUser(username)!=null) {
			//该用户已存在
			return false;
		}
		
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO panuser(user,password) VALUES(?,?)";
		boolean res = false;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, psw);
			res = (pstmt.executeUpdate() == 1);
		} catch (SQLException e) {
			if (!e.getMessage().contains("PRIMARY")) {
				e.printStackTrace();
			}
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	public static void main(String[] args) {
		// 测试方法
		System.out.println((new UserDao().findUser("wanghui")).getPassword());
		new UserDao().addUser("w", "123");
	}
}

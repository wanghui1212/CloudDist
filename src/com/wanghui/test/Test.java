package com.wanghui.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.hadoop.fs.FSDataInputStream;
import org.omg.CORBA.portable.InputStream;

import com.wanghui.dao.UserDao;
public class Test {

    public static void main(String[] args) throws Exception {
    	String cloudPath="wanghui/test2.txt";
		System.out.println(com.wanghui.dao.HdfsDao.downloadFile(cloudPath) instanceof FSDataInputStream);

    	Statement stmt = null;
    	ResultSet rs = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://192.168.137.128:3306/panUser?serverTimezone=UTC&useSSL=false";
        String name = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, name, password);

        // 3.conn.isClosed()方法判断连接是否关闭；返回true：连接关闭；返回false：连接未关闭
        if(conn.isClosed()){
            // 返回true：连接关闭
            System.out.println("连接失败！");
        }else{
            // 返回false：连接未关闭
            System.out.println("连接成功！");
          //3.获取语句对象
            
			stmt = conn.createStatement();
			//定义一个sql语句
			String sql = "select * from panuser";
			//4.语句对象将sql语句发送到数据库上,执行后得到结果集
			rs = stmt.executeQuery(sql);
			//5.遍历结果集,通过向下移动光标,光标指向哪一行就可以获取当前记录行的各个字段的值
			while(rs.next()){
				//获取当前记录行的各个字段的值,根据字段名获取字段值
				String user = rs.getString("user");
				String pwd = rs.getString("password");
				//打印当前行各个字段值
				System.out.println(user+","+pwd);
        }
    }
}
}


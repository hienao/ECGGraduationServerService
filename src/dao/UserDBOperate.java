/**
 * 
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.UserInfo;

/**
 * @author Administrator
 *对用户数据进行增删改查
 */
public class UserDBOperate {

	/**
	 * 
	 */
	private String sql = null;  
    private DBHelper db1 = null;  
    private ResultSet ret = null; 
	public UserDBOperate() {
		// TODO Auto-generated constructor stub
	}
	public boolean isUserExistByName(UserInfo userInfo) {//返回true则该用户存在，反之不存在
		int rowCount = 0;//查询出的结果的行数
		sql="select count(*) from user where name='"+userInfo.getName()+"'";
		db1 = new DBHelper(sql);//创建DBHelper对象  
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
	            rowCount = ret.getInt(1);  
	            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("安姓名查找用户ID失败");
			e.printStackTrace();
		}//执行语句，得到结果集  
		finally{
			db1.close();//关闭连接  
		}
        if (rowCount>0) {
			return true;
		} else {
			return false;
		}
		
	}
	public boolean isUserExistById(long userid) {//返回true则该用户存在，反之不存在
		int rowCount = 0;//查询出的结果的行数
		sql="select count(*) from user where userid="+userid;
		db1 = new DBHelper(sql);//创建DBHelper对象  
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
	            rowCount = ret.getInt(1);  
	            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("按ID查找用户是否存在失败");
			e.printStackTrace();
		}//执行语句，得到结果集  
		finally{
			db1.close();//关闭连接  
		}
        if (rowCount>0) {
			return true;
		} else {
			return false;
		}
		
	}
	public int userAdd(UserInfo userInfo) {//返回影响记录的行数
		int result=0;//影响记录的行数
		if (isUserExistByName(userInfo)) {//检测用户是否存在，若存在则不添加
			System.out.println("该用户已存在，停止添加");
		} else {
			sql="insert into user(name,photopath,sex,age,phone,pwd)values('"+userInfo.getName()+"','"+
					userInfo.getPhotopath()+"','"+userInfo.getSex()+"',"+userInfo.getAge()+","+userInfo.getPhone()
					+",'"+userInfo.getPwdString()+"')";
					try {
						db1 = new DBHelper(sql);//创建DBHelper对象  
						result=db1.pst.executeUpdate(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally{
						db1.close();//关闭连接  
					}
		}
		return result;
	}
	//通过各个属性添加用户
	public int userAddByString(String name,String photopath,String sex,int age,long phone,String pwd) {
		int result=0;//影响记录的行数q
		UserInfo userInfo=new UserInfo(age, phone, name, photopath, sex, pwd);
		result=userAdd(userInfo);
		return result;
	}
	
	public int userDelete(long userid) {//返回影响记录的行数
		int result=0;//影响记录的行数
		if (isUserExistById(userid)) {
			sql="DELETE FROM user WHERE userid="+userid;
			try {
				db1 = new DBHelper(sql);//创建DBHelper对象  
				result=db1.pst.executeUpdate(sql);
				String sql1="DELETE FROM caiji WHERE userid="+userid;
				db1.pst.executeUpdate(sql1);
				String sql2="DELETE FROM yangben WHERE userid="+userid;
						db1.pst.executeUpdate(sql2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("用户信息删除失败");
				e.printStackTrace();
			}
			finally{
				db1.close();//关闭连接  
			}
		} else {
			System.out.println("该用户id不存在，无法删除");
		}
		return result;
	}
	public int userModify(long userid,UserInfo userInfo) {//修改指定ID的用户的信息
		int result=0;//影响记录的行数
		if (isUserExistById(userid)) {
			sql="UPDATE user SET NAME='"+userInfo.getName()+"',photopath='"+userInfo.getPhotopath()
					+"',sex='"+userInfo.getSex()+"',age="+userInfo.getAge()
					+",phone="+userInfo.getPhone()+",pwd='"+userInfo.getPwdString()
					+"' WHERE userid="+userid;
			try {
				db1 = new DBHelper(sql);//创建DBHelper对象  
				result=db1.pst.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("用户信息修改失败");
				e.printStackTrace();
			}
			finally{
				db1.close();//关闭连接  
			}
		} else {
			System.out.println("该用户id不存在，无法修改");
		}
		return result;
	}
	
	public long  getUserIdByName(String name) {//通过名字获取用户ID
		int id=0;
		sql="select * from user where name='"+name+"'";
		db1 = new DBHelper(sql);//创建DBHelper对象  
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
            id = ret.getInt(1);  
            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("安姓名查找用户ID失败");
			e.printStackTrace();
		}//执行语句，得到结果集  
		finally{
			db1.close();//关闭连接  
		}
		return id;
	}

	public UserInfo getUserInfoById(long userid) {//通过ID获取用户信息
		sql="select * from user where userid="+userid;
		db1 = new DBHelper(sql);//创建DBHelper对象  
		UserInfo userInfo=new UserInfo();
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
			userInfo.setUserid(ret.getLong(1));
            userInfo.setName(ret.getString(2));
            userInfo.setPhotopath(ret.getString(3));
            userInfo.setSex(ret.getString(4));
            userInfo.setAge(ret.getInt(5));
            userInfo.setPhone(ret.getLong(6));
            userInfo.setPwdString(ret.getString(7));
            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			System.out.println("按ID获取用户信息失败,可能是用户ID不存在");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行语句，得到结果集  
		finally{
			db1.close();//关闭连接  
		}
		return userInfo;
	}
	public List<UserInfo> getAllUserInfo() {//获取所有用户信息
		sql="select * from user";
		db1 = new DBHelper(sql);//创建DBHelper对象  
		List<UserInfo>list=new ArrayList<UserInfo>();
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
				UserInfo userInfo=new UserInfo();
				userInfo.setUserid(ret.getLong(1));
	            userInfo.setName(ret.getString(2));
	            userInfo.setPhotopath(ret.getString(3));
	            userInfo.setSex(ret.getString(4));
	            userInfo.setAge(ret.getInt(5));
	            userInfo.setPhone(ret.getLong(6));
	            userInfo.setPwdString(ret.getString(7));
            list.add(userInfo);
            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			System.out.println("获取所有用户信息失败");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行语句，得到结果集  
		finally{
			db1.close();//关闭连接  
		}
		return list;
	}
}

/**
 * 
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.YangbenWaveInfo;

/**
 * @author Administrator
 *对样本波形数据进行增删改查
 */
public class YangbenWaveDBOperate {

	/**
	 * 
	 */
	private String sql = null;  
    private DBHelper db1 = null;  
    private ResultSet ret = null; 
	public YangbenWaveDBOperate() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean isYangbenWaveExistById(long waveid) {//判断指定ID的样本波形是否存在,一般用于修改，删除
		int rowCount = 0;//查询出的结果的行数
		sql="select count(*) from yangben where waveid="+waveid;
		db1 = new DBHelper(sql);//创建DBHelper对象  
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
	            rowCount = ret.getInt(1);  
	            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("按样本波形id查找样本波形是否存在失败");
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
	
	public int yangbenWaveAdd(YangbenWaveInfo yangbenWaveInfo) {//波形添加
		int result=0;//影响记录的行数
		sql="insert into yangben(userid,time,data)values("+yangbenWaveInfo.getUserid()+","+
				yangbenWaveInfo.getTime()+",'"+yangbenWaveInfo.getData()+"')";
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
		return result;
	}
	public int yangbenWaveAddByString(long userid,long time,String data) {//通过各个属性添加样本波形
		int result=0;//影响记录的行数
		YangbenWaveInfo yangbenWaveInfo=new YangbenWaveInfo(userid,time,data);
		result=yangbenWaveAdd(yangbenWaveInfo);
		return result;
	}
	public List<YangbenWaveInfo> getAllWaveInfoByUserId(long userid) {//通过样本波形用户ID获取样本波形数据
		sql="select * from yangben where userid="+userid;
		db1 = new DBHelper(sql);//创建DBHelper对象  
		List<YangbenWaveInfo>list=new ArrayList<YangbenWaveInfo>();
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
				YangbenWaveInfo yangbenWaveInfo=new YangbenWaveInfo();
				yangbenWaveInfo.setWaveid(ret.getLong(1));
				yangbenWaveInfo.setUserid(ret.getLong(2));
				yangbenWaveInfo.setTime(ret.getLong(3));
				yangbenWaveInfo.setData(ret.getString(4));
				list.add(yangbenWaveInfo);
            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			System.out.println("按用户ID获取样本波形信息失败,可能是该样本波形用户id不存在");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行语句，得到结果集  
		finally{
			db1.close();//关闭连接  
		}
		return list;
	}
	public YangbenWaveInfo getWaveInfoById(long waveid) {//通过样本波形ID获取样本波形数据
		sql="select * from yangben where waveid="+waveid;
		db1 = new DBHelper(sql);//创建DBHelper对象  
		YangbenWaveInfo yangbenWaveInfo=new YangbenWaveInfo();
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
				yangbenWaveInfo.setWaveid(ret.getLong(1));
				yangbenWaveInfo.setUserid(ret.getLong(2));
				yangbenWaveInfo.setTime(ret.getLong(3));
				yangbenWaveInfo.setData(ret.getString(4));
            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			System.out.println("按ID获取样本波形信息失败,可能是该样本波形id不存在");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行语句，得到结果集  
		finally{
			db1.close();//关闭连接  
		}
		return yangbenWaveInfo;
	}
	public int  yangbenWaveDelete(long waveid) {//返回影响的行数
		int result=0;//影响记录的行数
		if (isYangbenWaveExistById(waveid)) {
			sql="DELETE FROM yangben WHERE waveid="+waveid;
			try {
				db1 = new DBHelper(sql);//创建DBHelper对象  
				result=db1.pst.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("样本波形信息删除失败");
				e.printStackTrace();
			}
			finally{
				db1.close();//关闭连接  
			}
		} else {
			System.out.println("该样本波形id不存在,无法删除");
		}
		return result;
	}
	public int yangbenWaveModify(long waveid,YangbenWaveInfo yangbenWaveInfo) {
		int result=0;//影响记录的行数
		if (isYangbenWaveExistById(waveid)) {
			sql="UPDATE yangben SET userid="+yangbenWaveInfo.getUserid()+",time="+yangbenWaveInfo.getTime()
					+",data="+yangbenWaveInfo.getData()+" WHERE waveid="+waveid;
			try {
				db1 = new DBHelper(sql);//创建DBHelper对象  
				result=db1.pst.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("样本波形信息修改失败");
				e.printStackTrace();
			}
			finally{
				db1.close();//关闭连接  
			}
		} else {
			System.out.println("该样本波形id不存在,无法修改");
		}
		return result;
	}
}

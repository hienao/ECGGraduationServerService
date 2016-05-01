/**
 * 
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.WaveUtils;

import model.FileInfo;

/**
 * @author Administrator
 *
 */
public class FileDBOperate {

	private String sql = null;  
    private DBHelper db1 = null;  
    private ResultSet ret = null;
	/**
	 * 
	 */
	public FileDBOperate() {
		super();
	} 
	public boolean isFileExistById(long fileid) {//判断指定ID的文件数据是否存在,一般用于修改，删除
		int rowCount = 0;//查询出的结果的行数
		sql="select count(*) from caiji where fileid="+fileid;
		db1 = new DBHelper(sql);//创建DBHelper对象  
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
	            rowCount = ret.getInt(1);  
	            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("按文件id查找波形是否存在失败");
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
	public int fileAdd(FileInfo fileInfo) {//文件添加
		int result=0;//影响记录的行数
		sql="insert into caiji(userid,time,filecontents)values("+fileInfo.getUserid()+","+
				fileInfo.getTime()+",'"+WaveUtils.waveCutAvg(fileInfo.getFilecontents())+"')";
				try {
					db1 = new DBHelper(sql);//创建DBHelper对象  
					result=db1.pst.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("文件添加到数据库失败");
					e.printStackTrace();
				}
				finally{
					db1.close();//关闭连接  
				}
		return result;
	}
	public int fileAddByString(long userid,long time,String filecontents) {//通过各个属性添加文件
		int result=0;//影响记录的行数
		FileInfo fileInfo=new FileInfo(userid, time, filecontents);
		result=fileAdd(fileInfo);
		return result;
	}
	public List<FileInfo> getFileInfoByUserId(long userid) {//通过用户ID获取文件数据
		sql="select * from caiji where userid="+userid;
		db1 = new DBHelper(sql);//创建DBHelper对象  
		List<FileInfo>list=new ArrayList<FileInfo>();
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
				FileInfo fileInfo=new FileInfo();
				fileInfo.setFileid(ret.getLong(1));
				fileInfo.setUserid(ret.getLong(2));
				fileInfo.setTime(ret.getLong(3));
				fileInfo.setFilecontents(ret.getString(4));
				list.add(fileInfo);
            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			System.out.println("按用户ID获取文件信息失败,可能是该用户id不存在");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行语句，得到结果集  
		finally{
			db1.close();//关闭连接  
		}
		return list;
	}
	public FileInfo getFileInfoById(long fileid) {//通过文件ID获取波形数据
		sql="select * from caiji where fileid="+fileid;
		db1 = new DBHelper(sql);//创建DBHelper对象  
		FileInfo fileInfo=new FileInfo();
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {  
				fileInfo.setFileid(ret.getLong(1));
				fileInfo.setUserid(ret.getLong(2));
				fileInfo.setTime(ret.getLong(3));
				fileInfo.setFilecontents(ret.getString(4));
            }//显示数据  
			 ret.close();  
		} catch (SQLException e) {
			System.out.println("按ID获取文件信息失败,可能是该文件id不存在");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行语句，得到结果集  
		finally{
			db1.close();//关闭连接  
		}
		return fileInfo;
	}
	public int  fileDelete(long fileid) {//返回影响的行数
		int result=0;//影响记录的行数
		if (isFileExistById(fileid)) {
			sql="DELETE FROM caiji WHERE fileid="+fileid;
			try {
				db1 = new DBHelper(sql);//创建DBHelper对象  
				result=db1.pst.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("文件信息删除失败");
				e.printStackTrace();
			}
			finally{
				db1.close();//关闭连接  
			}
		} else {
			System.out.println("该文件id不存在,无法删除");
		}
		return result;
	}
	public int fileModify(long fileid,FileInfo fileInfo) {
		int result=0;//影响记录的行数
		if (isFileExistById(fileid)) {
			sql="UPDATE caiji SET userid="+fileInfo.getUserid()+",time="+fileInfo.getTime()
					+",filecontents='"+fileInfo.getFilecontents()+"' WHERE fileid="+fileid;
			try {
				db1 = new DBHelper(sql);//创建DBHelper对象  
				result=db1.pst.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("文件信息修改失败");
				e.printStackTrace();
			}
			finally{
				db1.close();//关闭连接  
			}
		} else {
			System.out.println("该文件id不存在,无法修改");
		}
		return result;
	}
}

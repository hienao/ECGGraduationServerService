/**
 * 
 */
package service;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import model.FileInfo;
import model.UserInfo;

import model.YangbenWaveInfo;
import utils.MyJson;
import utils.RPeakDetection;

import com.google.gson.Gson;
import dao.DBHelper;
import dao.FileDBOperate;
import dao.UserDBOperate;

import dao.YangbenWaveDBOperate;
import ecg.Compare;

/**
 * @author Administrator 对外接口
 */
public class MyService {

	/**
	 * 
	 */
	public String sql = null;
	public DBHelper db1 = null;
	public ResultSet ret = null;
	public Gson gson = new Gson();
	public FileDBOperate fileDBOperate = new FileDBOperate();
	public YangbenWaveDBOperate yangbenWaveDBOperate = new YangbenWaveDBOperate();
	public UserDBOperate userDBOperate = new UserDBOperate();

	public MyService() {
		// TODO Auto-generated constructor stub
	}

	public boolean isNameExit(String name) {
		UserInfo userInfo = new UserInfo();
		userInfo.setName(name);
		boolean result = userDBOperate.isUserExistByName(userInfo);
		System.out.println("用户名是否存在：" + result);
		return result;
	}

	public String getAllUserInfo() { // 获取数据库中所有的用户信息，返回json字符串
		List<UserInfo> list = userDBOperate.getAllUserInfo();
		String jsonString = MyJson.userInfoList2Json(list);
		return jsonString;
	}

	public String getUserInfoById(long userid) { // 通过用户Id获取用户信息，返回json字符串
		UserInfo userInfo = userDBOperate.getUserInfoById(userid);
		String jsonString = MyJson.user2Json(userInfo);
		return jsonString;
	}

	public long getUserIdByName(String name) {// 通过姓名获取用户ID
		return userDBOperate.getUserIdByName(name);
	}

	public int modifyUserById(long userid, String newUserInfojsonString) {
		// 判断用户是否存在
		UserInfo newUserInfo = MyJson.json2User(newUserInfojsonString);
		System.out.println(newUserInfo.toString());
		
		int result = userDBOperate.userModify(userid, newUserInfo);
		if (result > 0) {
			System.out.println("用户信息修改成功");
		} else {
			System.out.println("用户信息修改失败");
		}
		return result;
	}

	public int deleteUser(long userid) {
		int result = userDBOperate.userDelete(userid);
		if (result > 0) {
			System.out.println("用户删除成功");
		} else {
			System.out.println("用户删除失败");
		}
		return result;
	}

	public int addUser(String userinfojsonString) { // 1为添加成功0为添加失败
		UserInfo userInfo = MyJson.json2User(userinfojsonString);
		int result = userDBOperate.userAdd(userInfo);
		if (result > 0) {
			System.out.println("用户添加成功");
		} else {
			System.out.println("用户添加失败");
		}
		return result;
	}

	/********************************************************************************************/
	public String getAllFileDataByUserId(long userid) {// 获取指定用户在数据库中的所有波形数据，返回json字符串
		List<FileInfo> list = fileDBOperate.getFileInfoByUserId(userid);
		String jsonString = MyJson.fileInfoList2Json(list);
		return jsonString;
	}

	public int deleteFile(long fileid) {
		int result = fileDBOperate.fileDelete(fileid);
		if (result > 0) {
			System.out.println("文件删除成功");
		} else {
			System.out.println("文件删除失败");
		}
		return result;
	}

	public int addFile(String filejsonString) {
		FileInfo fileInfo = MyJson.json2File(filejsonString);
		int result = fileDBOperate.fileAdd(fileInfo);
		if (result > 0) {
			System.out.println("文件添加成功");
		} else {
			System.out.println("文件添加失败");
		}
		return result;
	}

	/*******************************************************************************************/
	public String getAllYangbenWaveDataByUserId(long userid) {// 获取指定用户在数据库中的所有样本波形数据，返回json字符串
		List<YangbenWaveInfo> list = yangbenWaveDBOperate.getAllWaveInfoByUserId(userid);
		String jsonString = MyJson.yangbenwaveInfoList2Json(list);
		return jsonString;
	}

	public int deleteYangbenWave(long waveid) {
		int result = yangbenWaveDBOperate.yangbenWaveDelete(waveid);
		if (result > 0) {
			System.out.println("样本波形信息删除成功");
		} else {
			System.out.println("样本波形信息删除失败");
		}
		return result;
	}

	public int addYangbenWave(String yangbenjsonString) {
		YangbenWaveInfo yangbenWaveInfo = MyJson
				.json2YangbenWave(yangbenjsonString);
		int result = yangbenWaveDBOperate.yangbenWaveAdd(yangbenWaveInfo);
		if (result > 0) {
			System.out.println("样本添加成功");
		} else {
			System.out.println("样本添加失败");
		}
		return result;
	}

	public String Recognize(String jsondataFilteredString) {// 查询出相似度最高的用户信息返回回去//输入为人工选择的数据
		Compare compare = new Compare();
		UserInfo useridInfo = compare.Recognize(jsondataFilteredString);
		UserInfo userInfo = userDBOperate.getUserInfoById(useridInfo
				.getUserid());
		String userString = MyJson.user2Json(userInfo);
		return userString;

	}
	public String RecognizeRaw(String jsondataFilteredString) {// 查询出相似度最高的用户信息返回回去//输入为原始数据
		float datafile[]=MyJson.json2FloatArray(jsondataFilteredString);
		float tempRPeak[] = RPeakDetection.RPeakRecognize(datafile);
		for (int i = 1; i < tempRPeak.length - 9; i++) {//第一个波形和最后一个波形可能不全，舍弃每次取5个
            float onedata[] = Arrays.copyOfRange(datafile, (int) tempRPeak[i], (int) tempRPeak[i + 5] + 1);
            if (Checkdatasuitable(onedata)) {
                String tempdataFilteredgson = MyJson.floatArray2Json(onedata);
                String result=Recognize(tempdataFilteredgson);
                if (MyJson.json2User(result).getUserid()!=-1) {
					return result;
				}
            }
           }
		UserInfo userInfo=new UserInfo();
		return MyJson.user2Json(userInfo);
	}
	private boolean Checkdatasuitable(float data[]){//检测截取的一段波形是否符合检测条件
        /**1.判断最大值与最小值之差是否小于100**/
        float max=data[1],min=data[1];
        float sum=0;
        for (int i=0;i<data.length;i++){
            sum+=data[i];
            if (data[i]>=max)
                max=data[i];
            else if (data[i]<=min)
                min=data[i];
        }
        if (max-min<100){
            /**2.判断其中是否有6个以上的R点**/
            float tempdataRPeak[]=RPeakDetection.RPeakRecognize(data);
            if (tempdataRPeak.length>=5){
                /**3.判断是否每个R点都在均值之上**/
                float avg=sum/data.length;
                for (int i=0;i<tempdataRPeak.length;i++){
                    if (data[(int)tempdataRPeak[i]]<=avg)
                        return false;
                }
                return true;
            }else
                return false;
        }
        return false;
    }
}

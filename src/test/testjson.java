package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.fabric.xmlrpc.base.Array;

import service.MyService;
import utils.MyJson;
import model.UserInfo;
import model.YangbenWaveInfo;

public class testjson {

	public testjson() {
		// TODO Auto-generated constructor stub
		
	}
	public static void main(String[] args) {
		Gson gson=new Gson();
		List<YangbenWaveInfo> list=null;
		String string=new MyService().getAllYangbenWaveDataByUserId(3);
		System.out.println(string);
		list=MyJson.json2YangbenWaveInfoList(string);
		Iterator iterator=list.iterator();
		while (iterator.hasNext()) {
			YangbenWaveInfo uYangbenWaveInfo=(YangbenWaveInfo) iterator.next();
			/*System.out.println(uYangbenWaveInfo.toString());*/
		}
		/*u2.toString();*/
		/*List<UserInfo>list=new  ArrayList<UserInfo>();
		list.add(userInfo);
		list.add(new UserInfo());
		String string2=MyJson.userInfoList2Json(list);
		List<UserInfo>listnewInfos=gson.fromJson(string2, new TypeToken<List<UserInfo>>() {  
        }.getType());
		for (UserInfo stu : listnewInfos) {  
            System.out.println(stu);  
        }*/
		
	}
}

package ecg;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import utils.MyJson;
import utils.WaveUtils;

import dao.FileDBOperate;
import dao.UserDBOperate;
import dao.YangbenWaveDBOperate;
import model.CompareInfo;
import model.UserInfo;
import model.WaveInfo;
import model.YangbenWaveInfo;

public class Compare {
/*2016.03.24需要修改此文件*/
	float ryuzhi=(float) 0.92;
	FileDBOperate fileDBOperate = new FileDBOperate();
	YangbenWaveDBOperate yangbenWaveDBOperate=new YangbenWaveDBOperate();
	UserDBOperate userDBOperate=new UserDBOperate();
	public Compare() {
	}
	public UserInfo Recognize(String jsondataFilteredString) {
		/*
		1.将R点数据+dataFiltered转化成waveinfolist的数据形式
		 * 参数中的userinfo date为插入波形数据结构必须，无实际意义，传参数时new一个即可
		 * 
		List<WaveInfo>list=InputInfo2WaveInfoList(jsonRPeakString, jsondataFilteredString, userInfo, date);
		List<Float>userrList=new ArrayList<Float>();//用于存储每个用户的相关系数的计算结果
		
		 * 1.获取所有用户信息，主要使用id
		 * 2.通过id获取用户的所有样本波形
		 * 3.通过波形对比得出最每个用户的波形相似系数，列表
		 * 4.遍历列表得到相似度最大，且超过最低值的用户、返回该用户、若无则返回一个id为0的用户
		 * 
		 * */
		List<UserInfo>userlist=userDBOperate.getAllUserInfo();
		List<CompareInfo>userrList=new ArrayList<CompareInfo>();//用于存储每个用户的相关系数的计算结果
		Iterator iterator=userlist.iterator();
		while (iterator.hasNext()) {
			UserInfo userInfo1 = (UserInfo) iterator.next();
			/*userrList.add(CompareFileByUser(jsondataFilteredString, userInfo1));*/
			userrList.add(new CompareInfo(userInfo1.getUserid(), CompareFileByUser(jsondataFilteredString, userInfo1)));
		}
		float  max=0;
		long maxuserid=-1;
		Iterator<CompareInfo> iterator2=userrList.iterator();
		while (iterator2.hasNext()) {
			CompareInfo compareInfo=iterator2.next();
			if (compareInfo.getR()>max) {
				max=compareInfo.getR();
				maxuserid=compareInfo.getUserid();
			}
		}
		System.out.println("max:"+max);
		UserInfo userInfo1=null;
		if (max>=ryuzhi) {
			
			userInfo1=userDBOperate.getUserInfoById(maxuserid);
		}else {
			userInfo1=new UserInfo();
			userInfo1.setUserid(-1);//表示未找到此人
		}
		System.out.println(userInfo1.toString());
		return userInfo1;
	}
	
	public float CompareFileByUser(String jsonfile,UserInfo userInfo) { //对比单次采集波形文件与指定用户样本所有样本波形，得到相关系数
		
		 /** 1.选出指定用户的所有样本波形
		 * */
		List<WaveInfo>list=MyJson.json2WaveInfoList(WaveUtils.waveCutAvg(jsonfile));
		if (list==null) {
			return 0;
		}
		List<YangbenWaveInfo>list2=yangbenWaveDBOperate.getAllWaveInfoByUserId(userInfo.getUserid());
		ArrayList<Float>rList=new ArrayList<Float>();
		/*
		 * 2.用List<WaveInfo>list中的每一个数据与指定用户的所有样本波形逐个对比，计算出每个相关系数R后取平均值、返回
		 * */
		Iterator itr=list.iterator();
		while (itr.hasNext()) {
			WaveInfo currentwaveInfo = (WaveInfo) itr.next();
			Iterator iterator2=list2.iterator();
			ArrayList<Float>rListtemp=new ArrayList<Float>();
			while (iterator2.hasNext()) {
				YangbenWaveInfo yangbenWaveInfo = (YangbenWaveInfo) iterator2.next();
				rListtemp.add(compareSingleWaveJson(currentwaveInfo.getData(), yangbenWaveInfo.getData()));
			}
			float avgrtemp=0;
			Iterator rIteratorTemp=rListtemp.iterator();
			while (rIteratorTemp.hasNext()) {
				avgrtemp += (Float) rIteratorTemp.next();
			}
			avgrtemp=avgrtemp/rListtemp.size();
			rList.add(avgrtemp);
		}
		float avgr=0;
		//掐头去尾
		Collections.sort (rList);/*
		rList.remove(rList.size()-1);
		rList.remove(0);*/
		//
		Iterator rIterator=rList.iterator();
		while (rIterator.hasNext()) {
			avgr += (Float) rIterator.next();
		}
		avgr=avgr/rList.size();
		return avgr;
	}
	    //判断两个json字符串中的单波的数据的相关系数，并返回相关系数e
    private  float compareSingleWaveJson(String currentwavejson, String yangbenjson){
        float []wavecurrent=MyJson.json2FloatArray(currentwavejson);//用于统计当前波中的数据
        float []waveyangben=MyJson.json2FloatArray(yangbenjson);//样本库中的单条样本数据数据，用于比对
        for (int i = 0; i < waveyangben.length; i++) {
		}
        float r=0;//相关系数
        //以下部分为对wavecurrent、waveyangben进行相关系数的运算
        float xavg=0,yavg=0;//x代表当前波 y代表样本数据库平均波形
        for (int i=0;i<waveyangben.length;i++){
            xavg+=wavecurrent[i];
            yavg+=waveyangben[i];
        }
        xavg/=waveyangben.length;
        yavg/=waveyangben.length;
        float fenzi=0,fenmu=0,fenmu1=0,fenmu2=0;
        for(int i=0;i<waveyangben.length;i++){
            fenzi+=(wavecurrent[i]-xavg)*(waveyangben[i]-yavg);
            fenmu1+=(wavecurrent[i]-xavg)*(wavecurrent[i]-xavg);
            fenmu2+=(waveyangben[i]-yavg)*(waveyangben[i]-yavg);
        }
        fenmu= (float) Math.sqrt(fenmu1*fenmu2);
        r=fenzi/fenmu;
        return r;
    }
    
}


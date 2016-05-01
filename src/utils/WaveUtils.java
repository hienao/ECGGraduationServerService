package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.WaveInfo;
import model.YangbenWaveInfo;

public class WaveUtils {
	public static float[]cutWaveData(float[]root,int startindex,int endindex){
        int length=endindex-startindex;
        float[]resultData=new float[length];
        for (int i=0;i<length;i++){
            resultData[i]=root[startindex+i];
        }
        return resultData;
    }
	 public static float[]waveDataEdited(float[]root){
	        float avgdata=0;
	        for (int i=0;i<root.length;i++){
	            avgdata+=root[i];
	        }
	        avgdata=avgdata/root.length;
	        for (int i=0;i<root.length;i++){
	            root[i]-=avgdata;
	        }
	        return root;
	    }
	 public static String waveCutAvg(String orginString){//将json的波形字符串中的数据删除平均值后重新封装
	        float root[]=MyJson.json2FloatArray(orginString);
	        float avgdata=0;
	        for (int i=0;i<root.length;i++){
	            avgdata+=root[i];
	        }
	        avgdata=avgdata/root.length;
	        for (int i=0;i<root.length;i++){
	            root[i]-=avgdata;
	        }
	        String result=MyJson.floatArray2Json(root);
	        return result;
	    }
}

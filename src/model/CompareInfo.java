package model;

public class CompareInfo {

	private long userid;
	private float r;
	public CompareInfo(long userid, float r) {
		super();
		this.userid = userid;
		this.r = r;
	}
	public float getR() {
		return r;
	}
	public void setR(float r) {
		this.r = r;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	
}

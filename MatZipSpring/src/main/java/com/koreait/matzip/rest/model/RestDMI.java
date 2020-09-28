package com.koreait.matzip.rest.model;

public class RestDMI extends RestVO {
	private String cd_category_nm;
	private String user_nm;
	private int cnt_favorite; // 좋아요 몇개가 있는지 ? 
	private int is_favorite; // 내가 좋아요 눌렀는지 ? 
	
	
	
	public int getIs_favorite() {
		return is_favorite;
	}
	public void setIs_favorite(int is_favorite) {
		this.is_favorite = is_favorite;
	}
	public String getCd_category_nm() {
		return cd_category_nm;
	}
	public void setCd_category_nm(String cd_category_nm) {
		this.cd_category_nm = cd_category_nm;
	}
	public String getUser_nm() {
		return user_nm;
	}
	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}
	public int getCnt_favorite() {
		return cnt_favorite;
	}
	public void setCnt_favorite(int cnt_favorite) {
		this.cnt_favorite = cnt_favorite;
	}
	
	
}

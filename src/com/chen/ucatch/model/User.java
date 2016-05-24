package com.chen.ucatch.model;

/**
 * 
 * 自定义的用户model
 * 
 * @author chenjiqiang <br>
 * @version 1.0.0 2015年10月12日 上午10:19:03 <br>
 * @see
 * @since JDK 1.4.2.6
 */
public class User {

	/** 用户表表名. */
	public static final String TABLENAME = "T_USER";


	/** 登录账号. */
	private String loginMail;

	/** 登录密码. */
	private String loginPwd;

	/** 用户名. */
	private String userName = "";

	/** 手机号码, 手机端登录用. */
	private String phone = "";

	/** 是否游客, 即仅报料用户, 1-是. */
	private int guestFlag;

	/** 性别: 1-男, 2-女, 0-未知. */
	private String sex = "";

	/** 年龄. */
	private int age;

	/** 用户类型: 0-手机用户, 1-后台管理员. */
	private int userType;

	/** 生日. */
	private String birthday;

	/** 身份证. */
	private String cidNumber;

	/** 所属组织Id */
	private String orgId = "";

	/** QQ号. */
	private String qq = "";

	/** 微信. */
	private String weixin = "";

	/** 注册日期 */
	private String registerDate = "";

	/** 用户头像id. */
	private String userPicId = "";

	/** 启用状态. */
	private int state;

	/** 集成登录的类型, 建议用大写短拼, 如: QQ, WEIXIN. */
	private String qqType;

	/** QQ集成登录后, 形成的唯一键. */
	private String qqKey;

	/** QQ上的头像ID. */
	private String qqPicId;

	/** QQ的昵称. */
	private String qqName;
	/**
	 * 是否记住密码
	 */
	private boolean rememberPwd;
	private String sortLetters; // 显示数据拼音的首字母
	// =====================================================================

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public boolean isRememberPwd() {
		return rememberPwd;
	}

	public void setRememberPwd(boolean rememberPwd) {
		this.rememberPwd = rememberPwd;
	}


	public String getLoginMail() {
		return loginMail;
	}

	public void setLoginMail(String loginMail) {
		this.loginMail = loginMail;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getGuestFlag() {
		return guestFlag;
	}

	public void setGuestFlag(int guestFlag) {
		this.guestFlag = guestFlag;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCidNumber() {
		return cidNumber;
	}

	public void setCidNumber(String cidNumber) {
		this.cidNumber = cidNumber;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getUserPicId() {
		return userPicId;
	}

	public void setUserPicId(String userPicId) {
		this.userPicId = userPicId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getQqType() {
		return qqType;
	}

	public void setQqType(String qqType) {
		this.qqType = qqType;
	}

	public String getQqKey() {
		return qqKey;
	}

	public void setQqKey(String qqKey) {
		this.qqKey = qqKey;
	}

	public String getQqPicId() {
		return qqPicId;
	}

	public void setQqPicId(String qqPicId) {
		this.qqPicId = qqPicId;
	}

	public String getQqName() {
		return qqName;
	}

	public void setQqName(String qqName) {
		this.qqName = qqName;
	}

}

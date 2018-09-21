package com.onemeter.entity;

/**
 * 新增潜客提交第一步信息
 * 
 * @author angelyin
 * @date 2015-10-13 下午2:50:01
 */
public class UpdateCustomer {

	/** 学校账号 **/
	private String school_id;
	/** 分校账号 **/
	private String branch_id;
	/** 父母名称 **/
	private String parent_name;
	/** 学生姓名 **/
	private String studentName;
	/** 手机号码 **/
	private String mobile;
	/** 来源 **/
	private String source_id;
	/** 状态 **/
	private String status_id;
	/** 添加人ID **/
	private String emp_id;
	/** 就读的学校 **/
	private String school_info;
	/** 班级 **/
	private String grade_info;
	/** 感兴趣的课程 **/
	private String kecheng;
	/** 性别 **/
	private String sex;
	/** 生日 **/
	private String birthday;
	/** 预约时间 **/
	private String leads_date;

	public String getSchool_id() {
		return school_id;
	}

	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getStatus_id() {
		return status_id;
	}

	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public String getSchool_info() {
		return school_info;
	}

	public void setSchool_info(String school_info) {
		this.school_info = school_info;
	}

	public String getGrade_info() {
		return grade_info;
	}

	public void setGrade_info(String grade_info) {
		this.grade_info = grade_info;
	}

	public String getLeads_date() {
		return leads_date;
	}

	public void setLeads_date(String leads_date) {
		this.leads_date = leads_date;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getKecheng() {
		return kecheng;
	}

	public void setKecheng(String kecheng) {
		this.kecheng = kecheng;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

}

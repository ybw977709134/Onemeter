package com.onemeter.entity;

/**
 * 获取指定潜客的信息数组
 * 
 * @author angelyin
 * @date 2015-10-23 下午3:13:23
 */
public class GgetCustomerDataById {
	private String id;
	private String school_id;
	private String branch_id;
	private String parent_name;
	private String student_name;
	private String sex;
	private String birthday;
	private String school_info;
	private String grade_info;
	private String interest_course;
	private String mobile;
	private String source_id;
	private String status_id;
	private String status_history;
	private String emp_id;
	private String customer_id;
	private String leads_date;
	private String book_date;
	private String time_stamp;
	public Sourcedata sourcedata;
	public Statusdata statusdata;
	public String memo;
	public String memo_time;

	public String getMemo() {
		return memo == null ? "" : memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo_time() {
		return memo_time == null ? "" : memo_time;
	}

	public void setMemo_time(String memo_time) {
		this.memo_time = memo_time;
	}

	/**
	 * 来源信息
	 * 
	 * @author angelyin
	 * @date 2015-10-21 上午12:32:40
	 */
	public class Sourcedata {
		private String id;
		private String school_id;
		private String source;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSchool_id() {
			return school_id;
		}

		public void setSchool_id(String school_id) {
			this.school_id = school_id;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}
	}

	/**
	 * 状态信息
	 * 
	 * @author angelyin
	 * @date 2015-10-21 上午12:32:24
	 */
	public class Statusdata {
		private String id;
		private String school_id;
		private String status;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSchool_id() {
			return school_id;
		}

		public void setSchool_id(String school_id) {
			this.school_id = school_id;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
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

	public String getInterest_course() {
		return interest_course;
	}

	public void setInterest_course(String interest_course) {
		this.interest_course = interest_course;
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

	public String getStatus_history() {
		return status_history;
	}

	public void setStatus_history(String status_history) {
		this.status_history = status_history;
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getLeads_date() {
		return leads_date;
	}

	public void setLeads_date(String leads_date) {
		this.leads_date = leads_date;
	}

	public String getBook_date() {
		return book_date;
	}

	public void setBook_date(String book_date) {
		this.book_date = book_date;
	}

	public String getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}

	public Sourcedata getSourcedata() {
		return sourcedata;
	}

	public void setSourcedata(Sourcedata sourcedata) {
		this.sourcedata = sourcedata;
	}

	public Statusdata getStatusdata() {
		return statusdata;
	}

	public void setStatusdata(Statusdata statusdata) {
		this.statusdata = statusdata;
	}

	public Bbranchdata branchdata;
	public Ccustomerdata customerdata;

	public Bbranchdata getBranchdata() {
		return branchdata;
	}

	public void setBranchdata(Bbranchdata branchdata) {
		this.branchdata = branchdata;
	}

	public Ccustomerdata getCustomerdata() {
		return customerdata;
	}

	public void setCustomerdata(Ccustomerdata customerdata) {
		this.customerdata = customerdata;
	}

	/**
	 * 分校信息
	 * 
	 * @author angelyin
	 * @date 2015-10-21 上午12:31:56
	 */
	public class Bbranchdata {
		private String id;
		private String school_id;
		private String branch_name;
		private String address;
		private String tel;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSchool_id() {
			return school_id;
		}

		public void setSchool_id(String school_id) {
			this.school_id = school_id;
		}

		public String getBranch_name() {
			return branch_name;
		}

		public void setBranch_name(String branch_name) {
			this.branch_name = branch_name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

	}

	/**
	 * 潜客信息
	 * 
	 * @author angelyin
	 * @date 2015-10-21 上午12:31:42
	 */
	public class Ccustomerdata {
		private String id;
		private String school_id;
		private String branch_id;
		private String role_id;
		private String emp_name;
		private String password;
		private String cellphone;
		private String authority;
		private String stop_flag;
		private String time_stamp;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

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

		public String getRole_id() {
			return role_id;
		}

		public void setRole_id(String role_id) {
			this.role_id = role_id;
		}

		public String getEmp_name() {
			return emp_name;
		}

		public void setEmp_name(String emp_name) {
			this.emp_name = emp_name;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getCellphone() {
			return cellphone;
		}

		public void setCellphone(String cellphone) {
			this.cellphone = cellphone;
		}

		public String getAuthority() {
			return authority;
		}

		public void setAuthority(String authority) {
			this.authority = authority;
		}

		public String getStop_flag() {
			return stop_flag;
		}

		public void setStop_flag(String stop_flag) {
			this.stop_flag = stop_flag;
		}

		public String getTime_stamp() {
			return time_stamp;
		}

		public void setTime_stamp(String time_stamp) {
			this.time_stamp = time_stamp;
		}

	}

}

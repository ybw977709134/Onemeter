package com.onemeter.entity;

import java.io.Serializable;

/**
 * 添加潜客成功后返回数据实体类
 * 
 * @author angelyin
 * @date 2015-10-15 上午11:00:07
 */
public class AddCustomerResult implements Serializable {

	private static final long serialVersionUID = -348916314064427846L;

	/** 页头类 **/
	private HHeader header;
	/** 主体类 **/
	private BBody body;

	public HHeader getHeader() {
		return header;
	}

	public void setHeader(HHeader header) {
		this.header = header;
	}

	public BBody getBody() {
		return body;
	}

	public void setBody(BBody body) {
		this.body = body;
	}

	public class HHeader {
		private int err_no;// 响应状态码
		private Ss_version s_version;
		private int user_id;// 用户ID号

		public int getErr_no() {
			return err_no;
		}

		public void setErr_no(int err_no) {
			this.err_no = err_no;
		}

		public Ss_version getS_version() {
			return s_version;
		}

		public void setS_version(Ss_version s_version) {
			this.s_version = s_version;
		}

		public int getUser_id() {
			return user_id;
		}

		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}

		/**
		 * 版本类
		 * 
		 * @author G510
		 * 
		 */
		public class Ss_version {
			private int server_version;// 版本号

			public int getServer_version() {
				return server_version;
			}

			public void setServer_version(int server_version) {
				this.server_version = server_version;
			}

		}

	}

	/**
	 * 主体
	 * 
	 * @author angelyin
	 * @date 2015-10-15 上午11:04:39
	 */
	public class BBody {
		private AaddCustomer addCustomer;

		public AaddCustomer getAddCustomer() {
			return addCustomer;
		}

		public void setAddCustomer(AaddCustomer addCustomer) {
			this.addCustomer = addCustomer;
		}

		/**
		 * 功能
		 * 
		 * @author angelyin
		 * @date 2015-10-15 上午11:06:56
		 */
		public class AaddCustomer {
			private String school_id;
			private String branch_id;
			private String parent_name;
			private String student_name;
			private int sex;
			private String birthday;
			private String school_info;
			private String grade_info;
			private String interest_course;
			private String mobile;
			private int source_id;
			private int status_id;
			private int status_history;
			private String emp_id;
			private String customer_id;
			private String book_date;
			private String leads_date;
			private String time_stamp;
			private String id;

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

			public int getSex() {
				return sex;
			}

			public void setSex(int sex) {
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

			public int getSource_id() {
				return source_id;
			}

			public void setSource_id(int source_id) {
				this.source_id = source_id;
			}

			public int getStatus_id() {
				return status_id;
			}

			public void setStatus_id(int status_id) {
				this.status_id = status_id;
			}

			public int getStatus_history() {
				return status_history;
			}

			public void setStatus_history(int status_history) {
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

			public String getBook_date() {
				return book_date;
			}

			public void setBook_date(String book_date) {
				this.book_date = book_date;
			}

			public String getLeads_date() {
				return leads_date;
			}

			public void setLeads_date(String leads_date) {
				this.leads_date = leads_date;
			}

			public String getTime_stamp() {
				return time_stamp;
			}

			public void setTime_stamp(String time_stamp) {
				this.time_stamp = time_stamp;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

		}

	}

}

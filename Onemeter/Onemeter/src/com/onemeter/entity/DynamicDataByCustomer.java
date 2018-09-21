package com.onemeter.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 历史动态实体类
 * 
 * @author angelyin
 * @date 2015-10-26 下午12:39:29
 */
public class DynamicDataByCustomer implements Serializable {

	private static final long serialVersionUID = -4857410138274943872L;
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
	 * 主体部分
	 * 
	 * @author angelyin
	 * @date 2015-10-26 下午12:41:08
	 */
	public class BBody {
		private GgetDynamicDataByCustomerId getDynamicDataByCustomerId;

		public GgetDynamicDataByCustomerId getGetDynamicDataByCustomerId() {
			return getDynamicDataByCustomerId;
		}

		public void setGetDynamicDataByCustomerId(
				GgetDynamicDataByCustomerId getDynamicDataByCustomerId) {
			this.getDynamicDataByCustomerId = getDynamicDataByCustomerId;
		}

		public class GgetDynamicDataByCustomerId {
			private String count;
			private List<DynamicData> data;

			public String getCount() {
				return count;
			}

			public void setCount(String count) {
				this.count = count;
			}

			public List<DynamicData> getData() {
				return data;
			}

			public void setData(List<DynamicData> data) {
				this.data = data;
			}

			/**
			 * 数组
			 * 
			 * @author angelyin
			 * @date 2015-10-26 下午12:44:44
			 */
			public class DynamicData {
				private String id;
				private String school_id;
				private String branch_id;
				private String emp_id;
				private String customer_id;
				private String memo;
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

				public String getMemo() {
					return memo;
				}

				public void setMemo(String memo) {
					this.memo = memo;
				}

				public String getTime_stamp() {
					return time_stamp;
				}

				public void setTime_stamp(String time_stamp) {
					this.time_stamp = time_stamp;
				}

			}
		}
	}

}

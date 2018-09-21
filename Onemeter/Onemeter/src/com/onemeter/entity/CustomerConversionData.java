package com.onemeter.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 获取潜客统计数据信息实体类
 * 
 * @author angelyin
 * @date 2015-10-17 上午11:28:59
 */
public class CustomerConversionData implements Serializable {

	private static final long serialVersionUID = -1531672724132566485L;

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

	public class BBody {
		private GgetCustomerConversionData getCustomerConversionData;

		public GgetCustomerConversionData getGetCustomerConversionData() {
			return getCustomerConversionData;
		}

		public void setGetCustomerConversionData(
				GgetCustomerConversionData getCustomerConversionData) {
			this.getCustomerConversionData = getCustomerConversionData;
		}

		public class GgetCustomerConversionData {
			private String count;
			private List<data> data;

			public String getCount() {
				return count;
			}

			public void setCount(String count) {
				this.count = count;
			}

			public List<data> getData() {
				return data;
			}

			public void setData(List<data> data) {
				this.data = data;
			}

			/**
			 * 集合
			 * 
			 * @author angelyin
			 * @date 2015-10-17 上午11:40:26
			 */
			public class data {
				private String status_id;
				private String num;
				private String status;

				public String getStatus_id() {
					return status_id;
				}

				public void setStatus_id(String status_id) {
					this.status_id = status_id;
				}

				public String getNum() {
					return num;
				}

				public void setNum(String num) {
					this.num = num;
				}

				public String getStatus() {
					return status;
				}

				public void setStatus(String status) {
					this.status = status;
				}

			}

		}

	}

}

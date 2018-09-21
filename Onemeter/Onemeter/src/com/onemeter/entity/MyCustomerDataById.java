package com.onemeter.entity;

import java.io.Serializable;
import java.util.List;

import com.onemeter.entity.AllCustomerData.BBody;
import com.onemeter.entity.AllCustomerData.HHeader;
import com.onemeter.entity.AllCustomerData.HHeader.Ss_version;

public class MyCustomerDataById implements Serializable {

	private static final long serialVersionUID = 6109724275660149045L;

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
	 * @date 2015-10-20 下午12:12:59
	 */
	public class BBody {
		private List<GgetCustomerDataById> getCustomerDataById;

		public List<GgetCustomerDataById> getGetCustomerDataById() {
			return getCustomerDataById;
		}

		public void setGetCustomerDataById(
				List<GgetCustomerDataById> getCustomerDataById) {
			this.getCustomerDataById = getCustomerDataById;
		}

	}
}

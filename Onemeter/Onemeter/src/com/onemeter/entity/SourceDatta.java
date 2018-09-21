package com.onemeter.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 获取来源接口实体类
 * 
 * @author angelyin
 * @date 2015-10-17 下午1:10:08
 */
public class SourceDatta implements Serializable {

	private static final long serialVersionUID = 4533076863442098118L;

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
		 * 版本
		 * 
		 * @author angelyin
		 * @date 2015-10-14 上午11:30:58
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
	 * @date 2015-10-14 上午11:32:49
	 */
	public class BBody {
		private List<getSourceData> getSourceData;

		public List<getSourceData> getGetSourceData() {
			return getSourceData;
		}

		public void setGetSourceData(List<getSourceData> getSourceData) {
			this.getSourceData = getSourceData;
		}

		public class getSourceData {
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

	}

}

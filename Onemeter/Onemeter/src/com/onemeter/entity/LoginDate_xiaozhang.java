package com.onemeter.entity;

import java.io.Serializable;

/**
 * 校长登录实体类
 * 
 * @author angelyin
 * @date 2015-10-16 上午11:48:07
 */
public class LoginDate_xiaozhang implements Serializable {

	private static final long serialVersionUID = 2755531252758588490L;

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
	 * @date 2015-10-16 上午11:53:20
	 */
	public class BBody {
		private ZZsbLogin zsbLogin;

		public ZZsbLogin getZsbLogin() {
			return zsbLogin;
		}

		public void setZsbLogin(ZZsbLogin zsbLogin) {
			this.zsbLogin = zsbLogin;
		}

		public class ZZsbLogin {
			private SSchool school;// 学校类
			private UUser user;// 用户类

			public SSchool getSchool() {
				return school;
			}

			public void setSchool(SSchool school) {
				this.school = school;
			}

			public UUser getUser() {
				return user;
			}

			public void setUser(UUser user) {
				this.user = user;
			}

			/**
			 * 学校信息
			 * 
			 * @author angelyin
			 * @date 2015-10-16 下午12:01:00
			 */
			public class SSchool {
				private String corp_id;// 校长用来登录的ID
				private String corp_name;
				private String corp_staff_scale;
				private String corp_member_count;
				private String logo_file_path;
				private String service_type;
				private String corp_create_timestamp;
				private String expiration;
				private String url;
				private String location;
				private String list_location;
				private String representative;
				private String representative_title;
				private String corp_type;
				private String is_active;
				private String security_level;
				private String security_ips;
				private String admin_id;
				private String password;
				private String email;
				private String uid;

				public String getCorp_id() {
					return corp_id;
				}

				public void setCorp_id(String corp_id) {
					this.corp_id = corp_id;
				}

				public String getCorp_name() {
					return corp_name;
				}

				public void setCorp_name(String corp_name) {
					this.corp_name = corp_name;
				}

				public String getCorp_staff_scale() {
					return corp_staff_scale;
				}

				public void setCorp_staff_scale(String corp_staff_scale) {
					this.corp_staff_scale = corp_staff_scale;
				}

				public String getCorp_member_count() {
					return corp_member_count;
				}

				public void setCorp_member_count(String corp_member_count) {
					this.corp_member_count = corp_member_count;
				}

				public String getLogo_file_path() {
					return logo_file_path;
				}

				public void setLogo_file_path(String logo_file_path) {
					this.logo_file_path = logo_file_path;
				}

				public String getService_type() {
					return service_type;
				}

				public void setService_type(String service_type) {
					this.service_type = service_type;
				}

				public String getCorp_create_timestamp() {
					return corp_create_timestamp;
				}

				public void setCorp_create_timestamp(
						String corp_create_timestamp) {
					this.corp_create_timestamp = corp_create_timestamp;
				}

				public String getExpiration() {
					return expiration;
				}

				public void setExpiration(String expiration) {
					this.expiration = expiration;
				}

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}

				public String getLocation() {
					return location;
				}

				public void setLocation(String location) {
					this.location = location;
				}

				public String getList_location() {
					return list_location;
				}

				public void setList_location(String list_location) {
					this.list_location = list_location;
				}

				public String getRepresentative() {
					return representative;
				}

				public void setRepresentative(String representative) {
					this.representative = representative;
				}

				public String getRepresentative_title() {
					return representative_title;
				}

				public void setRepresentative_title(String representative_title) {
					this.representative_title = representative_title;
				}

				public String getCorp_type() {
					return corp_type;
				}

				public void setCorp_type(String corp_type) {
					this.corp_type = corp_type;
				}

				public String getIs_active() {
					return is_active;
				}

				public void setIs_active(String is_active) {
					this.is_active = is_active;
				}

				public String getSecurity_level() {
					return security_level;
				}

				public void setSecurity_level(String security_level) {
					this.security_level = security_level;
				}

				public String getSecurity_ips() {
					return security_ips;
				}

				public void setSecurity_ips(String security_ips) {
					this.security_ips = security_ips;
				}

				public String getAdmin_id() {
					return admin_id;
				}

				public void setAdmin_id(String admin_id) {
					this.admin_id = admin_id;
				}

				public String getPassword() {
					return password;
				}

				public void setPassword(String password) {
					this.password = password;
				}

				public String getEmail() {
					return email;
				}

				public void setEmail(String email) {
					this.email = email;
				}

				public String getUid() {
					return uid;
				}

				public void setUid(String uid) {
					this.uid = uid;
				}

			}

			/**
			 * 用户信息
			 * 
			 * @author angelyin
			 * @date 2015-10-16 下午12:08:08
			 */
			public class UUser {
				private String admin_id;
				private String password;
				private String email;
				private String uid;

				public String getAdmin_id() {
					return admin_id;
				}

				public void setAdmin_id(String admin_id) {
					this.admin_id = admin_id;
				}

				public String getPassword() {
					return password;
				}

				public void setPassword(String password) {
					this.password = password;
				}

				public String getEmail() {
					return email;
				}

				public void setEmail(String email) {
					this.email = email;
				}

				public String getUid() {
					return uid;
				}

				public void setUid(String uid) {
					this.uid = uid;
				}

			}
		}

	}
}

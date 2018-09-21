package com.onemeter.test;

public class DemoBean {

	private String title;
	private boolean ischeckedd = false;

	/**
	 * 标识是否可以删除
	 */
	private boolean canRemove = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isIscheckedd() {
		return ischeckedd;
	}

	public void setIscheckedd(boolean ischeckedd) {
		this.ischeckedd = ischeckedd;
	}

	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}

	public DemoBean(String title, boolean canRemove) {
		this.title = title;
		this.canRemove = canRemove;
	}

	public DemoBean() {
	}

}

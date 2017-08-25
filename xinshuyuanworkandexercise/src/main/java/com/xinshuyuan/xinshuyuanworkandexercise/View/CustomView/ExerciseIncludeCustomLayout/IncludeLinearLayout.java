package com.xinshuyuan.xinshuyuanworkandexercise.View.CustomView.ExerciseIncludeCustomLayout;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * 嵌套题插件父类
 * @author Administrator
 *
 */
public class IncludeLinearLayout extends LinearLayout {

	private Long testId;
	private Integer testType;
	public IncludeLinearLayout(Context context) {
		super(context);
	}
	public Long getTestId() {
		return testId;
	}
	public void setTestId(Long testId) {
		this.testId = testId;
	}
	public Integer getTestType() {
		return testType;
	}
	public void setTestType(Integer testType) {
		this.testType = testType;
	}

}

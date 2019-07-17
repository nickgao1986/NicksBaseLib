Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.nickgao1986:NicksBaseLib:4.0'
	}


## 安卓日期控件
DatePickerDialogUtil


## 安卓提醒红点

BadgeTextView


## 安卓下拉刷新

PullToRefreshBase


## 安卓图片剪切功能
ClipLayout
ClipView


## 计步器利用传感器计步功能

TodayStepCounter


## 安卓列表滚动服务
ScrollableHelper
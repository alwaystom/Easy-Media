package com.zj.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.zj.ctrl.CameraCtrl;
import com.zj.ctrl.CloudCode;
import com.zj.ctrl.Control;
import com.zj.ctrl.LoginPlay;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

/**
 * 云台控制
 * @author ZJ
 *
 */
public class CloudCtrlController {
	
	/**
	 * @throws Exception 
	 * 
	 */
	@RequestMapping("ctrl")
	public void name() throws Exception {

		LoginPlay lp = new LoginPlay();
		// 输入摄像机ip，端口，账户，密码登录
		lp.doLogin("192.168.2.120", Convert.toShort("8000", (short) 8000), "admin",
				"admin123");
		
		CameraCtrl camera = new CameraCtrl();

		// 截取摄像机实时图片
//		boolean imgSavePath = Control.getImgSavePath(camera.getIp(), "D:\\tempFile\\3.jpg");

		if (StrUtil.equals(camera.getOp(), "up")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.TILT_UP, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.TILT_UP, CloudCode.SPEED_LV6, CloudCode.END);
		} else if (StrUtil.equals(camera.getOp(), "down")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.TILT_DOWN, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.TILT_DOWN, CloudCode.SPEED_LV6, CloudCode.END);
		} else if (StrUtil.equals(camera.getOp(), "left")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.PAN_LEFT, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.PAN_LEFT, CloudCode.SPEED_LV6, CloudCode.END);
		} else if (StrUtil.equals(camera.getOp(), "right")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.PAN_RIGHT, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.PAN_RIGHT, CloudCode.SPEED_LV6, CloudCode.END);
		} else if (StrUtil.equals(camera.getOp(), "left_up")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.UP_LEFT, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.UP_LEFT, CloudCode.SPEED_LV6, CloudCode.END);
		} else if (StrUtil.equals(camera.getOp(), "left_down")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.DOWN_LEFT, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.DOWN_LEFT, CloudCode.SPEED_LV6, CloudCode.END);
		} else if (StrUtil.equals(camera.getOp(), "right_up")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.UP_RIGHT, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.UP_RIGHT, CloudCode.SPEED_LV6, CloudCode.END);
		} else if (StrUtil.equals(camera.getOp(), "right_down")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.DOWN_RIGHT, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.DOWN_RIGHT, CloudCode.SPEED_LV6, CloudCode.END);
		} else if (StrUtil.equals(camera.getOp(), "big")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.ZOOM_IN, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.ZOOM_IN, CloudCode.SPEED_LV6, CloudCode.END);
		} else if (StrUtil.equals(camera.getOp(), "small")) {
			// 制摄像机云台控制(开启)
			Control.cloudControl(camera.getIp(), CloudCode.ZOOM_OUT, CloudCode.SPEED_LV6, CloudCode.START);
			try {
				// 让云台运行1000ms
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// 制摄像机云台控制(关闭)
			Control.cloudControl(camera.getIp(), CloudCode.ZOOM_OUT, CloudCode.SPEED_LV6, CloudCode.END);
		}



	}
}

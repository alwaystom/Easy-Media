package com.zj.ctrl;

import com.sun.jna.NativeLong;
import lombok.extern.slf4j.Slf4j;

/**
 * 摄像头控制
 * @author ZJ
 *
 */
@Slf4j
public class Control {
	
	private static HCNetSDK hCNetSDK = LoginPlay.hCNetSDK;
	
	/**
	 * 截取摄像头实时图片并存储
	 * 
	 * @param ip 摄像头ip
	 * @param path 截取图片存储的路径
	 * @return
	 */
	public static boolean getImgSavePath(String ip, String path) {
		//获取ip对应摄像头的句柄
		MyNativeLong nativeLong = TempData.getTempData().getNativeLong(ip, 0);
		if (nativeLong == null) {
			return false;
		}
		
		//用户句柄
		NativeLong lUserID = nativeLong.getlUserID();
		//通道句柄
		NativeLong lChannel = nativeLong.getlChannel();
		//创建截图质量对象 
		HCNetSDK.NET_DVR_JPEGPARA net_d = new HCNetSDK.NET_DVR_JPEGPARA();
		
		boolean result = hCNetSDK.NET_DVR_CaptureJPEGPicture(lUserID, lChannel, net_d, path);
		return result;
	}
	
	/**
	 * 云台控制<br/>
	 * 云台控制的方式为调用该方法摄像头便会一直执行该操作,直到该操作接收到"停止"指令及(iStop)参数
	 * 
	 * @param ip 摄像头ip
	 * @param iCommand 控制指令
	 * @param iSpeed 云台运行速度 
	 * @param iStop 是否为停止操作
	 * @return
	 */
	public static boolean cloudControl(String ip, int port, CloudCode iCommand, CloudCode iSpeed, CloudCode iStop) {
		//获取ip对应摄像头的句柄
		MyNativeLong nativeLong = TempData.getTempData().getNativeLong(ip, port);
		if (nativeLong == null) {
			log.error("无法获取句柄");
			return false;
		}
		
		//获取预览句柄
//		NativeLong lRealHandle = nativeLong.getlRealHandle();
//		if (lRealHandle.intValue() < 0) {
//			log.error("无法获取预览句柄");
//			return false;
//		}
		
		//判断是否为停止操作
		if (iSpeed.getKey() == 1) {
			return hCNetSDK.NET_DVR_PTZControl_Other(nativeLong.getlUserID(), nativeLong.getlChannel(), iCommand.getKey(), iStop.getKey());
		}
		boolean b = hCNetSDK.NET_DVR_PTZControlWithSpeed_Other(nativeLong.getlUserID(), nativeLong.getlChannel(), iCommand.getKey(), iStop.getKey(), iSpeed.getKey());
		if (!b) {
			log.error("云台操作失败：" + ip + ":" + port + "  " + iCommand.getKey() + "   handle:" + nativeLong.getlUserID());
		}
		return b;
	}
	
	/**
	 * 实时预览
	 * @param ip
	 * @param iCommand
	 * @param iSpeed
	 * @param iStop
	 * @return
	 */
	public static NativeLong realPlay(String ip, int port) {
		//获取ip对应摄像头的句柄
		MyNativeLong nativeLong = TempData.getTempData().getNativeLong(ip, port);
		if (nativeLong == null) {
			return null;
		}
		
		//获取预览句柄
		NativeLong lRealHandle = nativeLong.getlRealHandle();
		if (lRealHandle.intValue() < 0) {
			return null;
		}
		
		
		return lRealHandle;
	}
	
}
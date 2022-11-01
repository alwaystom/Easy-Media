package com.zj.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import cn.hutool.cache.CacheUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zj.ctrl.CameraCtrl;
import com.zj.ctrl.CloudCode;
import com.zj.ctrl.Control;
import com.zj.ctrl.LoginPlay;
import com.zj.ctrl.MyNativeLong;
import com.zj.ctrl.TempData;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 云台控制
 * @author ZJ
 *
 */
@RequestMapping("hk")
@RestController
@Slf4j
public class CloudCtrlController {
	
//	http://localhost:8888/hk/ctrl?ip=192.168.2.120&op=left&username=admin&password=VZCDOY

    public static final Map<String, CloudCode> stringToCommondMap = new HashMap<String, CloudCode>(){{
        put("up", CloudCode.TILT_UP);
        put("down", CloudCode.TILT_DOWN);
        put("left", CloudCode.PAN_LEFT);
        put("right", CloudCode.PAN_RIGHT);

        put("leftUp", CloudCode.UP_LEFT);
        put("leftDown", CloudCode.DOWN_LEFT);
        put("rightUp", CloudCode.UP_RIGHT);
        put("rightDown", CloudCode.DOWN_RIGHT);

        put("zoomIn", CloudCode.ZOOM_IN);
        put("zoomOut", CloudCode.ZOOM_OUT);
    }};

	/**
	 * 云台控制接口-停止
	 * @param session
	 * @param camera
	 */
	@RequestMapping("stopCtrl")
	public void stopCtrl(HttpSession session, CameraCtrl camera) {
        session.setAttribute("ip", camera.getIp());
        session.setAttribute("port", camera.getPort());
        CloudCode cloudCode = stringToCommondMap.get("up");
		Control.cloudControl(camera.getIp(), camera.getPort(), cloudCode, CloudCode.SPEED_LV6, CloudCode.END);
		log.info("云台控制停止:" + camera.getIp() + ":" + camera.getPort());
    }

    /**
     * 云台控制接口-开启
     * @param session
     * @param camera
     */
	@RequestMapping("ctrl")
	public void ctrl(HttpSession session, CameraCtrl camera) {
		checkLogin(camera);
		session.setAttribute("ip", camera.getIp());
		session.setAttribute("port", camera.getPort());
        CloudCode cloudCode = stringToCommondMap.get(camera.getOp());
        boolean b = false;
        if (cloudCode != null) {
            b = Control.cloudControl(camera.getIp(), camera.getPort(), cloudCode, CloudCode.SPEED_LV6, CloudCode.START);
        }
        log.info("云台控制:" + camera.getIp() + ":" + camera.getPort() + " 结果：" + b);
	}

	/**
	 * sdk登入
	 */
	private void checkLogin(CameraCtrl camera) {
		MyNativeLong nativeLong = TempData.getTempData().getNativeLong(camera.getIp(), camera.getPort());
		if(null == nativeLong) {
			LoginPlay lp = new LoginPlay();
			// 输入摄像机ip，端口，账户，密码登录
			try {
				boolean doLogin = lp.doLogin(camera.getIp(), Convert.toShort(camera.getPort(), (short) 8000), camera.getUsername(),
						camera.getPassword());
				log.info("摄像头登录：" + camera.getIp() + "，结果" + doLogin);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

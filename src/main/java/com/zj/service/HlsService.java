package com.zj.service;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.zj.common.CacheMap;
import com.zj.dto.CameraDto;
import com.zj.thread.MediaTransferHls;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.digest.MD5;

/**
 * 处理hls
 * @author ZJ
 *
 */
@Service
@Slf4j
public class HlsService {
	
	@Autowired
	private Environment env;
	
	/**
	 * 
	 */
	public static ConcurrentHashMap<String, MediaTransferHls> cameras = new ConcurrentHashMap<>(); 
	
	/**
	 * 定义ts缓存10秒
	 */
	public static CacheMap<String, byte[]> cacheTs = new CacheMap<>(10000);
	public static CacheMap<String, byte[]> cacheM3u8 = new CacheMap<>(10000);

    /**
     * 记录摄像头最后一次被访问ts切片的时间，对于长时间未被访问过的摄像头，自动关闭
     */
	private static final Map<String, Long> lastFetchTime = new HashMap(100);

    /**
     * 经过下面时间未被访问过的摄像头将自动关闭，单位毫秒
     */
    @Value("${mediaserver.autoClose.noClientsDuration}")
	public long AUTO_CLOSE_TIME;

	private static Thread checkCameraThread;

	public HlsService() {
	    startCheckCameraThread();
    }

	/**
	 * 保存ts
	 * @param camera
	 */
	public void processTs(String mediaKey, String tsName, InputStream in) {
		byte[] readBytes = IoUtil.readBytes(in);
		String tsKey = mediaKey.concat("-").concat(tsName);
		cacheTs.put(tsKey, readBytes);
	}

	/**
	 * 保存hls
	 * @param mediaKey
	 * @param in
	 */
	public void processHls(String mediaKey, InputStream in) {
		byte[] readBytes = IoUtil.readBytes(in);
		cacheM3u8.put(mediaKey, readBytes);
	}

	/**
	 * 关闭hls切片
	 * 
	 * @param cameraDto
	 */
	public void closeConvertToHls(CameraDto cameraDto) {
		// 区分不同媒体
		String mediaKey = MD5.create().digestHex(cameraDto.getUrl());
        closeConvertToHls(mediaKey);
	}

    /**
     * 关闭hls切片
     */
    private void closeConvertToHls(String mediaKey) {
        if (cameras.containsKey(mediaKey)) {
            MediaTransferHls mediaTransferHls = cameras.get(mediaKey);
            mediaTransferHls.stop();
            cameras.remove(mediaKey);
            cacheTs.remove(mediaKey);
            cacheM3u8.remove(mediaKey);
        }
    }

    /**
     * 获取分片，并记录最后一次操作时间
     */
	public byte[] getTs(String tsKey) {
        byte[] bytes = cacheTs.get(tsKey);
        if (bytes != null) {
            String mediaKey = tsKey.split("-")[0];
            lastFetchTime.put(mediaKey, System.currentTimeMillis());
        }
        return bytes;
    }

    private void startCheckCameraThread() {
	    checkCameraThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    checkCamera();
                    ThreadUtil.sleep(AUTO_CLOSE_TIME);
                }
            }
        });
	    checkCameraThread.setDaemon(true);
	    checkCameraThread.start();
    }

    /**
     * 检查超过一定时间未被访问过的摄像头，将其关闭
     */
    private void checkCamera() {
        long now = System.currentTimeMillis();
        Set<String> closeMediaKeys = new HashSet<>();
        for (Map.Entry<String, Long> entry : lastFetchTime.entrySet()) {
            if (now - entry.getValue() > AUTO_CLOSE_TIME) {
                String mediaKey = entry.getKey().split("-")[0];
                closeMediaKeys.add(mediaKey);
            }
        }

        for (String closeMediaKey : closeMediaKeys) {
            closeConvertToHls(closeMediaKey);
            lastFetchTime.remove(closeMediaKey);
        }
    }

	/**
	 * 开始hls切片
	 * 
	 * @param cameraDto
	 * @return
	 */
	public synchronized boolean startConvertToHls(CameraDto cameraDto) {
		// 区分不同媒体
		String mediaKey = MD5.create().digestHex(cameraDto.getUrl());
		cameraDto.setMediaKey(mediaKey);

		MediaTransferHls mediaTransferHls = cameras.get(mediaKey);

		if (null == mediaTransferHls) {
			mediaTransferHls = new MediaTransferHls(cameraDto, Convert.toInt(env.getProperty("server.port")));
			cameras.put(mediaKey, mediaTransferHls);
			mediaTransferHls.execute();
		}
		mediaTransferHls = cameras.get(mediaKey);
		
		// 15秒还没true认为启动不了
		for (int i = 0; i < 30; i++) {
			if (mediaTransferHls.isRunning()) {
			    lastFetchTime.put(cameraDto.getMediaKey(), System.currentTimeMillis());
				return true;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
		closeConvertToHls(cameraDto);
		log.error("开启切片失败：" + cameraDto.getUrl());
		return false;
	}

}

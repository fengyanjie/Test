//package com.voole.epg.f4k_download.domain;
//
//import java.io.File;
//import java.util.List;
//
//import android.text.TextUtils;
//
//public class SelectDisk {
//	/**
//	 * 文件的路径是否在存储设备找到
//	 * 存储设备找到下载db文件断点下载
//	 * 更改voolert.conf路径
//	 */
//	private void initConfPath() {
//
//		new Thread() {
//			public void run() {
//				// 判断存储设备
//				List<String> paths = getExitDisk();
//				if (paths == null) { // 存储设备不存在
//					return;
//				}
//				// 读取voolert.conf 文件存储路径
//				String loadPath = getDownLoadPath();
//				File path = new File(loadPath);
//				if (TextUtils.isEmpty(loadPath)
//						|| !path.getParentFile().exists() || !path.exists() || !new File(loadPath+"/"+VDBFactory.DATABASE_NAME).exists()) {// 路径不存在
//					for (String checkPath : paths) { // 循环存储设备分区
//														// 初始化一个存在voole.db的路径
//						LogUtils.d(checkPath);
//						File filePath = new File(checkPath + "/voole_video");
//						// 浏览当前路径下的所有文件和文件夹
//						File[] allFiles = filePath.listFiles();
//						if(allFiles == null) {
//							return;
//						}
//						// 循环所有路径
//						for (int i = 0; i < allFiles.length; i++) {
//							// 如果是文件
//							if (allFiles[i].isFile()) {
//								// 执行操作，例如输出文件名
//								System.out.println("--->"
//										+ allFiles[i].getName());
//								// 文件下 找到.db 数据库 文件
//								if (VDBFactory.DATABASE_NAME
//										.equalsIgnoreCase(allFiles[i].getName())) { // 默认给用户选择一个分区路径
//									initDownPath(checkPath); // 路径写入配置文件
//									break;
//								}
//							}
//						}
//
//					}
//
//				}
//
//			};
//
//		}.start();
//
//	}
//}

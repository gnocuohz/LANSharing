package team.wwg.lansharing.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateExcutorServiceUtil {
	private static ExecutorService exec; // 线程池,可重复利用的；
	private static int cpuCount;// 获取电脑cpu个数

	/**
	 * 创建线程池
	 */
	public static ExecutorService createExecutorService() {
		cpuCount = getCpuCount();
		if (cpuCount != 0) {
			exec = Executors.newFixedThreadPool(cpuCount * 8); // 线程池,有界线程池；
		} else {
			cpuCount = getCpuCount();
			exec = Executors.newFixedThreadPool(cpuCount * 8); // 线程池,有界线程池；
		}
		return exec;
	}

	/**
	 * 获取当前CPU个数
	 * 
	 * @return CPU个数
	 */
	private static int getCpuCount() {
//		Properties p = System.getProperties();// 获取当前的系统属性
		return Runtime.getRuntime().availableProcessors();
	}

}

/**
 *    Copyright  2014-2016 whatlookingfor@gmail.com(Jonathan)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.apache.ibatis.thread;

import com.whatlookingfor.sample.config.mybatis.MybatisRefreshProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedIOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过定时刷新实现mybatis的xml文件的刷新
 *
 * @author Jonathan
 * @version 2016/5/25 18:00
 * @since JDK 7.0+
 */
public class Runnable implements java.lang.Runnable {

	public static Logger logger = LoggerFactory.getLogger(Runnable.class);

	private String location;
	private Configuration configuration;


	private Long beforeTime = 0L; // 上一次刷新时间
	private static boolean refresh = false; // 是否执行刷新

	private static String mappingPath = "mappings"; // xml文件夹匹配字符串，需要根据需要修改
	private static int delaySeconds = 10;// 延迟刷新秒数
	private static int sleepSeconds = 1;// 休眠时间

	private static boolean enabled = false;


	public static boolean isRefresh() {
		return refresh;
	}

	public Runnable(String location, Configuration configuration,MybatisRefreshProperties mybatisRefresh) {
		this.location = location.replaceAll("\\\\", "/");
		this.configuration = configuration;
		delaySeconds = mybatisRefresh.getDelaySeconds() ==0 ? 50 : mybatisRefresh.getDelaySeconds();
		sleepSeconds = mybatisRefresh.getSleepSeconds()==0 ? 1 : mybatisRefresh.getSleepSeconds();
		mappingPath = StringUtils.isBlank(mybatisRefresh.getMappingPath()) ? "mappings"
				: mybatisRefresh.getMappingPath();
		enabled = mybatisRefresh.isEnabled();

		logger.debug("[delaySeconds] " + delaySeconds);
		logger.debug("[sleepSeconds] " + sleepSeconds);
		logger.debug("[mappingPath] " + mappingPath);
		logger.debug("[enabled] " + enabled);

	}

	@Override
	public void run() {
		location = location.substring("file [".length(),
				location.lastIndexOf(mappingPath) + mappingPath.length());
		beforeTime = System.currentTimeMillis();

		logger.debug("[location] " + location);
		logger.debug("[configuration] " + configuration);

		if (enabled) {
			start(this);
		}
	}

	public void start(final Runnable runnable) {

		new Thread(new java.lang.Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(delaySeconds * 1000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				refresh = true;

				System.out.println("========= Enabled refresh mybatis mapper =========");

				while (true) {
					try {
						runnable.refresh(location, beforeTime);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					try {
						Thread.sleep(sleepSeconds * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	/**
	 * 执行刷新
	 *
	 * @param filePath   刷新目录
	 * @param beforeTime 上次刷新时间
	 * @throws NestedIOException     解析异常
	 * @throws FileNotFoundException 文件未找到
	 */
	public void refresh(String filePath, Long beforeTime) throws Exception {

		// 本次刷新时间
		Long refrehTime = System.currentTimeMillis();

		List<File> refreshs = this.getRefreshFile(new File(filePath),
				beforeTime);
		if (refreshs.size() > 0) {
			logger.debug("refresh files:" + refreshs.size());
		}
		for (int i = 0; i < refreshs.size(); i++) {
			System.out.println("Refresh file: "
					+ mappingPath
					+ StringUtils.substringAfterLast(refreshs.get(i)
					.getAbsolutePath(), mappingPath));
			logger.debug("refresh file:" + refreshs.get(i).getAbsolutePath());
			logger.debug("refresh filename:" + refreshs.get(i).getName());
			SqlSessionFactoryBean.refresh(new FileInputStream(refreshs.get(i)),
					refreshs.get(i).getAbsolutePath(), configuration);
		}
		// 如果刷新了文件，则修改刷新时间，否则不修改
		if (refreshs.size() > 0) {
			this.beforeTime = refrehTime;
		}
	}


	/**
	 * 获取需要刷新的文件列表
	 *
	 * @param dir        目录
	 * @param beforeTime 上次刷新时间
	 * @return 刷新文件列表
	 */
	public List<File> getRefreshFile(File dir, Long beforeTime) {
		List<File> refreshs = new ArrayList<File>();

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				refreshs.addAll(this.getRefreshFile(file, beforeTime));
			} else if (file.isFile()) {
				if (this.check(file, beforeTime)) {
					refreshs.add(file);
				}
			} else {
				System.out.println("error file." + file.getName());
			}
		}

		return refreshs;
	}

	/**
	 * 判断文件是否需要刷新
	 *
	 * @param file       文件
	 * @param beforeTime 上次刷新时间
	 * @return 需要刷新返回true，否则返回false
	 */
	public boolean check(File file, Long beforeTime) {
		if (file.lastModified() > beforeTime) {
			return true;
		}
		return false;
	}

}

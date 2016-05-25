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

package com.whatlookingfor.sample.controller;

import com.whatlookingfor.sample.model.User;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Jonathan
 * @version 2016/5/17 19:17
 * @since JDK 7.0+
 */
@Controller
@RequestMapping(value = "/demo")
public class DemoController {
	private Logger logger = LoggerFactory.getLogger(DemoController.class);

	/**
	 * 可以直接使用@ResponseBody响应JSON
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getcount", method = RequestMethod.POST)
	@ApiOperation(value="测试-getCount", notes="getCount更多说明")
	public ModelMap getCount(HttpServletRequest request,
							 HttpServletResponse response) {
		logger.info(">>>>>>>> begin getCount >>>>>>>>");
		ModelMap map = new ModelMap();
		map.addAttribute("count", 158);

		// 后台获取的国际化信息
		map.addAttribute("xstest", "测试");
		return map;
	}

	/**
	 * 可以直接使用@ResponseBody响应JSON
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiIgnore//使用该注解忽略这个API
	@ResponseBody
	@RequestMapping(value = "/jsonTest1", method = RequestMethod.POST)
	public ModelMap jsonTest(HttpServletRequest request,
							 HttpServletResponse response) {
		ModelMap map = new ModelMap();
		map.addAttribute("hello", "你好");
		map.addAttribute("veryGood", "很好");

		return map;
	}

	/**
	 * 可以直接使用@ResponseBody响应JSON
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonTest3", method = RequestMethod.POST)
	public List<String> jsonTest3(HttpServletRequest request,
								  HttpServletResponse response) {
		List<String> list = new ArrayList<String>();
		list.add("hello");
		list.add("你好");
		return list;
	}

	/**
	 * JSON请求一个对象<br/>
	 * （Ajax Post Data：{"name":"名称","content":"内容"}）
	 *
	 * @param version
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonTest2", method = RequestMethod.POST)
	public ModelMap jsonTest2(@RequestBody User user) {
		ModelMap map = new ModelMap();
		map.addAttribute("result", "ok");
		return map;
	}

	/**
	 * 直接读取URL参数值<br/>
	 * /demo/jsonTest6.do?name=Hello&content=World
	 *
	 * @param demoName
	 * @param content
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonTest6", method = RequestMethod.POST)
	public ModelMap jsonTest6(@RequestParam("name") String demoName, @RequestParam String content) {
		logger.info("demoName：" + demoName);
		ModelMap map = new ModelMap();
		map.addAttribute("name",demoName + "AAA");
		map.addAttribute("content",content + "BBB");
		map.addAttribute("date",new java.util.Date());
		return map;
	}


	/**
	 * 输入 和输出为JSON格式的数据的方式 HttpEntity<?> ResponseEntity<?>
	 *
	 * @param u
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonTest4", method = RequestMethod.POST)
	public ResponseEntity<String> jsonTest4(HttpEntity<User> demo,
											HttpServletRequest request, HttpSession session) {
		//获取Headers方法
		HttpHeaders headers = demo.getHeaders();

		// 获取内容
		String demoContent = demo.getBody().getName();

		// 这里直接new一个对象（HttpHeaders headers = new HttpHeaders();）
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("MyHeaderName", "SHANHY");

		ResponseEntity<String> responseResult = new ResponseEntity<String>(
				demoContent, responseHeaders, HttpStatus.OK);
		return responseResult;
	}
}

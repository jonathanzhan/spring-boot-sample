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

import com.google.common.collect.Lists;
import com.whatlookingfor.sample.model.City;
import com.whatlookingfor.sample.model.User;
import io.swagger.annotations.*;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.http.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * swagger 的API demo
 *
 * @author Jonathan
 * @version 2016/5/26 11:27
 * @since JDK 7.0+
 */
@RestController
@Api(value = "swagger")
@RequestMapping("/swagger")
public class SwaggerController {


	/**
	 * 简单的demo
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "根据ID获取信息", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, notes = "根据id获取信息,返回id。仅做测试")
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_GATEWAY_TIMEOUT, message = "504错误"),
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "returns id", response = String.class),
			@ApiResponse(code = 404, message = "404")
	})
	public String getById(@ApiParam(name = "id", value = "参数id说明", required = true, example = "123", allowableValues = "10")
						  @PathVariable("id") String id) {
		return id;
	}

	@ApiOperation(value = "包含header的APIDOC")
	@RequestMapping(value = "/getCty/{id}" ,method = RequestMethod.GET)
	public City getCity(@RequestHeader(value = "client")String client, @RequestHeader(value = "pwd") String pwd,@PathVariable("id") int id,@RequestParam("name")String name){
		return new City(id,name);
	}

	/**
	 * 返回值类型的demo
	 * @param city
	 * @return
	 */
	@RequestMapping(value = "getByInfo", method = RequestMethod.GET)
	@ApiOperation(value = "根据city的信息查询", notes = "根据city的信息查询", response = City.class)
	public List<City> getByCity(@RequestParam(value = "city") @ApiParam(value = "city") City city) {
		List<City> list = Lists.newArrayList();
		list.add(city);
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
	public ModelMap jsonTest2(@RequestBody City city) {
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
		ModelMap map = new ModelMap();
		map.addAttribute("name",demoName + "AAA");
		map.addAttribute("content",content + "BBB");
		map.addAttribute("date",new java.util.Date());
		return map;
	}


	@ResponseBody
	@RequestMapping(value = "/jsonTest4", method = RequestMethod.POST)
	public ResponseEntity<String> jsonTest4(HttpEntity<City> demo,
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

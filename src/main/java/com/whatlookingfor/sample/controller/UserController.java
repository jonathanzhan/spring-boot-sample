/*
 * Copyright  2014-2016 whatlookingfor@gmail.com(Jonathan)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whatlookingfor.sample.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 *
 * @author Jonathan
 * @version 2016/6/1 15:49
 * @since JDK 7.0+
 */
@Controller
@RequestMapping(value = "user")
public class UserController {


	@RequiresUser
	@RequestMapping(value = "list")
	@ResponseBody
	public String list(){
		return "test";
	}


	@RequestMapping(value = "list1")
	@RequiresPermissions(value = "sys:test:add")
	@ResponseBody
	public String list1(){
		return "list1";
	}



	@RequestMapping(value = "list2")
	@RequiresPermissions(value = "sys:test:add1")
	@ResponseBody
	public String list2(){
		return "list2";
	}


	@RequestMapping(value = "list3")
	@ResponseBody
	public String list3(){
		return "list3";
	}
}

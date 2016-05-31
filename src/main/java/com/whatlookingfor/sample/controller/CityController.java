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

import com.whatlookingfor.sample.model.City;
import com.whatlookingfor.sample.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * city Controller
 *
 * @author Jonathan
 * @version 2016/5/26 17:59
 * @since JDK 7.0+
 */
@Controller
@RequestMapping("/city")
public class CityController {

	@Autowired
	private CityService cityService;

	@ApiIgnore
	@RequestMapping({"","list"})
	public String list(Model model) {

		List<City> list = cityService.findList();
		model.addAttribute("list", list);
		return "modules/city";
	}


	@RequestMapping("404")
	public String error1() throws Exception{
		throw new Exception("2qewqewq");
	}

}

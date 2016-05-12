/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/whatlookingfor">whatlookingfor</a> All rights reserved.
 */

package com.whatlookingfor.sample.chapter1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot的hello world
 *
 * @author Jonathan
 * @version 2016/5/12 17:12
 * @since JDK 7.0+
 */
@RestController
@EnableAutoConfiguration
public class HelloWorldController {

	@RequestMapping(value = "helloWorld")
	public String helloWorld(){
		return "hello world";
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldController.class,args);
	}
}

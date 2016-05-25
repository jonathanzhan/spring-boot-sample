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

package com.whatlookingfor.sample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Swagger ui配置
 *
 * @author Jonathan
 * @version 2016/5/17 18:39
 * @since JDK 7.0+
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements EnvironmentAware {
	private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);
	public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
	private RelaxedPropertyResolver propertyResolver;

	@Override
	public void setEnvironment(Environment environment) {
		this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
	}
//
//	@Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }


	@Bean
	public Docket testApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("test")
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(true)
				.pathMapping("/")// base，最终调用接口后会和paths拼接在一起
				.select()
				.paths(or(regex("/api/.*")))//过滤的接口
				.build()
				.apiInfo(apiInfo());
	}

	@Bean
	public Docket demoApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("demo")
				.genericModelSubstitutes(DeferredResult.class)
//              .genericModelSubstitutes(ResponseEntity.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(false)
				.pathMapping("/")
				.select()
				.paths(or(regex("/demo/.*")))//过滤的接口
				.build()
				.apiInfo(demoApiInfo());
	}

	private ApiInfo demoApiInfo() {
		Contact contact = new Contact("qe","sad","url");
		return new ApiInfo(
				propertyResolver.getProperty("title"),
				propertyResolver.getProperty("description"),
				propertyResolver.getProperty("version"),
				propertyResolver.getProperty("termsOfServiceUrl"),
				contact,
				propertyResolver.getProperty("license"),
				propertyResolver.getProperty("licenseUrl")
		);
	}


	private ApiInfo apiInfo() {
		Contact contact = new Contact("qe","sad","url");
		return new ApiInfo(
				propertyResolver.getProperty("title"),
				propertyResolver.getProperty("description"),
				propertyResolver.getProperty("version"),
				propertyResolver.getProperty("termsOfServiceUrl"),
				contact,
				propertyResolver.getProperty("license"),
				propertyResolver.getProperty("licenseUrl")
		);
	}
}

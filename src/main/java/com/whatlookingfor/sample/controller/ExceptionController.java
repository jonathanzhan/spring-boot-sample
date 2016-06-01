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

import com.whatlookingfor.sample.config.ExceptionMvcAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 重写BasicErrorController,主要负责系统的异常页面的处理以及错误信息的显示
 * @see org.springframework.boot.autoconfigure.web.BasicErrorController
 * @see org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration
 *
 * @author Jonathan
 * @version 2016/5/31 11:22
 * @since JDK 7.0+
 */
@Controller
@RequestMapping(value = "error")
@EnableConfigurationProperties({ServerProperties.class})
public class ExceptionController implements ErrorController {

	private ErrorAttributes errorAttributes;

	@Autowired
	private ServerProperties serverProperties;


	/**
	 * 初始化ExceptionController
	 * @param errorAttributes
	 */
	@Autowired
	public ExceptionController(ErrorAttributes errorAttributes) {
		Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
		this.errorAttributes = errorAttributes;
	}


	/**
	 * 定义404的ModelAndView
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(produces = "text/html",value = "404")
	public ModelAndView errorHtml404(HttpServletRequest request,
	                              HttpServletResponse response) {
		response.setStatus(getStatus(request).value());
		Map<String, Object> model = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.TEXT_HTML));
		return new ModelAndView("error/404", model);
	}

	/**
	 * 定义404的JSON数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "404")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error404(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.TEXT_HTML));
		HttpStatus status = getStatus(request);
		return new ResponseEntity<Map<String, Object>>(body, status);
	}

	/**
	 * 定义500的ModelAndView
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(produces = "text/html",value = "500")
	public ModelAndView errorHtml500(HttpServletRequest request,
	                              HttpServletResponse response) {
		response.setStatus(getStatus(request).value());
		Map<String, Object> model = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.TEXT_HTML));
		return new ModelAndView("error/500", model);
	}


	/**
	 * 定义500的错误JSON信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "500")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error500(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.TEXT_HTML));
		HttpStatus status = getStatus(request);
		return new ResponseEntity<Map<String, Object>>(body, status);
	}


	/**
	 * Determine if the stacktrace attribute should be included.
	 * @param request the source request
	 * @param produces the media type produced (or {@code MediaType.ALL})
	 * @return if the stacktrace attribute should be included
	 */
	protected boolean isIncludeStackTrace(HttpServletRequest request,
	                                      MediaType produces) {
		ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
		if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
			return true;
		}
		if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
			return getTraceParameter(request);
		}
		return false;
	}


	/**
	 * 获取错误的信息
	 * @param request
	 * @param includeStackTrace
	 * @return
	 */
	private Map<String, Object> getErrorAttributes(HttpServletRequest request,
	                                               boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		return this.errorAttributes.getErrorAttributes(requestAttributes,
				includeStackTrace);
	}

	/**
	 * 是否包含trace
	 * @param request
	 * @return
	 */
	private boolean getTraceParameter(HttpServletRequest request) {
		String parameter = request.getParameter("trace");
		if (parameter == null) {
			return false;
		}
		return !"false".equals(parameter.toLowerCase());
	}

	/**
	 * 获取错误编码
	 * @param request
	 * @return
	 */
	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		try {
			return HttpStatus.valueOf(statusCode);
		}
		catch (Exception ex) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	/**
	 * 实现错误路径,暂时无用
	 * @see ExceptionMvcAutoConfiguration#containerCustomizer()
	 * @return
	 */
	@Override
	public String getErrorPath() {
		return "";
	}

}

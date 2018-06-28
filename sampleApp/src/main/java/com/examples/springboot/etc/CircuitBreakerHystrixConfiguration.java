package com.examples.springboot.etc;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@Configuration
@EnableHystrix
public class CircuitBreakerHystrixConfiguration extends SpringBootServletInitializer{
	
	@Bean
	public HystrixMetricsStreamServlet hystrixMetricsStreamServlet(){
		return new HystrixMetricsStreamServlet();
	}
	
	@Bean 
	public ServletRegistrationBean servletRegistrationBean(HystrixMetricsStreamServlet hystrixMetricsStreamServlet){
		
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(hystrixMetricsStreamServlet,"/hystrix.stream");
		servletRegistrationBean.setName("hystrixServlet");
        servletRegistrationBean.setLoadOnStartup(1);

		return servletRegistrationBean;
	}

}

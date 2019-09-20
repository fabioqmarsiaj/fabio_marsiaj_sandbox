package com.fabioqmarsiaj.eureka_feign_poc.discovery;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface EurekaHttpMethods {
	
	@RequestLine("POST /{serviceName}")
	@Headers("Content-Type application/json")
	@Body("{jsonString}")
	public void registry(@Param("jsonString") String jsonString, @Param("serviceName") String serviceString);

}

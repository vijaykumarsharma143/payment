package com.payment.paymentclient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class ClientApplication {
	
	@Autowired
	private EurekaClient client;
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	@Autowired
	private ConfigAppClient configAppClient;

	@Value("${some.property:Default hello}")
	private String someotherproperty;
	
	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
	
	@RequestMapping("/hi")
	public String callmessage() {
		RestTemplate restTemplate = restTemplateBuilder.build();
		InstanceInfo instanceinfo = client.getNextServerFromEureka("service", false);
		String baseUrl = instanceinfo.getHomePageUrl();
		ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, null, String.class);
		return response.getBody();

	}
	@RequestMapping("/hello")
	public String prinntconfig() {
		StringBuilder s=new StringBuilder();
		s.append(configAppClient.getProperty());
		s.append("||");
		s.append(someotherproperty);
		return s.toString();

	}
		
		
		
}

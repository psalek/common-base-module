package edu.miu.common.service.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Generic service client. To be completed...</p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
@Component
public class BaseServiceClient {
	
	@Autowired
	private RestTemplate restTemplate;
	
	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
		return restTemplate.exchange(url, method, requestEntity, responseType);
	}

}

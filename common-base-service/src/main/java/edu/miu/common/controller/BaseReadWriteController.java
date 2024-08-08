package edu.miu.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.miu.common.service.BaseReadWriteService;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Provides basic create/update/delete functionality over a resource (type T with an ID of type I) 
 * and returns the response type of R. See {@link BaseReadWriteService} for more info. 
 * This class extends the functionality of {@link BaseReadController}</p>
 *
 * @author Payman Salek
 * 
 * @version 2.0.0
 * @since 1.0.0
 * 
 */
@Component
public abstract class BaseReadWriteController<R, T, I> extends BaseReadController<R, T, I> {
	
	@Autowired
	private BaseReadWriteService<R, T, I> baseService;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody R request) {
		return response(() -> baseService.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable I id, @RequestBody R request) {
		return response(() ->  baseService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable I id) {
		return response(() -> {
			baseService.delete(id);
			return null;
		});
	}
}

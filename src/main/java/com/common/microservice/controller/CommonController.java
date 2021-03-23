package com.common.microservice.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.common.microservice.service.CommonService;

@CrossOrigin("http://localhost:4200")
public class CommonController<E, S extends CommonService<E>> {
	
	@Autowired
	protected S service;
	
	@GetMapping
	public ResponseEntity<?> listUsers(){
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@GetMapping("/page")
	public ResponseEntity<?> list(Pageable pageable){
		return ResponseEntity.ok().body(service.findAll(pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserbyId(@PathVariable("id") Long id){
		
		Optional<E> StudOptional = service.findById(id);
		
		if(StudOptional.isEmpty()) {
			ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(StudOptional.get());
	}
	
	@PostMapping
	public ResponseEntity<?> addUser(@Valid @RequestBody E entity, BindingResult result){
		
		if(result.hasErrors()) {
			return this.validate(result);
		}
		E EntityDb = service.save(entity);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(EntityDb);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteE(@PathVariable("id") Long id){
		service.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
	
	protected ResponseEntity<?> validate(BindingResult result){
		Map<String, Object> errors = new HashMap<>();
		
		result.getFieldErrors().forEach(err ->{
			errors.put(err.getField()," the field "+ err.getField() + " " + err.getDefaultMessage());
		});
		
		return ResponseEntity.badRequest().body(errors);
	}
	
	
}

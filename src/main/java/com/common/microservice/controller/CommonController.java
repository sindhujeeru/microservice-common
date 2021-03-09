package com.common.microservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.common.microservice.service.CommonService;

public class CommonController<E, S extends CommonService<E>> {
	
	@Autowired
	protected S service;
	
	@GetMapping
	public ResponseEntity<?> listUsers(){
		return ResponseEntity.ok().body(service.findAll());
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
	public ResponseEntity<?> addUser(@RequestBody E entity){
		E EntityDb = service.save(entity);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(EntityDb);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteE(@PathVariable("id") Long id){
		service.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
	
	
}

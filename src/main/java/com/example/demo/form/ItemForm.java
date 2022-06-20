package com.example.demo.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ItemForm {
	
	@NotNull
	private Integer id;
	
}


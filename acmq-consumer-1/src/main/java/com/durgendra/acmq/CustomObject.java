package com.durgendra.acmq;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomObject implements Serializable{
	
	private Long id;
	private String name;
	private String code;
	private CustomDetail deatil;
	

}

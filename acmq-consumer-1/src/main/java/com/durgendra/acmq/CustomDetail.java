package com.durgendra.acmq;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomDetail {
	
	private String email;
	private String uid;
	private UUID uuid;
	private Date dob;

}

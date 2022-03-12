package com.example.wmp.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaskResponseDto {
	
	private String quotient;	//몫
	private String remainder;	//나머지
}

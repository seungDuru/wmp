package com.example.wmp.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaskRequestDto {
	
	private String url;		// URL
	private String type;	// TYPE
	private int unit;	// 출력단위묶음
	
}

package com.example.wmp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.wmp.dto.TaskRequestDto;
import com.example.wmp.dto.TaskResponseDto;
import com.example.wmp.service.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/task")
@Controller
public class TastController {
	
	private final TaskService taskService;
	
	@ExceptionHandler(Exception.class)
	public String exception(Exception e, Model model) {
		log.error(e.getClass() + e.getMessage());
		model.addAttribute("exception", e);
		log.error(model.toString());
		return "error";
	}
	
	@GetMapping("/form")
	public String taskView(Model model) throws Exception {
		model.addAttribute("requestDto", TaskRequestDto.builder().build());
		model.addAttribute("responseDto", TaskResponseDto.builder().build());
		return "task";
	}
	
	@PostMapping("/form")
	public String taskSubmit(@ModelAttribute TaskRequestDto requestDto, Model model) throws Exception {
		TaskResponseDto responseDto =  taskService.executionTask(requestDto);
		model.addAttribute("responseDto", responseDto);
		model.addAttribute("requestDto", requestDto);
		return "task";
	}
	
}

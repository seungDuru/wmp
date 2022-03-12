package com.example.wmp.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.wmp.dto.TaskRequestDto;
import com.example.wmp.dto.TaskResponseDto;

@Service
public class TaskService {

	/**
	 * executionTask
	 */
	public TaskResponseDto executionTask(TaskRequestDto requestDto) throws Exception {
		return requestProcessing(requestDto);
	}
	
	/**
	 * requestProcessing
	 * 요구사항 로직 처리
	 */
	private TaskResponseDto requestProcessing(TaskRequestDto requestDto) throws Exception {
		
		String strScrapPage = readWebPage(requestDto.getUrl());
		String strConvert = convertByType(strScrapPage, requestDto.getType());
		String strMixOrdered = orderedCharAndNumber(strConvert);
		TaskResponseDto taskResponseDto = splitString(strMixOrdered, requestDto.getUnit());
		
		return taskResponseDto;
	}
	
	/**
	 * readWebPage
	 * path 담긴 웹페이지 url html태그 읽기 
	 */
	private String readWebPage(String path) throws Exception {
		BufferedReader    br       = null;
		URL               url      = null;
		URLConnection     conn     = null;
		InputStreamReader isr      = null;
		String            contents = null;
		
		if(!ObjectUtils.isEmpty(path)) {
			
			url  = new URL(path);
			conn = (URLConnection)url.openConnection();
			isr  = new InputStreamReader(conn.getInputStream(), "utf-8");
			br   = new BufferedReader(isr);
			
			StringBuilder sb = new StringBuilder();
			while((contents = br.readLine()) != null){
				sb.append(contents + "\n");
			}
			br.close();
			
			return sb.toString();
			
		} else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * convertByType
	 * type 에 따른 html 태그 제거 및 숫자 영문 제외한 모든문자 제거
	 */
	private String convertByType(String str, String type) throws Exception {
		String returnStr = "";
		
		if("remove".equals(type)) {
			// html 태그 제거, 숫자 영문 제외한 모든문자 제거
			returnStr = str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "")
						   .replaceAll("<[^>]*>", "")
						   .replaceAll("[^0-9a-zA-Z]", "")
					       .replaceAll("\\s", "");
		} else {
			// 숫자 영문 제외한 모든문자 제거
			returnStr = str.replaceAll("[^0-9a-zA-Z]", "")
					       .replaceAll("\\s", "");
		}
		
		return returnStr;
	}
	
	/**
	 * orderedCharAndNumber
	 * 문자와 숫자를 오름차순 정렬 후 순서대로 교차
	 */
	private String orderedCharAndNumber(String str) throws Exception {
		char[] strArr = str.replaceAll("[^a-zA-Z]", "").toCharArray();	//문자 추출 배열
		char[] numArr = str.replaceAll("[^0-9]", "").toCharArray();		//숫자 추출 배열
		char[] orderedArr = new char[numArr.length + strArr.length];	//mix된 값 담을 배열
 		
		//정렬
 		Arrays.sort(numArr);
 		Arrays.sort(strArr);
		
 		//값 mix
 		int strCnt = 0, numCnt = 0;
 		for(int i = 0; i < orderedArr.length; i++) {
 			if(i % 2 == 0) {
 				if(strCnt < strArr.length) {
 					orderedArr[i] = strArr[strCnt];
 	 				strCnt++;
 				} else {
 					orderedArr[i] = numArr[numCnt];
 					numCnt++;
 				}
 			} else {
 				if(numCnt < numArr.length) {
 					orderedArr[i] = numArr[numCnt];
 					numCnt++;
 				} else {
 					orderedArr[i] = strArr[strCnt];
 					strCnt++;
 				}
 			}
 		}
 		return String.valueOf(orderedArr);
	}
	
	/**
	 * splitString
	 * 전달된 출력 단위 묶음에 해당하는 몫 과 나머지의 대한 값 리턴
	 */
	private TaskResponseDto splitString(String str, int unit) throws Exception {
		int mod = str.length() % unit;						//나머지 값
		int quotientEndIndex = str.length() - mod;			//몫 끝 인덱스
		int remainderStartIndex = quotientEndIndex + 1;		//나머지 시작 인덱스
		
		TaskResponseDto taskResponseDto = TaskResponseDto.builder()
				.quotient(str.substring(0, quotientEndIndex))
				.remainder(str.substring(remainderStartIndex, str.length()))
				.build();
		
		return taskResponseDto;
	}
	
}

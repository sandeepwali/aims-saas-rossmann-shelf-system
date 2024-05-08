package com.solumesl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Advise {
	@ExceptionHandler({NullPointerException.class})
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageDTO processNullPointerException(NullPointerException exception) {
		MessageDTO message = new MessageDTO();
		message.setMessage("Errors found in request, try again later or contact the Administrator.");
		message.setType("ERROR");
		return message;
	}
}
package com.solumesl.config;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
@Hidden
@RestController
@RequestMapping(path="/swagger-ui")
public class SwaggerConfig {
	@GetMapping(path="/swagger-ui.css", produces = "text/css")
	public String getCss() {
		String orig = "";
		String append = toText(getClass().getClassLoader().getResourceAsStream("swagger-ui.css"));
		return orig + append;
	}

	static String toText(InputStream in) {
		return new BufferedReader( new InputStreamReader(in, StandardCharsets.UTF_8))
				.lines().collect(Collectors.joining("\n"));
	}
}

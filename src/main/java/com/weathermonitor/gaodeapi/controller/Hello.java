package com.weathermonitor.gaodeapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/11/28 21:20
 */
@RestController
@RequestMapping("hello")
public class Hello {
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s! %s", name);
    }
}

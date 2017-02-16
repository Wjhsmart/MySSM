package com.jh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index() {
		return "index/index";
	}
	
	@RequestMapping(value="redirect_index", method=RequestMethod.GET)
	public String redirectIndex(Model model) {
		model.addAttribute("redirect", "redirect");
		return "index/index";
	}
}

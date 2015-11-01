package com.tapwisdom.admin.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srividyak on 08/07/15.
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("Home");
        modelAndView.addObject("name", "Vidya");
        return modelAndView;
    }
    
}

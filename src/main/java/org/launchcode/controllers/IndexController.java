package org.launchcode.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Simple redirect controller to send root to /cheese
 */
@Controller
public class IndexController {

    @RequestMapping("")
    public String index(){
        return "redirect:/cheese";
    }
}

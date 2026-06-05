package com.company.clothingerp.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CloudArchitectureController {

    @GetMapping("/cloud-architecture")
    public String showCloudArchitecture() {
        return "cloud-architecture";
    }
}

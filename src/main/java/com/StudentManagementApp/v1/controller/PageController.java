package com.StudentManagementApp.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PageController {

    /** Keep older bookmarks working while the canonical static filename stays lowercase. */
    @GetMapping("/Registrationpage.html")
    public RedirectView registrationPageCaseAlias() {
        return new RedirectView("/registrationpage.html", true);
    }
}

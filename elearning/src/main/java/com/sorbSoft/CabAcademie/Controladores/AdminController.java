package com.sorbSoft.CabAcademie.Controladores;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by anyderre on 11/08/17.
 */
@Controller
@RequestMapping("/admin")
@Secured("ROLE_ADMIN")
public class AdminController {

    @GetMapping("/registerCourse")
    public String registerCourse() {
        return "register_course";
    }

    @GetMapping("/category")
    public String category() {
        return "category";
    }

}

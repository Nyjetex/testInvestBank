package com.alexsanderyurevich.web.controller;

import com.alexsanderyurevich.converter.Converter;
import com.alexsanderyurevich.data.DeclineData;
import com.alexsanderyurevich.web.forms.NumberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public final class MainPageController {

    @GetMapping(value = {"/", "/index"})
    public String showPage(Model model) {
        model.addAttribute("form", new NumberForm());
        return "index";
    }

    @PostMapping(value = {"/", "/index"})
    public String convertNumber(Model model, @ModelAttribute("form") NumberForm numberForm) {
        try {
            DeclineData.Cases caseType = Enum.valueOf(DeclineData.Cases.class, numberForm.getCaseType());
            DeclineData.Gender gender = Enum.valueOf(DeclineData.Gender.class, numberForm.getGender());
            String result = Converter.convert(Long.valueOf(numberForm.getNumber()), caseType, gender);
            model.addAttribute("result", result);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("result", "Please, try again");
        }
        return "index";
    }
}

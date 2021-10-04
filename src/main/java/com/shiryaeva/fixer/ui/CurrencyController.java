package com.shiryaeva.fixer.ui;

import com.shiryaeva.fixer.logic.FixerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {

    @Autowired
    private FixerService fixerService;


    @RequestMapping("/")
    public String getLatestRates() {
        return fixerService.getLatestRates().toString();
    }

}

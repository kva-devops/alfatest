package com.example.alfabank.controller;

import com.example.alfabank.service.AnalysisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST-controller
 */
@RestController
@RequestMapping("/profit")
public class AnalysisController {

    /**
     * core business logic
     */
    @Autowired
    private AnalysisService analysisService;

    /**
     * GET method for displaying a GIF object depending on the dynamics of the exchange rate today and yesterday
     * @param currencyCode three-character alphabetic string, denoting the currency whose rate dynamics we want to get,
     *                     example: RUB, EUR, THB and others.
     *                     The base currency against which the comparison is made is USD
     * @return ResponseEntity object containing a string in html format with an GIF image
     */
    @RequestMapping(value = "/currencies/{code}/analyze", method = RequestMethod.GET)
    public ResponseEntity<String> showDifferencePerDay(@PathVariable("code") String currencyCode) throws Exception {
        return analysisService.showCorrespondingGif(currencyCode);
    }

}

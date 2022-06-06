package com.example.alfabank.service;

import com.example.alfabank.api.*;
import com.example.alfabank.models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

/**
 * Service contains main logic of application
 */
@Component
@Slf4j
public class AnalysisService {
    /**
     * instance of Feign-client interface for getting JsonNode object with currency rates of today
     */
    @Autowired
    private ExchangeRatesOfToday exchangeRatesOfToday;

    /**
     * instance of Feign-client interface for getting JsonNode object with currency rates of yesterday
     */
    @Autowired
    private ExchangeRatesOfYesterday exchangeRatesOfYesterday;

    /**
     * instance of Feign-client interface for getting JsonNode object all currencies
     */
    @Autowired
    private AllCurrencies allCurrencies;

    /**
     * instance of Feign-client interface for getting JsonNode-object containing random GIF image with tag: 'broke'
     */
    @Autowired
    private GiphyBrokeRandom giphyBroke;

    /**
     * instance of Feign-client interface for getting JsonNode-object containing random GIF image with tag: 'rich'
     */
    @Autowired
    private GiphyRichRandom giphyRich;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Мethod for obtaining an url of GIF-object depending on the dynamics of the exchange rate
     * @param currencyCode three-character alphabetic string, denoting the currency whose rate dynamics we want to get
     * @return ResponseEntity object containing a string in html format with an GIF image
     * @throws Exception
     */
    public ResponseEntity<String> showCorrespondingGif(String currencyCode) throws Exception {
        String anchor = UUID.randomUUID().toString();
        if (!validCurrencyCode(currencyCode)) {
            log.warn("AnalysisService.showCorrespondingGif() validCurrencyCode(currencyCode) returned false. "
                    + "Actual values: currencyCode='{}', anchor='{}'", currencyCode, anchor);
            throw new IllegalArgumentException("The format of the 'currencyCode' is incorrect. "
                    + "Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        JsonNode ratesOfToday = exchangeRatesOfToday.getRates();
        JsonNode ratesOfYesterday = exchangeRatesOfYesterday.getRates();
        if (ratesOfToday == null || ratesOfYesterday == null) {
            log.warn("AnalysisService.showCorrespondingGif() request to API 'https://openexchangerates.org/api/historical/:date.json' returned null. "
                    + "Actual values: anchor='{}'", anchor);
            throw new Exception("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        String urlGif = isRich(ratesOfToday, ratesOfYesterday, currencyCode) ?
                getGifUrl(giphyRich.getGiphyObject()) : getGifUrl(giphyBroke.getGiphyObject());
        String content = "<html><body><img src='" + urlGif + "'></body></html>";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    /**
     * Private method for validating parameters received in request: 'currency code'
     * @param code String value - currency code that is compared with the base currency
     * @return boolean value - TRUE if the check was successful, FALSE if the check was not successful
     * @throws Exception
     */
    private boolean validCurrencyCode(String code) throws Exception {
        String anchor = UUID.randomUUID().toString();
        if (code == null || code.length() != 3) {
            return false;
        }
        HashMap<String, String> allCurrenciesMap = allCurrencies.getAllCurrencies();
        if (allCurrenciesMap == null) {
            log.warn("AnalysisService.showCorrespondingGif() -> validCurrencyCode(currencyCode) request to API 'https://openexchangerates.org/api/currencies.json' returned null. "
                    + "Actual values: anchor='{}'", anchor);
            throw new Exception("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return allCurrenciesMap.containsKey(code.toUpperCase());
    }

    /**
     * Private method to check if the day was profitable
     * @param exchangeRatesOfToday - JsonNode value with exchange rates for today
     * @param exchangeRatesOfYesterday - JsonNode value with exchange rates for yesterday
     * @param currencyCode - String value - currency code that is compared with the base currency
     * @return boolean value
     * @throws Exception
     */
    private boolean isRich(JsonNode exchangeRatesOfToday, JsonNode exchangeRatesOfYesterday, String currencyCode) throws Exception {
        RatesResponseDTO todayRatesResponseDTO = mapper.treeToValue(exchangeRatesOfToday, RatesResponseDTO.class);
        HashMap<String, Float> todayRates = todayRatesResponseDTO.getRates();
        RatesResponseDTO yesterdayRatesResponseDTO = mapper.treeToValue(exchangeRatesOfYesterday, RatesResponseDTO.class);
        HashMap<String, Float> yesterdayRates = yesterdayRatesResponseDTO.getRates();
        return todayRates.get(currencyCode.toUpperCase()) - yesterdayRates.get(currencyCode.toUpperCase()) >= 0;
    }

    /**
     * Private method for getting a link to an GIF-image from JsonNode object
     * @param giphyObject - JsonNode object received when accessing the api website: https://giphy.com
     * @return String url leading to the GIF-image
     * @throws Exception
     */
    private String getGifUrl(JsonNode giphyObject) throws Exception {
        String anchor = UUID.randomUUID().toString();
        if (giphyObject == null) {
            log.warn("AnalysisService.showCorrespondingGif() -> getGifUrl(giphyObject). GiphyObject equals null, because request to API 'https://api.giphy.com/v1/gifs/random' returned null. "
                    + "Actual values: anchor='{}'", anchor);
            throw new Exception("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        FixedHeightGifDTO fixedHeightGifDTO = mapper.treeToValue(giphyObject.path("data").path("images").path("fixed_height"), FixedHeightGifDTO.class);
        GiphyResponseDTO giphyResponseDTO = mapper.treeToValue(giphyObject, GiphyResponseDTO.class);
        MetaInfoResponseDTO meta = giphyResponseDTO.getMeta();
        if (meta.getStatus() != 200) {
            log.warn("AnalysisService.showCorrespondingGif() -> getGifUrl(giphyObject). Result of request to API 'https://api.giphy.com/v1/gifs/random' returned status NOT OK. "
                    + "Actual values: status='{}', anchor='{}'", meta.getStatus(), anchor);
            throw new Exception("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return fixedHeightGifDTO.getUrl();
    }

}

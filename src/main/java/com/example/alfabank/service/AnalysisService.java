package com.example.alfabank.service;

import com.example.alfabank.models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

/**
 * Service contains main logic of application
 */
@Component
@Slf4j
public class AnalysisService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rates.id}")
    private String ratesApiKey;

    @Value("${rates.base}")
    private String ratesBaseCurrency;

    /**
     * URL for getting gif-picture with tag: "RICH"
     */
    @Value("${giphy.url.rich}")
    private String giphyUrlRich;

    /**
     * URL for getting gif-picture with tag: "BROKE"
     */
    @Value("${giphy.url.broke}")
    private String giphyUrlBroke;

    /**
     * URL for getting history of rates
     */
    @Value("${rates.url.historical}")
    private String ratesUrlHistorical;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Method for obtaining an url of GIF-object depending on the dynamics of the exchange rate
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
        String ratesTodayUrl = ratesUrlHistorical + LocalDate.now() + ".json?app_id=" + ratesApiKey + "&base=" + ratesBaseCurrency;
        String ratesYesterdayUrl = ratesUrlHistorical + LocalDate.now().minusDays(1L) + ".json?app_id=" + ratesApiKey + "&base=" + ratesBaseCurrency;
        System.out.println(ratesTodayUrl);
        System.out.println(ratesYesterdayUrl);
        JsonNode ratesOfToday = restTemplate.exchange(ratesTodayUrl, HttpMethod.GET, null, JsonNode.class).getBody();
        JsonNode ratesOfYesterday = restTemplate.exchange(ratesYesterdayUrl, HttpMethod.GET, null, JsonNode.class).getBody();
        if (ratesOfToday == null || ratesOfYesterday == null) {
            log.warn("AnalysisService.showCorrespondingGif() request to API 'https://openexchangerates.org/api/historical/:date.json' returned null. "
                    + "Actual values: anchor='{}'", anchor);
            throw new Exception("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        JsonNode giphyRich = restTemplate.exchange(giphyUrlRich, HttpMethod.GET, null, JsonNode.class).getBody();
        JsonNode giphyBroke = restTemplate.exchange(giphyUrlBroke, HttpMethod.GET, null, JsonNode.class).getBody();;
        String urlGif = isRich(ratesOfToday, ratesOfYesterday, currencyCode) ?
                getGifUrl(giphyRich) : getGifUrl(giphyBroke);
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
        JsonNode allCurrenciesJsonNode = restTemplate.exchange("https://openexchangerates.org/api/currencies.json", HttpMethod.GET, null, JsonNode.class).getBody();
        Currencies allCurrenciesMap = mapper.treeToValue(allCurrenciesJsonNode, Currencies.class);
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

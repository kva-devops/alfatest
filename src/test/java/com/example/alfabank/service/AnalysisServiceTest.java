package com.example.alfabank.service;

import com.example.alfabank.api.*;
import com.example.alfabank.models.MetaInfoResponseDTO;
import com.example.alfabank.models.RatesResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AnalysisServiceTest {
    @InjectMocks
    private final AnalysisService analysisService = new AnalysisService();
    @Mock
    private ExchangeRatesOfToday exchangeRatesOfToday;
    @Mock
    private ExchangeRatesOfYesterday exchangeRatesOfYesterday;
    @Mock
    private AllCurrencies allCurrencies;
    @Mock
    private GiphyBrokeRandom giphyBroke;
    @Mock
    private GiphyRichRandom giphyRich;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HashMap<String, String> currencies = new HashMap<String, String>() {{ put("RUB", "string"); }};
    private final HashMap<String, Object> giphyRichMap = new HashMap<String, Object>() {{
        put("data", new HashMap<String, Object>() {{
            put("images", new HashMap<String, Object>() {{
                put("fixed_height", new HashMap<String, String>() {{
                    put("url", "http://rich");
                    put("width", "string");
                    put("height", "string");
                    put("size", "string");
                    put("mp4", "string");
                    put("mp4_size", "string");
                    put("webp", "string");
                    put("webp_size", "string");
                }});
            }});
        }});
        put("meta", new MetaInfoResponseDTO("OK", 200, "string"));
    }};
    private final HashMap<String, Object> giphyBrokeMap = new HashMap<String, Object>() {{
        put("data", new HashMap<String, Object>() {{
            put("images", new HashMap<String, Object>() {{
                put("fixed_height", new HashMap<String, String>() {{
                    put("url", "http://broke");
                    put("width", "string");
                    put("height", "string");
                    put("size", "string");
                    put("mp4", "string");
                    put("mp4_size", "string");
                    put("webp", "string");
                    put("webp_size", "string");
                }});
            }});
        }});
        put("meta", new MetaInfoResponseDTO("OK", 200, "string"));
    }};
    private final HashMap<String, Float> ratesMapMore = new HashMap<String, Float>() {{ put("RUB", 10.1f); }};
    private final HashMap<String, Float> ratesMapLess = new HashMap<String, Float>() {{ put("RUB", 5.1f); }};
    private final JsonNode moreJsonNode = mapper.valueToTree(new RatesResponseDTO("string", "string", 0, "string", ratesMapMore));
    private final JsonNode lessJsonNode = mapper.valueToTree(new RatesResponseDTO("string", "string", 0, "string", ratesMapLess));
    private final JsonNode giphyRichJsonNode = mapper.valueToTree(giphyRichMap);
    private final JsonNode giphyBrokeJsonNode = mapper.valueToTree(giphyBrokeMap);

    @Test
    @DisplayName("'showCorrespondingGif' return response with 'broke' gif when today rates less then yesterday")
    void showCorrespondingGifBroke() throws Exception {
        String content = "<html><body><img src='http://broke'></body></html>";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        ResponseEntity<String> expected = new ResponseEntity<String>(content, headers, HttpStatus.OK);

        when(exchangeRatesOfToday.getRates()).thenReturn(lessJsonNode);
        when(exchangeRatesOfYesterday.getRates()).thenReturn(moreJsonNode);
        when(allCurrencies.getAllCurrencies()).thenReturn(currencies);
        when(giphyRich.getGiphyObject()).thenReturn(giphyRichJsonNode);
        when(giphyBroke.getGiphyObject()).thenReturn(giphyBrokeJsonNode);

        ResponseEntity<String> result = analysisService.showCorrespondingGif("RUB");
        assertEquals(expected.getStatusCode(), result.getStatusCode());
        assertEquals(expected.getBody(), result.getBody());
    }

    @Test
    @DisplayName("'showCorrespondingGif' return response with 'rich' gif when today rates more then yesterday")
    void showCorrespondingGifRich() throws Exception {
        String content = "<html><body><img src='http://rich'></body></html>";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        ResponseEntity<String> expected = new ResponseEntity<String>(content, headers, HttpStatus.OK);

        when(exchangeRatesOfToday.getRates()).thenReturn(moreJsonNode);
        when(exchangeRatesOfYesterday.getRates()).thenReturn(lessJsonNode);
        when(allCurrencies.getAllCurrencies()).thenReturn(currencies);
        when(giphyRich.getGiphyObject()).thenReturn(giphyRichJsonNode);
        when(giphyBroke.getGiphyObject()).thenReturn(giphyBrokeJsonNode);

        ResponseEntity<String> result = analysisService.showCorrespondingGif("RUB");
        assertEquals(expected.getStatusCode(), result.getStatusCode());
        assertEquals(expected.getBody(), result.getBody());
    }

    @Test
    @DisplayName("'showCorrespondingGif' throw IllegalArgumentException if 'currencyCode' not valid")
    void showCorrespondingGifValidError() throws Exception {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> analysisService.showCorrespondingGif("eee"));
        assertTrue(exception.getMessage().contains("The format of the 'currencyCode' is incorrect"));
    }

    @Test
    @DisplayName("'showCorrespondingGif' throw Exception if API 'https://openexchangerates.org/api/currencies.json' is unavailable")
    void showCorrespondingGifExceptionUnavailableApiOpenexchangerates() {
        when(allCurrencies.getAllCurrencies()).thenReturn(null);

        Throwable exception = assertThrows(Exception.class, () -> analysisService.showCorrespondingGif("RUB"));
        assertTrue(exception.getMessage().contains("An internal error has occurred"));
    }

    @Test
    @DisplayName("'showCorrespondingGif' throw Exception if API 'https://api.giphy.com/v1/gifs/random' is unavailable")
    void showCorrespondingGifExceptionUnavailableApiGiphy() {
        when(exchangeRatesOfToday.getRates()).thenReturn(moreJsonNode);
        when(exchangeRatesOfYesterday.getRates()).thenReturn(lessJsonNode);
        when(allCurrencies.getAllCurrencies()).thenReturn(currencies);
        when(giphyRich.getGiphyObject()).thenReturn(null);
        when(giphyBroke.getGiphyObject()).thenReturn(null);

        Throwable exception = assertThrows(Exception.class, () -> analysisService.showCorrespondingGif("RUB"));

        assertTrue(exception.getMessage().contains("An internal error has occurred"));
    }

    @AfterEach
    public void close() {
    }
}
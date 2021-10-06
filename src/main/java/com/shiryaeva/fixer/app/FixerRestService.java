package com.shiryaeva.fixer.app;

import com.shiryaeva.fixer.entity.CurrencyDTO;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class FixerRestService implements InitializingBean {

    private static final long DELAY = 14400000; // 4 h
    private static final String FIXER_URL = "http://data.fixer.io/api/latest";
    private final String apiKey;
    @Autowired
    private Logger logger;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CurrencyDataService currencyDataService;

    @Autowired
    public FixerRestService(@Value("${API_KEY}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Scheduled(fixedDelay = DELAY)
    public void getLatestRates() {
        logger.info("=======================");
        logger.info("Getting latest rates...");
        logger.info("=======================");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(FIXER_URL)
                .queryParam("access_key", apiKey);
        CurrencyDTO currencyDTO = restTemplate.getForObject(builder.toUriString(), CurrencyDTO.class);
        if (currencyDTO != null) {
            currencyDataService.convertFixerResponseToData(currencyDTO);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        getLatestRates();
    }
}



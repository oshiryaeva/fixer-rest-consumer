package com.shiryaeva.fixer.logic;

import com.shiryaeva.fixer.response.FixerResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FixerService implements InitializingBean {

    private static final long DELAY = 3600000; // 1 hour

    private final String FIXER_URL = "http://data.fixer.io/api/latest";
    private final String API_KEY = "45af2a20c452c4c0a40905396a7486f4";

    @Autowired
    private Logger logger;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DomainService domainService;

    @Scheduled(fixedDelay = DELAY)
    public FixerResponse getLatestRates() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(FIXER_URL)
                .queryParam("access_key", API_KEY);
        logger.info("URL: " + builder.toUriString());
        FixerResponse fixerResponse = restTemplate.getForObject(builder.toUriString(), FixerResponse.class);
        logger.info("Response: " + fixerResponse.toString());
        domainService.convertFixerResponseToData(fixerResponse);
        return fixerResponse;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        getLatestRates();
    }
}



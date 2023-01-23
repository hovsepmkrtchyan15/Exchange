package com.example.exchange.faign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "directory", url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml")
public interface CEBClient {
    @GetMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    String getSbpBankIcons();
}
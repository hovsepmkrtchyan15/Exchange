package com.example.exchange.parser;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Getter
@XmlRootElement(namespace = "http://www.gesmes.org/xml/2002-08-01", name = "Cube")
public class CubeRate {

    @XmlAttribute(name = "currency")
    private String currency;

    @XmlAttribute(name = "rate")
    private BigDecimal rate;

}


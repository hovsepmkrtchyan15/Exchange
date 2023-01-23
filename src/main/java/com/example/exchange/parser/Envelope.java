package com.example.exchange.parser;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@XmlRootElement(namespace = "http://www.gesmes.org/xml/2002-08-01", name = "Envelope")
public final class Envelope {

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private Cube cube;

}


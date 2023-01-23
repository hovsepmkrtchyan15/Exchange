package com.example.exchange.parser;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@XmlRootElement(namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref", name = "Envelope")
public class Cube {

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private CubeTime cubeTime;


}

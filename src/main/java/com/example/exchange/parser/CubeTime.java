package com.example.exchange.parser;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@XmlRootElement(namespace = "http://www.gesmes.org/xml/2002-08-01", name = "Cube")
public class CubeTime {

    @XmlAttribute(name = "time")
    private String date;

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private List<CubeRate> cubes;


}


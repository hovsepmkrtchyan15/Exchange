package com.example.exchange.service;

import com.example.exchange.entity.ExchangeRates;
import com.example.exchange.repository.ExchangeRatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    @Value("${exchange.currency.url}")
    private String spec;
    private final ExchangeRatesRepository exchangeRatesRepository;

    private static final String CUBE_NODE = "//Cube/Cube/Cube";
    private static final String CUBE_NODE_TIME = "//Cube/Cube";
    private static final String CURRENCY = "currency";
    private static final String RATE = "rate";
    private static final String DATE = "time";


    public List<ExchangeRates> findAllByDate(LocalDate date) {
        Optional<List<ExchangeRates>> allByDate = exchangeRatesRepository.findAllByDate(date);
        if (allByDate.isPresent() && allByDate.get().size() > 0) {
            return allByDate.get();
        }
        exchangeRatesRepository.deleteAll();
        saveRates();
        return exchangeRatesRepository.findAll();
    }

    public void saveRates() {
        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;

        try {
            URL url = new URL(spec);
            InputStream is = url.openStream();
            document = builder.parse(is);

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            XPathExpression expr = xpath.compile(CUBE_NODE);
            XPathExpression time = xpath.compile(CUBE_NODE_TIME);

            NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            Node nTime = (Node) time.evaluate(document, XPathConstants.NODE);

            NamedNodeMap attributes = nTime.getAttributes();
            LocalDate date = LocalDate.parse(attributes.getNamedItem(DATE).getNodeValue());

            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                NamedNodeMap attribs = node.getAttributes();
                if (attribs.getLength() > 0) {
                    Node currencyAttrib = attribs.getNamedItem(CURRENCY);
                    if (currencyAttrib != null) {
                        String currency = currencyAttrib.getNodeValue();
                        String rateTStr = attribs.getNamedItem(RATE).getNodeValue();
                        ExchangeRates exchangeRates = ExchangeRates.builder()
                                .currency(currency)
                                .rates(Double.valueOf(rateTStr))
                                .date(date)
                                .build();
                        exchangeRatesRepository.save(exchangeRates);
                    }
                }
            }
        } catch (SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }

    }

    public Double change(int idFrom, int idTo) {
        Optional<ExchangeRates> byId = exchangeRatesRepository.findById(idFrom);
        Optional<ExchangeRates> byId1 = exchangeRatesRepository.findById(idTo);
        return byId1.get().getRates() / byId.get().getRates();
    }

    public String findById(int id) {
        Optional<ExchangeRates> byId = exchangeRatesRepository.findById(id);
        return byId.get().getCurrency();
    }
}

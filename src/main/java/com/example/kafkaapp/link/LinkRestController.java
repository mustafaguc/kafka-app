package com.example.kafkaapp.link;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Controller class for handling RESTful endpoints related to link extraction and production.
 * Utilizes a LinkExtractor for extracting links and a LinkProducer for processing and producing links.
 */
@RestController
public class LinkRestController {

    // Instance variable to hold a LinkExtractor instance
    private final LinkExtractor linkExtractor = new LinkExtractor();

    // Instance variable to hold a LinkProducer instance
    private final LinkProducer linkProducer;

    /**
     * Constructor for the LinkRestController class.
     *
     * @param linkProducer The LinkProducer dependency used for producing links.
     */
    public LinkRestController(LinkProducer linkProducer) {
        this.linkProducer = linkProducer;
    }

    /**
     * Endpoint for fetching links based on the provided URL.
     *
     * @param url The URL from which to extract links.
     * @return A Stream of Link objects extracted from the provided URL.
     */
    @GetMapping("/fetch")
    public Stream<Link> extractLinksOf(@RequestParam String url) {
        // Log the current thread name for debugging or monitoring purposes
        System.out.println("Extracting links of " + Thread.currentThread().getName());

        // Return a stream of links extracted using the LinkExtractor
        return linkExtractor.extractLinksOf(url);
    }

    /**
     * Endpoint for producing links based on the provided URL.
     *
     * @param url The URL from which to extract and produce links.
     * @return A Map.Entry containing the produced link count.
     */
    @GetMapping("/produce")
    public Map.Entry<String, Integer> produceLinksOf(@RequestParam String url) {
        // Extract links from the provided URL and convert them to a list
        List<Link> links = linkExtractor.extractLinksOf(url).toList();

        // Send the extracted links to the LinkProducer for further processing
        linkProducer.sendLinkMessages(links);

        // Return a Map.Entry containing the produced link count
        return new AbstractMap.SimpleEntry<>("Produced Link Count", links.size());
    }
}


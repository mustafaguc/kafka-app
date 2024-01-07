package com.example.kafkaapp.link;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class LinkRestController {

    private final LinkExtractor linkExtractor = new LinkExtractor();
    private final LinkProducer linkProducer;

    public LinkRestController(LinkProducer linkProducer) {
        this.linkProducer = linkProducer;
    }

    @GetMapping("/fetch")
    public Stream<Link> extractLinksOf(@RequestParam String url) {
        return linkExtractor.extractLinksOf(url);
    }

    @GetMapping("/produce")
    public Map.Entry<String, Integer> produceLinksOf(@RequestParam String url) {
        List<Link> links = linkExtractor.extractLinksOf(url).toList();
        linkProducer.sendLinkMessages(links);
        return new AbstractMap.SimpleEntry<>("Produced Link Count", links.size());
    }
}

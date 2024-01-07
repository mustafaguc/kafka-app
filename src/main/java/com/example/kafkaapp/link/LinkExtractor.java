package com.example.kafkaapp.link;

import org.jsoup.Jsoup;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.stream.Stream;

public class LinkExtractor {

    public String downloadPage(String url) {
        return RestClient.create()
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    private Stream<Link> extractLinks(String htmlContent) {
        return Jsoup.parse(Optional.ofNullable(htmlContent).orElse("<body></body>")).body()
                .selectXpath("//a[string-length(@href) > 0]")
                .stream()
                .map(element -> new Link(element.text(), element.attr("href")));
    }

    public Stream<Link> extractLinksOf(String url) {
        return extractLinks(downloadPage(url)).map(link -> link.concatenateWith(url));
    }

}

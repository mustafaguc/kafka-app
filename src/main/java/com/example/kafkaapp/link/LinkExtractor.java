package com.example.kafkaapp.link;

import org.jsoup.Jsoup;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The LinkExtractor class is responsible for extracting links from a web page.
 */
public class LinkExtractor {

    /**
     * Downloads the content of a web page given its URL.
     *
     * @param url the URL of the web page
     * @return the downloaded page content as a string
     */
    public String downloadPage(String url) {
        return RestClient.create()
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    /**
     * Extracts links from the HTML content of a web page.
     *
     * @param htmlContent the HTML content of the web page
     * @return a stream of Link objects representing the extracted links
     */
    private Stream<Link> extractLinks(String htmlContent) {
        return Jsoup.parse(Optional.ofNullable(htmlContent).orElse("<body></body>")).body()
                .selectXpath("//a[string-length(@href) > 0]")
                .stream()
                .map(element -> new Link(element.text(), element.attr("href")));
    }

    /**
     * Extracts links from a web page given its URL.
     *
     * @param url the URL of the web page
     * @return a stream of Link objects representing the extracted links
     */
    public Stream<Link> extractLinksOf(String url) {
        return extractLinks(downloadPage(url)).map(link -> link.concatenateWith(url));
    }

}

package com.example.kafkaapp.link;

import java.net.URI;

/**
 * Represents a link with text and href.
 */
public record Link(String text, String href) {

    /**
     * Concatenates the current link's href with the provided URL.
     *
     * @param url the URL to concatenate with the href
     * @return a new Link object with the concatenated href, or the current Link object if the href starts with "http"
     */
    public Link concatenateWith(String url) {
        if (href.startsWith("http")) {
            return this;
        }

        URI uri = URI.create(url);
        return new Link(text(), uri.getScheme() + "://" + uri.getHost() + (!href.startsWith("/") ? "/" : "") + href);
    }
}

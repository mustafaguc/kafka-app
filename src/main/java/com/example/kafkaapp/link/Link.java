package com.example.kafkaapp.link;

public record Link(String text, String href) {

    public Link concatenateWith(String url) {
        return !href().startsWith("http") ? new Link(text(), url.concat(href())) : this;
    }
}

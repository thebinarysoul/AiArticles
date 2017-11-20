package com.bot;

public class Article {
    private final String description;
    private final String link;
    private final int id;
    private static int count = 1;

    public Article(final String description, final String link) {
        this.description = description;
        this.link = link;
        this.id = ++count;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Article{");
        sb.append("description='").append(description).append('\'');
        sb.append(", link='").append(link).append('\'');
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}

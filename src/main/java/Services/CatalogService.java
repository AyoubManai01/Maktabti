package Services;

import Entites.Catalog;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogService {
    private Catalog catalog;

    public CatalogService(Catalog catalog) {
        this.catalog = catalog;
    }

    public List<String> searchByTitle(String title) {
        return (List<String>) catalog.getBookTitles().entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(title))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());
    }

    public List<String> searchByAuthors(String author) {
        return (List<String>) catalog.getBookAuthors().entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(author))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());
    }

    public List<String> searchBySubject(String subject) {
        return (List<String>) catalog.getBookSubjects().entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(subject))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());
    }

    public List<String> searchByPubDate(Date date) {
        return (List<String>) catalog.getBookPublicationDates().entrySet().stream()
                .filter(entry -> entry.getKey().equals(date))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());
    }
}
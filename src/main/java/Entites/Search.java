package Entites;

import java.util.Map;
import java.util.List;
import java.util.Date;

public interface Search {
    public Map<String, List> searchByTitle(String title);
    public Map<String, List> searchByAuthors(String title);
    public Map<String, List> searchBySubject(String title);
    public Map<Date, List> searchByPubDate(Date datetime);
}

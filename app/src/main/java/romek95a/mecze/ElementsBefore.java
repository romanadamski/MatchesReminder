package romek95a.mecze;

import org.jsoup.select.Elements;

public class ElementsBefore {
    Elements firstMatch;
    Elements nextMatches;
    ElementsBefore (Elements firstMatch, Elements nextMatches){
        this.firstMatch = firstMatch;
        this.nextMatches = nextMatches;
    }
}

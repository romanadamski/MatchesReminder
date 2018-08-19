package romek95a.mecze;

import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseHtml {
    ParseHtml(String teamName){
        this.teamName = teamName;
    }
    String teamName;
    public void parse(){
        AsyncParse asyncParse = new AsyncParse();
        asyncParse.execute();
    }
    class AsyncParse extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            try {
                String link = "http://www.google.com/search?q=" + teamName;
                String classMatch = "div.abhAW";
                List<String> jslki = new ArrayList<>();
                Document doc = Jsoup.connect(link).get();
                Log.d("Link:", link);
                Elements elements = doc.select(classMatch);
                MatchesHandler.nextTeams.clear();
                for(Element elem: elements) {
                    //System.out.println(elem);
                    MatchesHandler.nextTeams.add(elem.text());
                }
                System.out.println("sizeeee " + MatchesHandler.nextTeams.size());
                System.out.println("jslki" + MatchesHandler.nextTeams);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

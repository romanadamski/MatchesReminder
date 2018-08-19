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
import java.util.concurrent.ExecutionException;

public class ParseHtml {
    ParseHtml(String teamName){
        this.teamName = teamName;
    }
    String teamName;
    public List<String> parse(){
        AsyncParse asyncParse = new AsyncParse();
        try {
            return asyncParse.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    class AsyncParse extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            try {
                String link = "http://www.google.com/search?q=" + teamName;
                String classFirstMatch = "div.abhAW";
                String classnextMatches = "div.bkWMgd";
                List<String> nextTeams = new ArrayList<>();
                Document doc = Jsoup.connect(link).get();
                Log.d("Link:", link);
                Elements firstMatch = doc.select(classFirstMatch);
                Elements nextMatches = doc.select(classnextMatches);
                MatchesHandler.nextTeams.clear();
                for(Element elem: firstMatch) {
                    nextTeams.add(elem.text());
                }
                for(Element elem: nextMatches) {
                    nextTeams.add(elem.text());
                }
                System.out.println("sizeeee " + nextTeams.size());
                System.out.println("nextTeams" + nextTeams.get(1));
                return nextTeams;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

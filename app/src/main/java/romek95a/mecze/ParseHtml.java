package romek95a.mecze;

import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ParseHtml {
    ParseHtml(String teamName){
        this.teamName = teamName;
    }
    String classFirstLeagueTimeEnd= "div.imso_mh__stts-l";
    String classFirstTeamsScore = "div.imso_mh__tm-a-sts";

    String classFirstLeagueTime1 = "div.imso-hide-overflow";
    String classFirstLeagueTime2 = "span.imso-hide-overflow";
    String classFirstDuring = "span.vVvqnf";
    String classFirstTeamHomeName = "div.imso_mh__first-tn-ed";
    String classFirstTeamAwayName = "div.imso_mh__second-tn-ed";
    String classFirstTeamHomeScore = "div.imso_mh__l-tm-sc";
    String classFirstTeamAwayScore = "div.imso_mh__r-tm-sc";


    String teamName;
    public ElementsBefore parse(){
        AsyncParse asyncParse = new AsyncParse();
        try {
            ElementsBefore elementsBefore = asyncParse.execute().get();
            return elementsBefore;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    class AsyncParse extends AsyncTask<String, String, ElementsBefore> {
        @Override
        protected ElementsBefore doInBackground(String... strings) {
            try {
                String link = "http://www.google.com/search?q=" + teamName;
                String classFirstMatch = "div.abhAW";
                String classNextMatches = "div.bkWMgd";
                Document doc = Jsoup.connect(link).get();
                Log.d("Link:", link);
                Elements firstMatch = doc.select(classFirstMatch);
                Elements nextMatches = doc.select(classNextMatches);
                return new ElementsBefore(firstMatch, nextMatches);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public Map<String, String> firstMatchFinalInfo(Elements firstMatch){
        Map<String, String> firstMatchesMap = new HashMap<>();

        String firstMatchLeagueTime1 = firstMatch.select(classFirstLeagueTimeEnd).select(classFirstLeagueTime1).text();
        String firstMatchLeagueTime2 = firstMatch.select(classFirstLeagueTimeEnd).select(classFirstLeagueTime2).text();
        String firstMatchDuring = firstMatch.select(classFirstLeagueTimeEnd).select(classFirstDuring).text();
        String firstMatchTeamHomeName = firstMatch.select(classFirstTeamsScore).select(classFirstTeamHomeName).text();
        firstMatchTeamHomeName = firstMatchTeamHomeName.replace("-", "");
        String firstMatchTeamAwayName = firstMatch.select(classFirstTeamsScore).select(classFirstTeamAwayName).text();
        firstMatchTeamAwayName = firstMatchTeamAwayName.replace("-", "");
        String firstMatchTeamHomeScore = firstMatch.select(classFirstTeamsScore).select(classFirstTeamHomeScore).text();
        String firstMatchTeamAwayScore = firstMatch.select(classFirstTeamsScore).select(classFirstTeamAwayScore).text();

        if(!firstMatchLeagueTime1.equals(""))
            firstMatchesMap.put("leagueTime", firstMatchLeagueTime1);
        if(!firstMatchLeagueTime2.equals(""))
            firstMatchesMap.put("leagueTime", firstMatchLeagueTime2);
        firstMatchesMap.put("teamHomeName", firstMatchTeamHomeName);
        firstMatchesMap.put("teamAwayName", firstMatchTeamAwayName);
        firstMatchesMap.put("teamHomeScore", firstMatchTeamHomeScore);
        firstMatchesMap.put("teamAwayScore", firstMatchTeamAwayScore);
        firstMatchesMap.put("during", firstMatchDuring);

        return firstMatchesMap;
    }
}

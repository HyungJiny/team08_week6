package cnu.lecture;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Querier implements InSummonersGameInfo{

	protected final GameParticipantListener listener;
	protected final String apiKey;

	public Querier(String apiKey, GameParticipantListener listener) {
		this.apiKey=apiKey;
		this.listener=listener;
	}

	public InGameInfo getSummonersGameInfo(String summonerName)
			throws UnsupportedEncodingException, IOException, ClientProtocolException {
		
				HttpClient client = HttpClientBuilder.create().build();
			    String summonerId = getGameId(summonerName, client);
			    InGameInfo gameInfo = getGameInfo(client, summonerId);
				return gameInfo;
			}

	protected String getGameId(String summonerName, HttpClient client)
			throws UnsupportedEncodingException, IOException, ClientProtocolException {
		HttpUriRequest summonerRequest = buildApiHttpRequest(summonerName);
		HttpResponse summonerResponse = client.execute(summonerRequest);
		
		Gson summonerInfoGson = new Gson();
		Type mapType = new TypeToken<HashMap<String, SummonerInfo>>(){}.getType();
		HashMap<String, SummonerInfo> entries = summonerInfoGson.fromJson(jsonReader(summonerResponse), mapType);
		String summonerId = entries.get(summonerName).getId();
		return summonerId;
	}

	protected InGameInfo getGameInfo(HttpClient client, String summonerId) throws IOException, ClientProtocolException {
		HttpUriRequest inGameRequest = buildObserverHttpRequest(summonerId);
		HttpResponse inGameResponse = client.execute(inGameRequest);
		Gson inGameGson = new Gson();
		InGameInfo gameInfo = inGameGson.fromJson(jsonReader(inGameResponse), InGameInfo.class);
		return gameInfo;
	}
	protected JsonReader jsonReader(HttpResponse summonerResponse) throws IOException {
		return new JsonReader(new InputStreamReader(summonerResponse.getEntity().getContent()));
	}


	private HttpUriRequest buildApiHttpRequest(String summonerName) throws UnsupportedEncodingException {
	    String url = mergeWithApiKey(new StringBuilder()
	            .append("https://kr.api.pvp.net/api/lol/kr/v1.4/summoner/by-name/")
	            .append(URLEncoder.encode(summonerName, "UTF-8")))
	            .toString();
	    return new HttpGet(url);
	}

	private HttpUriRequest buildObserverHttpRequest(String id) {
	    String url = mergeWithApiKey(new StringBuilder()
	            .append("https://kr.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/KR/")
	            .append(id))
	            .toString();
	    return new HttpGet(url);
	}

	private StringBuilder mergeWithApiKey(StringBuilder builder) {
	    return builder.append("?api_key=").append(apiKey);
	}

}

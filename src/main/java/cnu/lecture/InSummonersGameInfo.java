package cnu.lecture;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;

public interface InSummonersGameInfo {
    public InGameInfo getSummonersGameInfo(String summonerName) 
            throws UnsupportedEncodingException, IOException, ClientProtocolException;
}

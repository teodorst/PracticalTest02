package ro.pub.cs.systems.eim.practicaltest02.network;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

import static android.content.ContentValues.TAG;

public class ServerCommunicationThread extends Thread {

    private Socket socket;

    public ServerCommunicationThread(Socket socket) {
        this.socket = socket;
        Log.d(Constants.TAG, "[SERVER] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
    }

    public String httpGetRequest(String queryParam) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String url = Constants.API_SERVER + queryParam;
        HttpGet httpGet = new HttpGet(url);
        ResponseHandler<String> requestHandler = new BasicResponseHandler();
        String responseString = httpClient.execute(httpGet, requestHandler);
        Log.d(Constants.TAG, responseString);
        return responseString;
    }


    public void run() {
        try {
            if (socket == null) {
                return;
            }
            BufferedReader requestReader = Utilities.getReader(socket);
            PrintWriter responsePrintWriter = Utilities.getWriter(socket);

            String line = requestReader.readLine();
            if (line != null && !line.isEmpty()) {
                String responseToClient = null;

                String suggestionsJsonString = httpGetRequest(line);

                // Parse json
                JSONArray resultJson = new JSONObject(suggestionsJsonString).getJSONArray("RESULTS");
                int i;
                List<String> suggestions = new ArrayList<String>();
                for (i = 0; i < resultJson.length(); i ++) {
                    suggestions.add(resultJson.getJSONObject(i).getString("name"));
                }

                Log.d(TAG, "run: Server Seding" + suggestions.toString());
                responsePrintWriter.println(suggestions.toString());
                socket.close();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


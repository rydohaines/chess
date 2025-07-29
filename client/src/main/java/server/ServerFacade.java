package server;

import com.google.gson.Gson;
import responses.*;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ServerFacade(int port) {
        serverUrl = "http://localhost:"+port;
    }
    public <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String header) throws Exception{
        URL url = (new URI(serverUrl + path)).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);
        http.setDoOutput(true);
        writeBody(request, http,header);
        http.connect();
        throwIfNotSuccessful(http);
        return readBody(http, responseClass);
    }

    public RegisterResponse register(RegisterRequest request) throws Exception {
        var path = "/user";
        return this.makeRequest("POST",path, request,RegisterResponse.class, null);
    }
    public LoginResponse login(LoginRequest request) throws Exception{
        var path = "/session";
        return this.makeRequest("POST",path,request,LoginResponse.class, null);
    }
    public void clearAll() throws Exception {
        var path = "/db";
        this.makeRequest("DELETE",path,null,null,null);
    }
    private static void writeBody(Object request, HttpURLConnection http,String header) throws Exception {
        if(header != null) {
            http.addRequestProperty("authorization", header);
        }
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws Exception {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }catch (Exception ex){
                throw new Exception(ex.toString());
            }
        }
        return response;
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        switch(status){
            case 400-> throw new Exception("Bad Request Please Try Again");
            case 401-> throw new Exception("Unauthorized, try a different password");
            case 403-> throw new Exception("Already Taken");
        }
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public CreateGameResponse create(CreateGameRequest request) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path,request,CreateGameResponse.class,request.authToken());
    }
    public void logout(LogoutRequest request) throws Exception{
        var path = "/session";
        this.makeRequest("DELETE",path, null, null,request.authToken());
    }
    public ListGamesResult list (String authToken) throws Exception {
        var path = "/game";
        return this.makeRequest("GET", path, null,ListGamesResult.class,authToken);
    }
    public void join(JoinGameRequest request, String authToken) throws Exception {
        var path = "/game";
        this.makeRequest("PUT",path,request,null, authToken);
    }
}

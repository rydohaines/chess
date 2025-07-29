package server;

import com.google.gson.Gson;
import dataaccess.ResponseException;
import service.responses.*;

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
    public <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception{
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public RegisterResponse register(RegisterRequest request) throws Exception {
        var path = "/user";
        return this.makeRequest("POST",path, request,RegisterResponse.class);
    }
    public LoginResponse login(LoginRequest request) throws Exception{
        var path = "/session";
        return this.makeRequest("POST",path,request,LoginResponse.class);
    }
    public void clearAll() throws Exception {
        var path = "/db";
        this.makeRequest("DELETE",path,null,null);
    }
    private static void writeBody(Object request, HttpURLConnection http) throws Exception {
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
            }
        }
        return response;
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        switch(status){
            case 400-> throw new Exception("Bad Request Please Try Again");
            case 401-> throw new Exception("Unauthorized, try a different password");
            case 403-> throw new Exception("Username Already Taken");
        }
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public CreateGameResponse create(CreateGameRequest request) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path,request,CreateGameResponse.class);
    }
}

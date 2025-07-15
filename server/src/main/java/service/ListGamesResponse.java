package service;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public record ListGamesResponse(int gameID, String whiteUsername,String blackUsername,String gameName) {
}

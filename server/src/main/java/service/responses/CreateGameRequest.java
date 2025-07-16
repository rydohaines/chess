package service.responses;

public record CreateGameRequest(String authToken, String gameName) {
}

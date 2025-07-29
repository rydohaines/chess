package responses;

public record CreateGameRequest(String authToken, String gameName) {
}

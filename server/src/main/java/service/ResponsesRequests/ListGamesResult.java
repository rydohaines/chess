package service.ResponsesRequests;

import java.util.Collection;

public record ListGamesResult(Collection<ListGamesResponse> games) {
}

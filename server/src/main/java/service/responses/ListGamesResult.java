package service.responses;

import java.util.Collection;

public record ListGamesResult(Collection<ListGamesResponse> games) {
}

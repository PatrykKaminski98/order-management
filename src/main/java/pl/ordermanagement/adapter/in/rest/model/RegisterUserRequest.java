package pl.ordermanagement.adapter.in.rest.model;

public record RegisterUserRequest(String username, String password, String role) {
}

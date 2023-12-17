package zhurasem.client.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zhurasem.client.model.UserDto;
import zhurasem.client.model.UserWebModel;

@Component
public class UserClient {

    private static final String ONE_URI = "/{id}";
    private final WebClient userWebClient;

    public UserClient(@Value("${server_backend_url}") String backendUrl) {
        userWebClient = WebClient.create(backendUrl + "/users");
    }

    public Mono<UserWebModel> create(UserDto userDto) {
        return userWebClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserWebModel.class);
    }

    public Flux<UserWebModel> readAll() {
        return userWebClient
                .get()
                .retrieve()
                .bodyToFlux(UserWebModel.class);
    }

    public Mono<UserWebModel> readById(String username) {
        return userWebClient
                .get()
                .uri(ONE_URI, username)
                .retrieve()
                .bodyToMono(UserWebModel.class);
    }

    public Mono<UserWebModel> update(UserDto userDto) {
        return userWebClient
                .put()
                .uri(ONE_URI, userDto.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserWebModel.class);
    }

    public Mono<Void> delete(String username) {
        return userWebClient
                .delete()
                .uri(ONE_URI, username)
                .retrieve()
                .bodyToMono(Void.TYPE);
    }
}

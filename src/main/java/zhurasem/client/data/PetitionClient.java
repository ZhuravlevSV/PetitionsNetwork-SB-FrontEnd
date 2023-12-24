package zhurasem.client.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zhurasem.client.model.PetitionDto;
import zhurasem.client.model.PetitionWebModel;

@Component
public class PetitionClient {

    private static final String ONE_URI = "/{id}";
    private final WebClient petitionWebClient;

    public PetitionClient(@Value("${server_backend_url}") String backendUrl) {
        petitionWebClient = WebClient.create(backendUrl + "/petitions");
    }

    public Mono<PetitionWebModel> create(PetitionDto petitionDto) {
        return petitionWebClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(petitionDto)
                .retrieve()
                .bodyToMono(PetitionWebModel.class);
    }

    public Flux<PetitionWebModel> readAll() {
        return petitionWebClient
                .get()
                .retrieve()
                .bodyToFlux(PetitionWebModel.class);
    }

    public Mono<PetitionWebModel> readById(Long pid) {
        return petitionWebClient
                .get()
                .uri(ONE_URI, pid)
                .retrieve()
                .bodyToMono(PetitionWebModel.class);
    }

    public Mono<PetitionWebModel> update(PetitionDto petitionDto) {
        return petitionWebClient
                .put()
                .uri(ONE_URI, petitionDto.getPid())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(petitionDto)
                .retrieve()
                .bodyToMono(PetitionWebModel.class);
    }

    public Mono<Void> delete(Long pid) {
        return petitionWebClient
                .delete()
                .uri(ONE_URI, pid)
                .retrieve()
                .bodyToMono(Void.TYPE);
    }

    public Mono<PetitionWebModel> sign(Long pid, String username) {
        return petitionWebClient
                .post()
                .uri("/{pid}/sign/{username}", pid, username)
                .retrieve()
                .bodyToMono(PetitionWebModel.class);
    }

}

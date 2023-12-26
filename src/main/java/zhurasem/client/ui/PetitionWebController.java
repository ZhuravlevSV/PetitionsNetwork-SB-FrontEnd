package zhurasem.client.ui;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zhurasem.client.data.PetitionClient;
import zhurasem.client.data.UserClient;
import zhurasem.client.model.PetitionWebModel;
import zhurasem.client.model.UserWebModel;

import java.util.List;

@Controller
public class PetitionWebController {

    private final PetitionClient petitionClient;
    private final UserClient userClient;

    public PetitionWebController(PetitionClient petitionClient, UserClient userClient) {
        this.petitionClient = petitionClient;
        this.userClient = userClient;
    }

    @GetMapping("/petitions")
    public String list(Model model) {
        model.addAttribute("petitions", petitionClient.readAll());
        return "petitions";
    }

    @GetMapping("/petitions/add")
    public String addPetition(Model model) {
        model.addAttribute("petitionWebModel", new PetitionWebModel());
        model.addAttribute("userList", userClient.readAll());
        return "petitionAdd";
    }

    @PostMapping("/petitions/add")
    public String addPetitionSubmit(@ModelAttribute PetitionWebModel petitionWebModel, Model model) {
        model.addAttribute("petitionWebModel", petitionClient
                .create(petitionWebModel)
                .onErrorResume(WebClientResponseException.Conflict.class,
                        e -> Mono.just(new PetitionWebModel(true, petitionWebModel))));
        return "petitionAdd";
    }

    @GetMapping("/petitions/{id}/sign")
    public String signPetition(@PathVariable Long id, Model model) {
        Mono<PetitionWebModel> petitionMono = petitionClient.readById(id);
        Flux<UserWebModel> allUsers = userClient.readAll();

        Mono<List<UserWebModel>> signedUsersMono = petitionMono
                .flatMap(petition -> Flux.fromIterable(petition.getSignedUsersIds())
                        .flatMap(signedUserId -> allUsers.filter(user -> user.getUsername().equals(signedUserId)))
                        .collectList());

        Mono<List<UserWebModel>> notSignedUsersMono = signedUsersMono
                .flatMap(signedUsers -> allUsers
                        .filter(user -> signedUsers.stream().noneMatch(existingUser -> existingUser.getUsername().equals(user.getUsername())))
                        .collectList());

        model.addAttribute("petition", petitionMono.block()); // блокируем Mono для получения значения
        model.addAttribute("signedUsers", signedUsersMono.block()); // блокируем Mono для получения значения
        model.addAttribute("notSignedUsers", notSignedUsersMono.block()); // блокируем Mono для получения значения

        return "petitionSign";
    }

    @PostMapping("/petitions/{id}/sign/{authorId}")
    public Mono<String> signPetitionConfirm(@PathVariable Long id, @PathVariable String authorId, Model model) {
        return petitionClient.sign(id, authorId)
                .thenReturn("redirect:/petitions");
    }

    @GetMapping("/petitions/{username}/myPetitions")
    public String listMyPetitions(@PathVariable String username, Model model) {
        model.addAttribute("author", userClient.readById(username));
        model.addAttribute("petitions", petitionClient.readAllUserPetitions(username));
        return "petitionsMyPetitions";
    }


}

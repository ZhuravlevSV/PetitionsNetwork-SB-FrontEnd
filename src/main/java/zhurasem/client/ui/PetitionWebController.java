package zhurasem.client.ui;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import zhurasem.client.data.PetitionClient;
import zhurasem.client.data.UserClient;
import zhurasem.client.model.PetitionWebModel;

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
}

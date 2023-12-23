package zhurasem.client.ui;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zhurasem.client.data.PetitionClient;
import zhurasem.client.model.PetitionWebModel;

@Controller
public class PetitionWebController {

    private final PetitionClient petitionClient;

    public PetitionWebController(PetitionClient petitionClient) {
        this.petitionClient = petitionClient;
    }

    @GetMapping("/petitions")
    public String list(Model model) {
        model.addAttribute("petitions", petitionClient.readAll());
        return "petitions";
    }

    @GetMapping("/petitions/add")
    public String addPetition(Model model) {
        model.addAttribute("petitionWebModel", new PetitionWebModel());
        return "petitionAdd";
    }
}

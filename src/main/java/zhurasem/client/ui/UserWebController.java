package zhurasem.client.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import zhurasem.client.data.UserClient;
import zhurasem.client.model.UserDto;
import zhurasem.client.model.UserWebModel;

@Controller
public class UserWebController {

    private final UserClient userClient;

    public UserWebController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", userClient.readAll());
        return "users";
    }

    @GetMapping("/users/add")
    public String addUser(Model model) {
        model.addAttribute("userWebModel", new UserWebModel());
        return "userAdd";
    }

    @PostMapping("/users/add")
    public String addUserSubmit(@ModelAttribute UserWebModel userWebModel, Model model) {
        model.addAttribute("userWebModel", userClient
                .create(userWebModel)
                .onErrorResume(WebClientResponseException.Conflict.class,
                        e -> Mono.just(new UserWebModel(true, userWebModel))));
        return "userAdd";
    }

    @GetMapping("/users/edit")
    public String editUser(@RequestParam String username, Model model) {
        model.addAttribute("userDto", userClient.readById(username));
        return "userEdit";
    }

    @PostMapping("/users/edit")
    public String editUserSubmit(@ModelAttribute UserDto userDto, Model model) {
        model.addAttribute("userDto", userClient.update(userDto));
        return "redirect:/users";
    }

    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam String username, Model model) {
        model.addAttribute("username", userClient.delete(username));
        return "redirect:/users";
    }
}

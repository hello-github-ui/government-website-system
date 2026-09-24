package com.example.gov.controller.home;

import com.example.gov.common.SessionKeys;
import com.example.gov.entity.Consult;
import com.example.gov.service.ConsultService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

/**
 * 在线办事/公众服务控制器。
 */
@Controller
@RequestMapping("/service")
@Slf4j
public class HomeServiceController {

    private final ConsultService consultService;

    public HomeServiceController(ConsultService consultService) {
        this.consultService = consultService;
    }

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("title", "公众服务");
        return "home/service";
    }

    @GetMapping("/guide")
    public String guide(Model model) {
        model.addAttribute("title", "办事指南");
        return "home/service-guide";
    }

    @GetMapping("/consult")
    public String consult(Model model) {
        model.addAttribute("title", "在线咨询");
        return "home/service-consult";
    }

    /**
     * 服务页快速提交咨询。
     */
    @PostMapping("/submitConsult")
    public String submitConsult(@RequestParam String name,
                                @RequestParam String phone,
                                @RequestParam(required = false) String email,
                                @RequestParam String title,
                                @RequestParam String content,
                                @RequestParam(defaultValue = "1") Integer type,
                                HttpSession session,
                                RedirectAttributes ra) {
        if (name == null || name.isBlank() || phone == null || phone.isBlank()
            || title == null || title.isBlank() || content == null || content.isBlank()) {
            ra.addFlashAttribute("flashError", "请填写必填项");
            return "redirect:/service/consult";
        }
        Consult c = new Consult();
        c.setUserId((Long) session.getAttribute(SessionKeys.USER_ID) == null ? 0L
            : (Long) session.getAttribute(SessionKeys.USER_ID));
        c.setType(type);
        c.setTitle(title);
        c.setContent(content);
        c.setContactName(name);
        c.setContactPhone(phone);
        c.setContactEmail(email);
        c.setStatus(0);
        consultService.create(c);
        ra.addFlashAttribute("flashMessage", "提交成功，我们会尽快回复您");
        return "redirect:/service";
    }
}

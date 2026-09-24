package com.example.gov.controller.admin;

import com.example.gov.entity.Nav;
import com.example.gov.mapper.NavMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台导航管理控制器。
 */
@Controller
@RequestMapping("/admin/nav")
@Slf4j
public class AdminNavController {

    private final NavMapper navMapper;

    public AdminNavController(NavMapper navMapper) {
        this.navMapper = navMapper;
    }

    @GetMapping({"", "/"})
    public String list(Model model) {
        List<Nav> all = navMapper.selectAll();
        model.addAttribute("navs", buildTree(all, 0L));
        return "admin/nav-list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        log.info("[{}] AdminNavController.addPage 调用", Thread.currentThread().getName());
        model.addAttribute("parentNavs", navMapper.selectByParent(0L));
        return "admin/nav-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam(value = "nav_name") String navName,
                      @RequestParam(value = "nav_url", required = false) String navUrl,
                      @RequestParam(defaultValue = "1") Integer navType,
                      @RequestParam(value = "parent_id", defaultValue = "0") Long parentId,
                      @RequestParam(defaultValue = "_self") String target,
                      @RequestParam(defaultValue = "0") Integer sort,
                      @RequestParam(value = "is_show", defaultValue = "1") Integer isShow,
                      RedirectAttributes ra) {
        Nav n = new Nav();
        n.setNavName(navName);
        n.setNavUrl(navUrl);
        n.setNavType(navType);
        n.setParentId(parentId);
        n.setTarget(target);
        n.setSort(sort);
        n.setIsShow(isShow);
        navMapper.insert(n);
        ra.addFlashAttribute("flashMessage", "添加成功");
        return "redirect:/admin/nav";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("nav", navMapper.findById(id));
        model.addAttribute("parentNavs", navMapper.selectByParent(0L));
        return "admin/nav-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam(value = "nav_name") String navName,
                       @RequestParam(value = "nav_url", required = false) String navUrl,
                       @RequestParam(defaultValue = "1") Integer navType,
                       @RequestParam(value = "parent_id", defaultValue = "0") Long parentId,
                       @RequestParam(defaultValue = "_self") String target,
                       @RequestParam(defaultValue = "0") Integer sort,
                       @RequestParam(value = "is_show", defaultValue = "1") Integer isShow,
                       RedirectAttributes ra) {
        if (id.equals(parentId)) {
            ra.addFlashAttribute("flashError", "不能将导航设置为自己的子导航");
            return "redirect:/admin/nav/edit/" + id;
        }
        Nav n = navMapper.findById(id);
        n.setNavName(navName);
        n.setNavUrl(navUrl);
        n.setNavType(navType);
        n.setParentId(parentId);
        n.setTarget(target);
        n.setSort(sort);
        n.setIsShow(isShow);
        navMapper.update(n);
        ra.addFlashAttribute("flashMessage", "保存成功");
        return "redirect:/admin/nav";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        log.info("[{}] AdminNavController.delete 调用, 参数: id={}", Thread.currentThread().getName(), id);
        if (navMapper.countChildren(id) > 0) {
            ra.addFlashAttribute("flashError", "请先删除子导航");
            return "redirect:/admin/nav";
        }
        navMapper.deleteById(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/nav";
    }

    private List<Nav> buildTree(List<Nav> all, Long parentId) {
        List<Nav> tree = new ArrayList<>();
        for (Nav n : all) {
            long pid = n.getParentId() == null ? 0L : n.getParentId();
            if (pid == parentId) {
                tree.add(n);
            }
        }
        return tree;
    }
}

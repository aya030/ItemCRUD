package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Item;
import com.example.demo.form.ItemForm;
import com.example.demo.service.ItemService;

@Controller
@RequestMapping("/items")
public class ItemController {

	@Autowired
	ItemService itemService;

	/* Top */
	@RequestMapping("/index")
	public String index(ItemForm itemForm, Model model) {

		model.addAttribute("title", "商品APP_一覧画面");
		model.addAttribute("logo", "AyaDesign");
		List<Item> itemList = itemService.getItemList();
		model.addAttribute("itemList", itemList);
		return "index";
	}

	/* 検索結果 */
	@GetMapping("/search")
	public String search(ItemForm itemForm, Model model) {

		model.addAttribute("title", "商品APP_検索結果");
		model.addAttribute("logo", "AyaDesign");

		Item item = itemService.findById(itemForm.getId());
		model.addAttribute("item", item);

		return "items/search";
	}

	/* 詳細 */
	@GetMapping("/detail/id={id}")
	public String detail(ItemForm itemForm, @PathVariable("id") Integer id, Model model) {

		model.addAttribute("title", "商品APP_商品詳細");
		model.addAttribute("logo", "AyaDesign");

		Item item = itemService.findById(id);
		model.addAttribute("item", item);

		return "items/detail";
	}

	/* 新規商品登録 */
	@GetMapping("/new")
	public String newItem(Model model, @ModelAttribute Item item) {

		model.addAttribute("title", "商品APP_新規登録");
		model.addAttribute("logo", "AyaDesign");

		model.addAttribute("item", item);
		Map<String, String> radioCategory;
		radioCategory = itemService.initRadioCategory();
		model.addAttribute("radioCategory", radioCategory);
		return "items/new";
	}

	@PostMapping("/new")
	public String create(@Validated @ModelAttribute Item item, BindingResult result) {
		if (result.hasErrors()) {
			return "items/new";
		}
		itemService.insertOne(item);
		return "redirect:/items/index";
	}

	/* 更新 */
	@GetMapping("/edit/id={id}")
	public String edit(@PathVariable("id") int id, Model model) {

		model.addAttribute("title", "商品APP_更新");
		model.addAttribute("logo", "AyaDesign");

		model.addAttribute("item", itemService.findById(id));
		Map<String, String> radioCategory;
		radioCategory = itemService.initRadioCategory();
		model.addAttribute("radioCategory", radioCategory);
		return "items/edit";
	}

	@PostMapping("/edit/id={id}")
	public String update(@ModelAttribute Item item) {
		itemService.updateOne(item.getId(), item.getName(), item.getPrice(), item.getCategory(), item.getNum());
		return "redirect:/items/index";
	}

	/* 削除 */
	@PostMapping("/delete/id={id}")
	public String delete(@PathVariable("id") int id) {
		itemService.deleteOne(id);
		return "redirect:/items/index";
	}
}

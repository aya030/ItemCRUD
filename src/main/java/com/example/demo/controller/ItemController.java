package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

	private final ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	/* Top */
	@GetMapping("/index")
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

		Optional<Item> itemSearch = itemService.findById(itemForm.getId());
		if (itemSearch.isPresent()) {
			Item item = itemSearch.get();
			model.addAttribute("item", item);
			return "items/search";
		} else {
			model.addAttribute("title", "商品APP_一覧画面");
			model.addAttribute("logo", "AyaDesign");

			return "error/404";
		}
	}

	/* 詳細 */
	@GetMapping("/detail/id={id}")
	public String detail(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("title", "商品APP_商品詳細");
		model.addAttribute("logo", "AyaDesign");

		Optional<Item> itemSearch = itemService.findById(id);
		Item item = itemSearch.get();
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
	public String create(Model model, @Validated @ModelAttribute Item item, BindingResult result) {

		if (result.hasErrors()) {
			model.addAttribute("title", "商品APP_新規登録");
			model.addAttribute("logo", "AyaDesign");
			Map<String, String> radioCategory = itemService.initRadioCategory();
			model.addAttribute("radioCategory", radioCategory);
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

		model.addAttribute("item", itemService.findById(id).get());
		Map<String, String> radioCategory;
		radioCategory = itemService.initRadioCategory();
		model.addAttribute("radioCategory", radioCategory);
		return "items/edit";
	}

	@PostMapping("/edit/id={id}")
	public String update(Model model, @Validated @ModelAttribute Item item, BindingResult result) {

		if (result.hasErrors()) {
			model.addAttribute("title", "商品APP_更新");
			model.addAttribute("logo", "AyaDesign");

			Map<String, String> radioCategory = itemService.initRadioCategory();
			model.addAttribute("radioCategory", radioCategory);
			return "items/new";
		}
		itemService.updateOne(item.getId(), item.getName(), item.getPrice(), item.getCategory(), item.getNum());
		return "redirect:/items/index";
	}

	/* 削除 */
	@PostMapping("/delete/id={id}")
	public String delete(@PathVariable("id") int id) {
		itemService.deleteOne(id);
		return "redirect:/items/index";
	}

	/*
	 * 例外処理
	 */

	/* /detail/{id}のIDがない時 */
	@ExceptionHandler(MissingPathVariableException.class)
	public String missingPathVariableExceptionHandler(Model model) {
		model.addAttribute("status", "500エラー");
		model.addAttribute("error", "MissingPathVariableException");
		model.addAttribute("message", "IDが不正です");

		return "error";
	}

	/* 変更・削除ボタンのIDがない時 */
	@ExceptionHandler(NumberFormatException.class)
	public String NumberFormatExceptionHandler(Model model) {
		model.addAttribute("status", "400エラーです");
		model.addAttribute("error", "NumberFormatException");
		model.addAttribute("message", "IDが不正です");

		return "error";
	}

	/* 検索がID(数字）以外の時 */
	@ExceptionHandler(BindException.class)
	public String bindExceptionHandler(Model model) {
		model.addAttribute("title", "商品APP_一覧画面");
		model.addAttribute("logo", "AyaDesign");
		List<Item> itemList = itemService.getItemList();
		model.addAttribute("itemList", itemList);

		model.addAttribute("message", "* IDが不正です。数字を入力してください");
		return "index";
	}
}

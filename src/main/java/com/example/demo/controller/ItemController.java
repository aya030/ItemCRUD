package com.example.demo.controller;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

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
		model.addAttribute("itemList", itemService.getItemList());
		return "index";
	}

	/* 検索結果 */
	@GetMapping("/search")
	public String search(@RequestParam("id") String id, Model model,
			@Validated @ModelAttribute("itemForm") ItemForm itemForm, BindingResult result) {

		model.addAttribute("title", "商品APP_検索結果");
		model.addAttribute("logo", "AyaDesign");

		try {
			if (result.hasErrors()) {

				model.addAttribute("title", "商品APP_一覧画面");
				model.addAttribute("message", "* 入力の値がありません。IDを入力してください");

				return "index";

			} else {
				Optional<Item> itemSearch = itemService.findById(itemForm.getId());
				model.addAttribute("item", itemSearch.get());
				return "items/search";
			}
		} catch (NoSuchElementException e) {
			model.addAttribute("title", "商品APP_一覧画面");
			model.addAttribute("message", "* IDが" + id + "の商品は存在しません");
			return "index";
		}
	}

	/* 詳細 */
	@GetMapping("/detail/id={id}")
	public String detail(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("title", "商品APP_商品詳細");
		model.addAttribute("logo", "AyaDesign");

		Optional<Item> itemSearch = itemService.findById(id);
		Item item = itemSearch.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		model.addAttribute("item", item);

		return "items/detail";
	}

	/* 新規商品登録 */
	@GetMapping("/new")
	public String newItem(Model model, @ModelAttribute Item item) {

		model.addAttribute("title", "商品APP_新規登録");
		model.addAttribute("logo", "AyaDesign");

		model.addAttribute("item", item);
		model.addAttribute("radioCategory", itemService.initRadioCategory());
		return "items/new";
	}

	@PostMapping("/new")
	public String create(Model model, @Validated @ModelAttribute Item item, BindingResult result) {

		if (result.hasErrors()) {
			model.addAttribute("title", "商品APP_新規登録");
			model.addAttribute("logo", "AyaDesign");
			model.addAttribute("radioCategory", itemService.initRadioCategory());
			return "items/new";
		}
		itemService.insertOne(item);
		return "redirect:/items/index";
	}

	/* 更新 */
	@GetMapping("/edit/id={id}")
	public String edit(@PathVariable("id") int id, Model model) {

		Optional<Item> itemSerach = itemService.findById(id);
		Item item = itemSerach.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		model.addAttribute("title", "商品APP_更新");
		model.addAttribute("logo", "AyaDesign");

		model.addAttribute("item", item);
		model.addAttribute("radioCategory", itemService.initRadioCategory());

		return "items/edit";
	}

	@PostMapping("/edit/id={id}")
	public String update(@PathVariable("id") int id, Model model, @Validated @ModelAttribute Item item,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("title", "商品APP_更新");
			model.addAttribute("logo", "AyaDesign");

			model.addAttribute("radioCategory", itemService.initRadioCategory());
			return "items/edit";
		}
		Optional<Item> itemSearch = itemService.findById(id);
		itemSearch.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		itemService.updateOne(id, item.getName(), item.getPrice(), item.getCategory(), item.getNum());
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
		model.addAttribute("itemList", itemService.getItemList());

		model.addAttribute("message", "* IDが不正です。数字を入力してください");
		return "index";
	}

	/* 400エラー対策 Searchの@RequestParamのパラメーターが存在しない場合の例外 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public String MissingServletRequestParameterExceptionHandler(Model model) {
		model.addAttribute("title", "商品APP_一覧画面");
		model.addAttribute("logo", "AyaDesign");
		model.addAttribute("itemList", itemService.getItemList());

		return "index";
	}
}

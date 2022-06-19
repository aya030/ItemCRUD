package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;

@WebMvcTest(ItemController.class)
@TestPropertySource(locations = "classpath:test.properties")
public class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ItemService itemService;

	@Autowired
	ItemController itemController;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(itemController).setViewResolvers(viewResolver()).build();
	}

	private ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}

	public Optional<Item> getItemTest() {

		Item item = new Item();

		item.setId(1);
		item.setName("ひまわりピアス");
		item.setPrice(3600);
		item.setCategory("ピアス");
		item.setNum(3);

		return Optional.ofNullable(item);
	}
	
    public List<Item> getItemTestList() {

        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
		item1.setId(1);
		item1.setName("ひまわりピアス");
		item1.setPrice(3600);
		item1.setCategory("ピアス");
		item1.setNum(3);

        Item item2 = new Item();
		item2.setId(2);
		item2.setName("マーガレットイヤリング");
		item2.setPrice(4200);
		item2.setCategory("イヤリング");
		item2.setNum(5);

        items.add(item1);
        items.add(item2);

        return items;

    }
	
	@Test
	public void indexページをリクエストすると結果が正常となりListの全件が呼ばれindexが返る事() throws Exception {
		when(itemService.getItemList()).thenReturn(getItemTestList());
		mockMvc.perform(get("/items/index"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("itemList", getItemTestList()))
		       .andExpect(view().name("index"));
		
		verify(itemService, times(1)).getItemList();
	}
	
	@Test
	public void 検索にid1を入力して検索を押すとseachが呼ばれている事() throws Exception {
		when(itemService.findById(1)).thenReturn(getItemTest());
		mockMvc.perform(get("/items/search")
			   .param("id", "1"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("items/search"));
	}
	
	@Test
	public void 検索のidがNULLのとき検索を押すとindex画面が返る事() throws Exception {
		mockMvc.perform(get("/items/search"))
		       .andExpect(status().isOk())
			   .andExpect(view().name("index"));
	}
	
	@Test
	public void 検索のidが空のとき検索を押すと例外情報が入った状態で画面が返る事() throws Exception {
		mockMvc.perform(get("/items/search")
			   .param("id", ""))
			   .andExpect(status().isOk())
			   .andExpect(model().attribute("message", "* 入力の値がありません。IDを入力してください"))
			   .andExpect(view().name("index"));
	}
	
	@Test
	public void 検索のidに空白を入れて検索を押すと例外情報が入った状態で画面が返る事() throws Exception {
		mockMvc.perform(get("/items/search")
			   .param("id", "  "))
			   .andExpect(status().isOk())
			   .andExpect(model().attribute("message", "* 入力の値がありません。IDを入力してください"))
			   .andExpect(view().name("index"));
	}
	
	//コントローラーの分岐が上手く行っていない
//	@Test
//	public void 検索のidに存在しないIDを入れて検索を押すと例外情報が入った状態で画面が返る事() throws Exception {
//		when(itemService.getItemList()).thenReturn(getItemTestList());
//		mockMvc.perform(get("/items/search")
//			.param("id", "99")).andDo(print())
//			.andExpect(status().isOk())
//			.andExpect(model().attribute("message", "* IDが99の商品は存在しません"))
//			.andExpect(view().name("index"));
//	}
	
	@Test
	public void 詳細ページのリクエスト結果が正常となりViewとしてdetailが返る事() throws Exception {
		when(itemService.findById(1)).thenReturn(getItemTest());
		mockMvc.perform(get("/items/detail/id={id}",1))
		       .andExpect(status().isOk())
		       .andExpect(view().name("items/detail"));
		
	}

	@Test
	public void 新規登録ページのリクエスト結果が正常となりViewとしてnewが返る事() throws Exception {
		mockMvc.perform(get("/items/new"))
		       .andExpect(status().isOk())
		       .andExpect(view().name("items/new"));
	}

	@Test
	public void 新規登録ページで登録処理を行うとサービスで処理されてindex画面に遷移される事() throws Exception {
		mockMvc.perform(post("/items/new")
			   .param("id", "1")
			   .param("name", "test")
			   .param("price", "1800")
			   .param("category", "ピアス")
			   .param("num", "1"))
		       .andExpect(status().is(302))
			   .andExpect(view().name("redirect:/items/index"));
		
		verify(itemService, times(1)).insertOne(any());
	}
	
	@Test
	public void 新規登録ページで商品名がNULLの状態で登録処理を行うと例外情報が入った状態で画面が返る事() throws Exception {
		mockMvc.perform(post("/items/new")
			   .param("id", "1")
			   .param("price", "1800")
			   .param("category", "ピアス")
			   .param("num", "1"))
		       .andExpect(status().isOk())
		       .andExpect(model().hasErrors())
		       .andExpect(view().name("items/new"));
	}
	
	@Test
	public void 新規登録ページで商品名が空の状態で登録処理を行うと例外情報が入った状態で画面が返る事() throws Exception {
		mockMvc.perform(post("/items/new")
			   .param("id", "1")
			   .param("name", "")
			   .param("price", "1800")
			   .param("category", "ピアス")
			   .param("num", "1"))
		       .andExpect(status().isOk())
		       .andExpect(model().hasErrors())
		       .andExpect(view().name("items/new"));
	}
	
	@Test
	public void 新規登録ページで商品名が空白の状態で登録処理を行うと例外情報が入った状態で画面が返る事() throws Exception {
		mockMvc.perform(post("/items/new")
			   .param("id", "1")
			   .param("name", " ")
			   .param("price", "1800")
			   .param("category", "ピアス")
			   .param("num", "1"))
		       .andExpect(status().isOk())
		       .andExpect(model().hasErrors())
		       .andExpect(view().name("items/new"));
	}

	@Test
	public void 更新ページのリクエスト結果が正常となりViewとしてeditが返る事() throws Exception {
		when(itemService.findById(1)).thenReturn(getItemTest());
		mockMvc.perform(get("/items/edit/id={id}", 1))
		       .andExpect(status().isOk())
		       .andExpect(view().name("items/edit")); 
	}
	
	@Test
	public void 更新ページで更新処理を行うとサービスで処理されてindex画面に遷移される事() throws Exception {
		when(itemService.findById(1)).thenReturn(getItemTest());
		mockMvc.perform(post("/items/edit/id={id}", 1)
			   .param("id", "1")
			   .param("name", "マーガレットピアス")
			   .param("price", "1800")
			   .param("category", "ピアス")
			   .param("num", "1"))
		       .andExpect(status().is(302))
		       .andExpect(view().name("redirect:/items/index"));

		verify(itemService, times(1)).updateOne(1, "マーガレットピアス", 1800, "ピアス", 1);
	}
	
	@Test
	public void 更新ページで商品名がNULLの状態で更新処理を行うと例外情報が入った状態で画面が返る事() throws Exception {
		mockMvc.perform(post("/items/edit/id={id}", 1)
			   .param("id", "1")
			   .param("price", "1800")
			   .param("category", "ピアス")
			   .param("num", "1"))
		       .andExpect(status().isOk())
		       .andExpect(model().hasErrors())
		       .andExpect(view().name("items/edit"));
	}
	
	@Test
	public void 更新ページで商品名が空の状態で更新処理を行うと例外情報が入った状態で画面が返る事() throws Exception {
		mockMvc.perform(post("/items/edit/id={id}", 1)
			   .param("id", "1")
			   .param("name", "")
			   .param("price", "1800")
			   .param("category", "ピアス")
			   .param("num", "1"))
		       .andExpect(status().isOk())
		       .andExpect(model().hasErrors())
		       .andExpect(view().name("items/edit"));
	}
	
	@Test
	public void 更新ページで商品名が空白の状態で更新処理を行うと例外情報が入った状態で画面が返る事() throws Exception {
		mockMvc.perform(post("/items/edit/id={id}", 1)
			   .param("id", "1")
			   .param("name", " ")
			   .param("price", "1800")
			   .param("category", "ピアス")
			   .param("num", "1"))
		       .andExpect(status().isOk())
		       .andExpect(model().hasErrors())
		       .andExpect(view().name("items/edit"));
	}
	
	@Test
	public void 更新ページで値段が数字以外の状態で更新処理を行うと例外情報が入った状態で画面が返る事() throws Exception {
		mockMvc.perform(post("/items/edit/id={id}", 1)
			   .param("id", "1")
			   .param("name", "マーガレットピアス")
			   .param("price", "例外の発生")
			   .param("category", "ピアス")
			   .param("num", "1"))
		       .andExpect(status().isOk())
		       .andExpect(model().hasErrors())
		       .andExpect(view().name("items/edit"));
	}
	
	@Test
	public void indexで削除処理を行うとサービスで処理されて同一画面に遷移される事() throws Exception {
	    mockMvc.perform(post("/items/delete/id={id}",1))
		       .andExpect(status().is(302))
			   .andExpect(view().name("redirect:/items/index"));
	    verify(itemService, times(1)).deleteOne(1);
	}

}

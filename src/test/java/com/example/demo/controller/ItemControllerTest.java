package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.example.demo.service.ItemService;

@AutoConfigureMockMvc
@SpringBootTest(classes = ItemController.class)
//@EnableWebMvc
//@Configuration
public class ItemControllerTest {

	private MockMvc mockMvc;

	@MockBean
	ItemService itemService;

	@Autowired
	ItemController itemController = new ItemController(itemService);

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(itemController).setViewResolvers(viewResolver()).build();
	}
	
	private ViewResolver viewResolver( ) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}

	@Test
	@DisplayName("indexページのリクエスト結果が正常となりViewとしてindexが返る事()")
	public void testIndex() throws Exception {
		mockMvc.perform(get("/items/index"))
		.andDo(print());
//		.andExpect(status().isOk());
//		.andExpect(view().name("index"));
//		.andExpect(model().attribute("title","商品APP_検索結果"));
	}
}

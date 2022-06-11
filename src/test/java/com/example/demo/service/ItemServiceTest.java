package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemMapper;

@SpringBootTest(classes = ItemService.class)
public class ItemServiceTest {
	@MockBean
	ItemMapper itemMapper;

	@Autowired
	ItemService itemService = new ItemService(itemMapper);
	
	Item item = new Item();

	@BeforeEach
	public void beforeEach() {
		item.setId(1);
		item.setName("ひまわりピアス");
		item.setPrice(1800);
		item.setCategory("ピアス");
		item.setNum(2);
	}

	@Test
	public void 検索idに1を渡すとidが１に紐づいたデータと一致する() {
		int id = 1;
		when(itemMapper.findById(id)).thenReturn(Optional.of(item));

		Item findById = itemService.findById(id).get();
		assertEquals(1, findById.getId());
		assertEquals("ひまわりピアス", findById.getName());
		assertEquals(1800, findById.getPrice());
		assertEquals("ピアス", findById.getCategory());
		assertEquals(2, findById.getNum());
	}

	@Test
	public void 検索idにNULLを渡してもNullPointerExceptionにならない() {
		int id = 1;
		when(itemMapper.findById(id)).thenReturn(Optional.of(item));
		assertNotNull(itemService.findById(2));
	}

	@Test
	public void データをリスト表示するとデータ数が一致する() {
		Item item1 = new Item(2, "マーガレットピアス", 2400, "ピアス", 3);
		Item item2 = new Item(3, "チューリップイヤリング", 3450, "イヤリング", 5);
		when(itemMapper.findAll()).thenReturn(List.of(item1, item2));
		assertEquals(2, itemService.getItemList().size());
		assertThat(itemService.getItemList()).hasSize(2);
	}

	@Test
	public void 登録処理を呼び出したときにmapperの登録処理が1回のみ実行され正常に処理が完了すること() {
		itemService.insertOne(item);
		verify(itemMapper, times(1)).insertOne(item);
	}

	@Test
	public void idが2の更新処理を呼び出したときにmapperの更新処理が1回のみ実行され正常に処理が完了すること() {
		itemService.updateOne(2, "マーガレットピアス", 2400, "ピアス", 3);
		verify(itemMapper, times(1)).updateOne(2, "マーガレットピアス", 2400, "ピアス", 3);
	}

	@Test
	public void idが1の削除処理を呼び出したら削除処理が1回のみ実行され正常に処理が完了すること() {
		itemService.deleteOne(1);
		verify(itemMapper, times(1)).deleteOne(1);
	}

	@Test
	public void initRadioCategoryのMapの値が保持できていること() {
		Map<String, String> expected = new HashMap<>();
		expected.put("0", "ピアス");
		expected.put("1", "イヤリング");
		expected.put("2", "ネックレス");
		assertEquals(expected, itemService.initRadioCategory());
		
		/*initRadioCategory()の要素数は3である*/
		assertEquals(3, itemService.initRadioCategory().size());
	}

}

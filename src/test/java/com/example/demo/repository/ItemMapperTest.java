package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.example.demo.entity.Item;


@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class ItemMapperTest {

	@Autowired
	private ItemMapper itemMapper;
	
	@Test
	@Order(1)
	public void 検索を1件して結果がIDに紐づく1件だけ取得出来る事() throws Exception {
		Item findById = itemMapper.findById(1).get();
		
		assertEquals("ひまわりピアス", findById.getName());
		assertEquals(3600, findById.getPrice());
		assertEquals("ピアス", findById.getCategory());
		assertEquals(3, findById.getNum());
	}
	
	@Test
	@Order(2)
	public void 検索_全件して結果をリストで取得出来る事() throws Exception {
		
		assertEquals(3, itemMapper.findAll().size());
	}
	
	@Test
	@Order(3)
	public void 新規登録が出来る事() throws Exception {

		Item item = new Item(4, "パンジーイヤリング",2400,"イヤリング",2);
		itemMapper.insertOne(item);
		
		Item findById = itemMapper.findById(4).get();
		assertEquals(4, findById.getId());
		assertEquals("パンジーイヤリング", findById.getName());
		assertEquals(2400, findById.getPrice());
		assertEquals("イヤリング", findById.getCategory());
		assertEquals(2, findById.getNum());
		
		assertEquals(4, itemMapper.findAll().size());
	}
	
	@Test
	@Order(4)
	public void IDに紐づく1件の更新が出来る事() throws Exception {

		itemMapper.updateOne(1,"ひまわりイヤリング",3800,"イヤリング",1);
		
		Item findById = itemMapper.findById(1).get();
		assertEquals(1, findById.getId());
		assertEquals("ひまわりイヤリング", findById.getName());
		assertEquals(3800, findById.getPrice());
		assertEquals("イヤリング", findById.getCategory());
		assertEquals(1, findById.getNum());
	}
	
	@Test
	@Order(5)
	public void IDに紐づく1件の削除が出来る事() throws Exception {
		
		itemMapper.deleteOne(2);
		assertEquals(3, itemMapper.findAll().size());
	}
	
	@Test
	@Order(6)
	public void 既に存在するキーで登録しようとするとDuplicateKeyExceptionとなる事() throws Exception {
		
		Item item = new Item(1, "チューリップイヤリング",2800,"イヤリング",5);
		
		assertThatThrownBy(() -> {
			itemMapper.insertOne(item);
		}).isInstanceOf(DuplicateKeyException.class);
	}
	
	@Test
	@Order(7)
	public void NULLで登録しようとするとDataIntegrityViolationExceptionとなる事() throws Exception {
		
		assertThatThrownBy(() -> {
			itemMapper.insertOne(null);
		}).isInstanceOf(DataIntegrityViolationException.class);
	}	
}


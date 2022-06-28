package com.example.demo.repository;

import com.example.demo.entity.Item;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MissingPathVariableException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@SpringBootTest
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class ItemMapperTest {

	@Autowired
	private ItemMapper itemMapper;

	@Test
	@DataSet("items.yml")
	public void 検索を1件して結果がIDに紐づく1件だけ取得出来る事() {

		Item actual = itemMapper.findById(1).get();

		assertEquals("ひまわりピアス", actual.getName());
		assertEquals(3600, actual.getPrice());
		assertEquals("ピアス", actual.getCategory());
		assertEquals(3, actual.getNum());
	}

	@Test
	@DataSet("items.yml")
	public void 検索_全件して結果をリストで取得出来る事() throws Exception {

		List<Item> actual = itemMapper.findAll();
		assertEquals(3, actual.size());

	}

	@Test
	@DataSet("items.yml")
	public void 新規登録が出来る事() throws Exception {

		Item item = new Item(4, "パンジーイヤリング", 2400, "イヤリング", 2);
		itemMapper.insertOne(item);
		Item actual = itemMapper.findById(4).get();

		/* 新規登録されたIDに紐づいた値が同じかどうか */
		assertEquals(4, actual.getId());
		assertEquals("パンジーイヤリング", actual.getName());
		assertEquals(2400, actual.getPrice());
		assertEquals("イヤリング", actual.getCategory());
		assertEquals(2, actual.getNum());

		/* 新規登録されてリストの数が増えているかどうか */
		assertEquals(4, itemMapper.findAll().size());
	}

	@Test
	@DataSet("items.yml")
	public void IDに紐づく1件の更新が出来る事() throws Exception {

		itemMapper.updateOne(1, "チューリップイヤリング", 3800, "イヤリング", 1);
		Item actual = itemMapper.findById(1).get();

		assertEquals(1, actual.getId());
		assertEquals("チューリップイヤリング", actual.getName());
		assertEquals(3800, actual.getPrice());
		assertEquals("イヤリング", actual.getCategory());
		assertEquals(1, actual.getNum());

	}

	@Test
	@DataSet("items.yml")
	public void IDに紐づく1件の削除が出来る事() throws Exception {

		itemMapper.deleteOne(2);

		List<Item> actual = itemMapper.findAll();
		assertEquals(2, actual.size());
	}

	@Test
	@DataSet("items.yml")
	public void 既に存在するIDで登録しようとするとDuplicateKeyExceptionとなる事() throws Exception {

		Item item = new Item(1, "チューリップイヤリング", 2800, "イヤリング", 5);

		assertThatThrownBy(() -> {
			itemMapper.insertOne(item);
		}).isInstanceOf(DuplicateKeyException.class);
	}

	@Test
	public void NULLで登録しようとするとDataIntegrityViolationExceptionとなる事() throws Exception {

		assertThatThrownBy(() -> {
			itemMapper.insertOne(null);
		}).isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	@DataSet("items.yml")
	public void 存在しないIDで削除しようとしてもエラーが出ないこと() throws Exception {
		
		itemMapper.deleteOne(99);
		
		/* 存在しないIDを削除してもリストの数は変わらないこと */
		List<Item> actual = itemMapper.findAll();
		assertEquals(3, actual.size());
		
	}

}

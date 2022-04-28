package com.example.demo.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemMapper;

@Service
public class ItemService {

	@Autowired
	ItemMapper itemMapper;

	// 1件検索
	public Item findById(Integer id) {
		Item item = new Item();
		item.setId(id);
		return itemMapper.findById(item);
	}

	// 全件取得
	public List<Item> getItemList() {
		return itemMapper.findAll();
	}

	// 新規登録
	public void insertOne(Item item) {
		itemMapper.insertOne(item);
	}

	// 更新
	public void updateOne(int id, String name, int price, String category, int num) {
		itemMapper.updateOne(id, name, price, category, num);
	}

	// 削除
	public void deleteOne(Item item) {
		itemMapper.deleteOne(item);
	}

	// ラジオボタン(新規登録・更新)
	public Map<String, String> initRadioCategory() {
		Map<String, String> radio = new LinkedHashMap<>();
		radio.put("0", "ピアス");
		radio.put("1", "イヤリング");
		radio.put("2", "ネックレス");
		return radio;
	}

}

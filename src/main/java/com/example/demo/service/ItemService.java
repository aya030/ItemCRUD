package com.example.demo.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemMapper;

@Service
public class ItemService {
	private final ItemMapper itemMapper;

	public ItemService(ItemMapper itemMapper) {
		this.itemMapper = itemMapper;
	}

	// 1件検索
	public Optional<Item> findById(Integer id) {
		Optional<Item> opt = itemMapper.findById(id);
		if (opt.isPresent()) {
			return itemMapper.findById(id);
		} else {
			return Optional.empty();
		}
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
	public void updateOne(Integer id, String name, int price, String category, int num) {
		itemMapper.updateOne(id, name, price, category, num);
	}

	// 削除
	public void deleteOne(int id) {
		itemMapper.deleteOne(id);
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

package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Item;

@Mapper
public interface ItemMapper {
//1件検索
	public Item findById(Item item);

//全件取得
	public List<Item> findAll();

	// 登録
	public void insertOne(Item item);

	// 更新
	public void updateOne(@Param("id") int id, @Param("name") String name, @Param("price") int price,
			@Param("category") String category, @Param("num") int num);

	// 削除
	public void deleteOne(Item item);

}
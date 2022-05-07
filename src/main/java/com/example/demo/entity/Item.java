package com.example.demo.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Item {

	private Integer id;

	@NotBlank(message = "商品名を入力してください")
	private String name;

	@NotNull(message = "1桁以上の数値を入力してください")
	private Integer price;

	@NotBlank(message = "カテゴリーを選んでください")
	private String category;

	@NotNull(message = "1桁以上の数値を入力してください")
	private Integer num;

	public boolean isPresent() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}

package com.example.demo.repository;

import com.example.demo.entity.Item;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    assertEquals("ひピアス", actual.getName());
    assertEquals(3600, actual.getPrice());
    assertEquals("ピアス", actual.getCategory());
    assertEquals(3, actual.getNum());
  }

}


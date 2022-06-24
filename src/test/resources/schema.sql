--itemtableがあれば削除
DROP TABLE IF EXISTS itemtable;

--itemtableがなければ新しく作成
 CREATE TABLE IF NOT EXISTS itemtable(
id INT AUTO_INCREMENT,
name VARCHAR(50) NOT NULL,
price INT NOT NULL,
category VARCHAR(50) NOT NULL,
num INT NOT NULL,
PRIMARY KEY(id)
);

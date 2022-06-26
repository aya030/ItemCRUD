--itemtableがあれば削除
DROP TABLE IF EXISTS itemtabletest;

--itemtableがなければ新しく作成
 CREATE TABLE IF NOT EXISTS itemtabletest(
id INT AUTO_INCREMENT,
name VARCHAR(50) NOT NULL,
price INT NOT NULL,
category VARCHAR(50) NOT NULL,
num INT NOT NULL,
PRIMARY KEY(id)
);

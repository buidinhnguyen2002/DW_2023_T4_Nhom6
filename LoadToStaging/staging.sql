create database staging;
use staging;

create table staging.StagingNews(id int auto_increment not null primary key,
						 title text,
                         image text,
                         category text,
						 desciption text,
                         content text,
                         author text,
                         tags text,
                         create_at text,
                         update_at text,
                         create_by text,
                         update_by text);
                         select * from staging.StagingNews;  
                         insert into staging.StagingNews(title, image, category, desciption, content, author, tags, create_at, update_at, create_by, update_by) 
                         VALUES ("a", "a.png", "Thể thao", "Hay", "Hay, kịch tính", "VTV", "Bóng đá", "2023-12-15", "2023-12-15", "admin", "admin");
                         insert into staging.StagingNews(title, image, category, desciption, content, author, tags, create_at, update_at, create_by, update_by) 
                         VALUES ("b", "a.png", "Thể thao", "Hay", "Vui, kịch tính", "VTV", "Bóng đá", "2023-12-15", "2023-12-15", "admin", "admin");
drop table staging.StagingNews;
drop database staging;
select *from staging.News;
select * from staging.Category;
drop table News;
drop table Category;
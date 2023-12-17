create database data_mart;
use data_mart;
create table present_news_articles(id int primary key,
								 title text,
                                 description text,
                                 content text,
                                 image text,
                                 category varchar(100),
                                 authorName varchar(100),
                                 dateNews datetime,
                                 tags text);
drop table PresentNewsArticles;								                                 
insert into present_news_articles values('1', 'Chủ tịch Trung Quốc Tập Cận Bình rời Hà Nội', 'Hà Nội Tổng bí thư, Chủ tịch Trung Quốc Tập Cận Bình
 và phu nhân lên chuyên cơ rời sân bay Nội Bài, kết thúc chuyến thăm chính thức cấp Nhà nước đến Việt Nam, chiều 13/12.',
 'Hơn 17h, ông Tập Cận Bình và phu nhân Bành Lệ Viên đến sân bay Nội Bài sau khi cùng Tổng bí thư Nguyễn Phú Trọng và phu nhân giao lưu với nhân sĩ hữu nghị, thế hệ trẻ Việt Nam - Trung Quốc. Chủ tịch Quốc hội Vương Đình Huệ tiễn ông Tập và phu nhân tại sân bay. Khi đến chân cầu thang chuyên cơ, ông Tập dừng lại vài phút trò chuyện, bắt tay những người đến tiễn. Tổng bí thư, Chủ tịch Trung Quốc Tập Cận Bình và phu nhân Bành Lệ Viên đến Hà Nội trưa 12/12. Đây là lần thứ ba ông Tập thăm Việt Nam trên cương vị Tổng bí thư, Chủ tịch Trung Quốc, sau năm 2015 và 2017. Lễ đón cấp Nhà nước Tổng Bí thư, Chủ tịch Trung Quốc Tập Cận Bình diễn ra chiều cùng ngày tại Phủ Chủ tịch do Tổng bí thư Nguyễn Phú Trọng chủ trì, với 21 phát đại bác chào mừng.', 'https://i1-vnexpress.vnecdn.net/2023/12/14/1683992295748-01-jpeg-4440-1702523552.jpg?w=240&h=144&q=100&dpr=1&fit=crop&s=pGBSWNF-3zzOZ0sU9jJYvw',
 'Thời sự', 'Nga Nguyễn', '2023-12-17', 'Chính trị, Dân sinh, Lao động-việc làm');
 
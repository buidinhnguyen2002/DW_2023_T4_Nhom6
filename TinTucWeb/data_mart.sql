create database data_mart;
use data_mart;
create table PresentNewsArticles(id varchar(50) primary key,
								 title varchar(100),
                                 description text,
                                 content text,
                                 image text,
                                 category varchar(100),
                                 authorName varchar(100),
                                 dateNews datetime,
                                 tags text);
drop table PresentNewsArticles;								                                 
insert into PresentNewsArticles values('N1', 'Filip Nguyễn chờ AFC xét duyệt để dự Asian Cup', 'VFF đăng ký Filip Nguyễn trong danh sách sơ bộ dự Asian Cup 2023, nhưng cầu thủ mới hoàn tất thủ tục để có quốc tịch Việt Nam phải chờ Liên đoàn Bóng đá châu Á xét duyệt.', 
'VFF đăng ký Filip Nguyễn trong danh sách sơ bộ dự Asian Cup 2023, nhưng cầu thủ mới hoàn tất thủ tục để có quốc tịch Việt Nam phải chờ Liên đoàn Bóng đá châu Á xét duyệt.
\nLiên đoàn Bóng đá Việt Nam hôm nay 12/12 gửi danh sách sơ bộ dự Asian Cup 2023 theo quy định, gồm 50 cầu thủ. Vào buổi họp kỹ thuật trước trận đấu đầu tiên của giải, đội tuyển sẽ chốt quân số 23 người.
\nFilip Nguyễn có tên trong danh sách sơ bộ. Tuy nhiên, anh là trường hợp đặc biệt, phải chờ AFC xét duyệt xem đủ điều kiện khoác áo tuyển Việt Nam hay không. Nguyên nhân là anh từng được triệu tập lên đội tuyển Czech, mới có quốc tịch Việt Nam hôm 6/12.
\nVFF cho biết Filip Nguyễn từng khoác áo tuyển Czech nhưng chưa ra sân thi đấu nên đủ điều kiện để khoác áo tuyển Việt Nam. Việc AFC xét duyệt chỉ là vấn đề thủ tục.
\nFilip Nguyễn sinh ngày 14/9/1992 có mẹ là người Czech và bố người Việt Nam. Anh xuất thân từ học viện rồi lên chơi cho đội một Sparta Prague, trước khi chuyển sang Bydzov, Vlasim, Slova Liberec và Slovacko. Filip Nguyễn từng được Hiệp hội các giải Bóng đá Nhà nghề Czech bình chọn làm thủ môn hay nhất giải VĐQG mùa 2018-2019.
\nFilip Nguyễn nhiều lần bày tỏ nguyện vọng khoác áo tuyển Việt Nam khi còn thi đấu cho các CLB của Czech. Tuy nhiên, anh gặp trục trặc giấy tờ do không có đủ thời gian sinh sống ở Việt Nam. Vấn đề được giải quyết khi thủ môn sinh năm 1992 đầu quân cho CLB Công an Hà Nội hôm 30/6.
\nAsian Cup 2023 lùi tổ chức sang năm 2024 do Covid-19. Giải diễn ra từ 12/1 tới 10/2 tại Qatar. Việt Nam nằm ở bảng D, lần lượt gặp Nhật Bản (14/1), Indonesia (19/1) và Iraq (24/1). Thầy trò HLV Troussier sẽ di chuyển đi Qatar từ ngày 5/1 để làm quen thời tiết.',
 'https://i1-thethao.vnecdn.net/2023/12/12/THOA3759-2660-1702389197.jpg?w=0&h=0&q=100&dpr=2&fit=crop&s=D6Z5gAfIUKVRsNZB3aKLTQ', 'Thể thao',
 'Lâm Thỏa', '2023-12-13', 'Đội tuyển bóng đá Việt Nam');
 insert into PresentNewsArticles values('N2', 'Nhà sáng tạo nội dung học IT trực tuyến để tăng lợi thế nghề nghiệp', 'Là Content Creator mảng video Youtube, Nguyễn Anh Minh (sinh năm 1999) học IT trực tuyến tại FUNiX để trang bị kiến thức công nghệ, mở rộng cơ hội nghề nghiệp.', 
'Là Content Creator mảng video Youtube, Nguyễn Anh Minh (sinh năm 1999) học IT trực tuyến tại FUNiX để trang bị kiến thức công nghệ, mở rộng cơ hội nghề nghiệp.
 \nTốt nghiệp ngành Truyền thông, Anh Minh đã có nhiều năm làm Content Creator (nhà sáng tạo nội dung) mảng Youtube. Công việc này theo Minh khá thú vị, nhưng vất vả và đôi khi hơi bấp bênh. Nhận thấy sự phát triển của công nghệ ngày càng mạnh mẽ, Minh quyết định trang bị thêm kỹ năng này, tìm kiếm cơ hội tiềm năng hơn.
 \nMinh nhận thấy ngành IT có sức ảnh hưởng đến mọi lĩnh vực của đời sống, liên tục là ngành được người trẻ ưu tiên khi chọn trường, chọn ngành... Cậu cũng có xu hướng thích mày mò, nghiên cứu về công nghệ, các xu hướng mới, các tip làm việc, nên tin rằng bản thân có thể theo đuổi học tập để phát triển bản thân, thậm chí chuyển nghề nếu tìm được khóa học phù hợp.
 \nBiết đến FUNiX, chàng trai sinh năm 1999 nhanh chóng bị hấp dẫn và quyết định học tập ở đây để nâng cao kỹ năng, tìm kiếm thêm hướng phát triển trong tương lai gần. Hơn nữa, cậu bạn 9X cho rằng, lựa chọn theo học trực tuyến công nghệ tại đây sẽ giúp cậu rút ngắn thời gian học tập.
 \n"Mình chọn FUNiX vì chương trình học online cho phép mình chủ động thời gian. Ngoài ra, mình sẽ được các mentor, hannah hỗ trợ nhiệt tình, đồng hành với mình trong việc học, nên có thêm cơ hội phát triển, từ đó cải thiện thu nhập cũng như tìm thấy những công việc mới triển vọng hơn", Minh chia sẻ thêm.
 \nVì vừa đi làm, vừa học nên Minh chỉ dành được khoảng 1- 2 tiếng mỗi ngày cho việc học, thường là vào buổi tối trước khi đi ngủ. Nhiều hôm mệt hoặc bận việc, Minh buộc phải nghỉ thì sẽ cố gắng học bù ngay vào ngày hôm sau. Vì chưa có nền tảng, Minh thiếu kiến thức, bỡ ngỡ với việc học. Áp lực công việc hoặc kiến thức khó nhằn đôi khi cũng khiến Minh chán nản. 
 May nhờ có sự ủng hộ, khích lệ của gia đình và nhất là sự đồng hành của mentor và hannah nên Minh được tiếp thêm động lực để tiếp tục học.
 \nNhập học tháng 7, Minh hiện trang bị cho mình kiến thức để xây dựng website cá nhân. Đến giờ cảm nhận của cậu là hoàn toàn hài lòng với lựa chọn của mình. Cậu đánh giá Khóa học tại FUNiX hấp dẫn, hữu ích ở nhiều phương diện.
 \nTheo đó, Minh không chỉ được học những kiến thức công nghệ bài bản cho dân non-IT mà còn được đội ngũ mentor (cố vấn chuyên môn), hannah (cán bộ hỗ trợ) chăm sóc tận tình. Cùng với đó là những cơ hội học tập đa dạng khác, như các lớp học tiếng Anh miễn phí đủ trình độ, các buổi gặp gỡ trao đổi với đàn anh hoặc với những người có tầm ảnh hưởng trong ngành.
 \nNgoài trang bị kiến thức công nghệ, học viên như Minh có nhiều dịp học hỏi, rèn luyện các kỹ năng quan trọng thông qua các hoạt động ngoại khóa, hoạt động học tập phong phú như xDay, xCoffee, xTalk, xCoffee; câu lạc bộ tiếng Anh, tiếng Nhật; câu lạc bộ tranh biện, câu lạc bộ bóng đá, chạy...
 \nĐể một người chưa từng có background IT học tốt khóa học FUNiX, Minh cho rằng chăm chỉ, ham học hỏi và quyết tâm cao là những nhân tố quan trọng hàng đầu. Tin tưởng mình sẽ tìm được những cơ hội phát triển bản thân, hiện Minh nỗ lực hết sức để hoàn thành khóa học đúng tiến độ.
 \nHannah Thu Hiền - người đồng hành cùng Anh Minh nhận xét cậu là học viên chăm chỉ, giàu nghị lực. Vừa làm, vừa học rất vất vả nhưng Minh luôn cố gắng đảm bảo tiến độ, deadline. Không chỉ chuyên chú học, cậu còn chịu khó tham gia các hoạt động ngoại khóa, các lớp học tiếng Anh để giao lưu, học hỏi từ bạn bè đồng môn.
 \n"Với sự năng động, nhạy bén sẵn có của dân truyền thông, cùng kiến thức IT học ở FUNiX, Minh sẽ sớm tìm được những cơ hội mới, công việc như ý với mức lương - đãi ngộ xứng đáng những nỗ lực mà bạn đã bỏ ra trong thời gian qua", Hannah Thu Hiền nhận định.', 
 'https://i1-vnexpress.vnecdn.net/2023/09/29/c2-1365-1695969573.jpg?w=1020&h=0&q=100&dpr=1&fit=crop&s=B4pzuf52uFynzbrA2qdPoA', 'Giáo dục', 'Vân Anh', '2023-12-13', 'IT');
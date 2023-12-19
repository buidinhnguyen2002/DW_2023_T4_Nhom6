create database if not exists control;
use control;
create table control.data_files(id SERIAL,
								df_config_id bigint,
                                name varchar(1000),
                                row_count bigint,
                                status varchar(255),
                                data_range_from date,
                                data_range_to date,
                                note text,
                                created_at date,
                                updated_at date,
                                created_by varchar(255),
                                updated_by varchar(255));
CREATE TABLE control.data_file_configs (
  `id` int(11) NOT NULL,
  `name` varchar(1000) NOT NULL,
  `description` text NOT NULL,
  `source_path` varchar(1000) DEFAULT NULL,
  `location` varchar(1000) DEFAULT NULL,
  `format` varchar(255) NOT NULL,
  `columns` text NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `update_at` datetime DEFAULT NULL,
  `create_by` varchar(1000) NOT NULL DEFAULT 'root',
  `update_by` varchar(1000) DEFAULT NULL
); 
select * from control.data_file_configs;

SELECT location FROM control.data_file_configs WHERE create_at = '2023-12-14 08:50:55' ORDER BY create_at DESC LIMIT 1;

select * from control.data_file_configs;
select * from control.data_files;

create table control.Logs (id int not null auto_increment primary key,
						   event varchar(100),
                           status varchar(255),
                           note varchar(1000),
						   create_at date);
                           select * from control.Logs;
                          drop table control.Logs;		
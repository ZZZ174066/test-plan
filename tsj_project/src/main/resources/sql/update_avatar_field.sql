-- 更新用户表的avatar字段以支持Base64图片数据
-- 原来的VARCHAR(255)长度不足以存储Base64编码的图片
-- 修改为TEXT以支持大容量的图片数据

ALTER TABLE sys_user MODIFY COLUMN avatar TEXT COMMENT '用户头像Base64数据或URL';

-- 验证字段更新
-- SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH 
-- FROM INFORMATION_SCHEMA.COLUMNS 
-- WHERE TABLE_NAME = 'sys_user' AND COLUMN_NAME = 'avatar';

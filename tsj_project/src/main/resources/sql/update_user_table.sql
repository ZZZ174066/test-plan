-- 为用户表添加新字段：头像、在线状态和邮箱
ALTER TABLE sys_user ADD COLUMN avatar VARCHAR(255) DEFAULT '/src/assets/user/user.jpg' COMMENT '用户头像URL';
ALTER TABLE sys_user ADD COLUMN online_status TINYINT DEFAULT 0 COMMENT '在线状态 0-离线 1-在线';
ALTER TABLE sys_user ADD COLUMN email VARCHAR(100) DEFAULT NULL COMMENT '用户邮箱';

-- 更新已有数据，设置默认值
UPDATE sys_user SET online_status = 0 WHERE online_status IS NULL; 
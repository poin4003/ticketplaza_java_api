-- 1. ticket table
CREATE TABLE IF NOT EXISTS `ticketplaza`.`ticket` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `name` VARCHAR(50) NOT NULL COMMENT 'ticket_name',
    `desc` TEXT COMMENT 'ticket description',
    `start_time` DATETIME NOT NULL COMMENT 'ticket sale start time',
    `end_time` DATETIME NOT NULL COMMENT 'ticket sale end time',
    `status` INT (11) NOT NULL DEFAULT 0 COMMENT 'ticket sale activity status',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Last update time',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
    PRIMARY KEY (`id`),
    KEY `idx_end_time` (`end_time`), -- Very high query runtime
    KEY `idx_start_time`  (`start_time`), -- Very high query runtime
    KEY `idx_status` (`status`) -- Very high query runtime
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- 2. ticket detail (item) table
CREATE TABLE IF NOT EXISTS `ticketplaza`.`ticket_item` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `name` VARCHAR(50) NOT NULL COMMENT 'Ticket title',
    `description` TEXT COMMENT 'Ticket description',
    `stock_initial` INT(11) NOT NULL DEFAULT 0 COMMENT 'Initial stock quantity (e.g., 1000 tickets)',
    `stock_available` INT(11) NOT NULL DEFAULT 0 COMMENT 'Current available stock (e.g, 900 tickets)',
    `is_stock_prepared` BOOLEAN NOT NULL DEFAULT 0 COMMENT 'Indicates if stock is pre-warmed (0/1)',
    `price_original` BIGINT(20) NOT NULL COMMENT 'Original ticket price',
    `price_flash` BIGINT(20) NOT NULL COMMENT 'Discounted price during flash sale',
    `sale_start_time` DATETIME NOT NULL COMMENT 'Flash sale start time',
    `sale_end_time` DATETIME NOT NULL COMMENT 'Flash sale end time',
    `status` INT(11) NOT NULL DEFAULT 0 COMMENT 'Ticket status (e.g., active/inactive)',
    `activity_id` BIGINT (20) NOT NULL COMMENT 'ID of associated activity',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of the last update',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
    PRIMARY KEY (`id`),
    KEY `idx_end_time` (`sale_end_time`),
    KEY `idx_start_time` (`sale_start_time`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- INSERT MOCK DATA
-- Insert data into `ticket` table
INSERT INTO `ticketplaza`.`ticket` (`name`, `desc`, `start_time`, `end_time`)
VALUES
    ('Đợt mở bán vé ngày 12/12', 'Sự kiện mở bán vé đặc biệt cho ngày 12/12', '2025-12-12 00:00:00', '2025-12-12 23:59:59'),
    ('Đợt mở bán vé ngày 01/01', 'Sự kiện mở bán vé đặc biệt cho ngày đầu năm mới 12/12', '2025-12-12 00:00:00', '2025-12-12 23:59:59');

-- Insert data into `ticket_item` table  corresponding to each event in `ticket` table
-- Insert 2 ticket_item per ticket
INSERT INTO `ticketplaza`.`ticket_item` (
    `name`, `description`, `stock_initial`, `stock_available`, `is_stock_prepared`,
    `price_original`, `price_flash`, `sale_start_time`, `sale_end_time`,
    `status`, `activity_id`
) VALUES
('Vé Phổ Thông', 'Vé vào cửa phổ thông cho sự kiện ngày 12/12', 1000, 1000, 1,
 500000, 400000, '2025-12-12 00:00:00', '2025-12-12 23:59:59', 1, 1),

('Vé VIP', 'Vé VIP cho sự kiện ngày 12/12 với đặc quyền bổ sung', 200, 200, 1,
 1500000, 1200000, '2025-12-12 00:00:00', '2025-12-12 23:59:59', 1, 1),

('Vé Phổ Thông', 'Vé vào cửa phổ thông cho sự kiện ngày 01/01', 1000, 1000, 1,
 500000, 400000, '2026-01-01 00:00:00', '2026-01-01 23:59:59', 1, 2),

('Vé VIP', 'Vé VIP cho sự kiện ngày 01/01 với đặc quyền bổ sung', 200, 200, 1,
 1500000, 1200000, '2026-01-01 00:00:00', '2026-01-01 23:59:59', 1, 2);

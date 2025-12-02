-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 02, 2025 at 02:49 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `protocol-zero`
--

-- --------------------------------------------------------

--
-- Table structure for table `bomb`
--

CREATE TABLE `bomb` (
  `bomb_id` int(11) NOT NULL,
  `serial_code` varchar(20) NOT NULL,
  `time_limit` int(11) NOT NULL,
  `max_strikes` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bomb`
--

INSERT INTO `bomb` (`bomb_id`, `serial_code`, `time_limit`, `max_strikes`) VALUES
(1, 'Z9E1', 300, 3),
(2, 'A2B5', 300, 3),
(3, 'A4O3', 300, 3),
(4, 'A3N0', 300, 3),
(5, 'P5Q2', 300, 3),
(6, 'AIO3', 300, 3);

-- --------------------------------------------------------

--
-- Table structure for table `bomb_button`
--

CREATE TABLE `bomb_button` (
  `id` int(11) NOT NULL,
  `bomb_id` int(11) NOT NULL,
  `button_color` varchar(20) NOT NULL,
  `button_label` varchar(50) NOT NULL,
  `solve_type` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bomb_button`
--

INSERT INTO `bomb_button` (`id`, `bomb_id`, `button_color`, `button_label`, `solve_type`) VALUES
(1, 1, 'RED', 'HOLD', 'TAP'),
(2, 2, 'YELLOW', 'PRESS', 'HOLD1'),
(3, 3, 'GREEN', 'HOLD', 'HOLD4'),
(4, 4, 'RED', 'PRESS', 'HOLD1'),
(5, 5, 'YELLOW', 'HOLD', 'HOLD4'),
(6, 6, 'GREEN', 'PRESS', 'TAP');

-- --------------------------------------------------------

--
-- Table structure for table `bomb_keypad`
--

CREATE TABLE `bomb_keypad` (
  `keypad_id` int(11) NOT NULL,
  `bomb_id` int(11) NOT NULL,
  `column_index` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bomb_keypad`
--

INSERT INTO `bomb_keypad` (`keypad_id`, `bomb_id`, `column_index`) VALUES
(1, 1, 4),
(2, 2, 3),
(3, 3, 2),
(4, 4, 3),
(5, 5, 4),
(6, 6, 1);

-- --------------------------------------------------------

--
-- Table structure for table `bomb_simon`
--

CREATE TABLE `bomb_simon` (
  `simon_id` int(11) NOT NULL,
  `bomb_id` int(11) NOT NULL,
  `start_length` tinyint(4) NOT NULL DEFAULT 1,
  `target_length` tinyint(4) NOT NULL DEFAULT 6
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bomb_simon`
--

INSERT INTO `bomb_simon` (`simon_id`, `bomb_id`, `start_length`, `target_length`) VALUES
(1, 1, 1, 6),
(2, 2, 1, 6),
(3, 3, 1, 6),
(4, 4, 1, 6),
(5, 5, 1, 6),
(6, 6, 1, 6);

-- --------------------------------------------------------

--
-- Table structure for table `bomb_wire`
--

CREATE TABLE `bomb_wire` (
  `id` int(11) NOT NULL,
  `bomb_id` int(11) NOT NULL,
  `wire_index` int(11) NOT NULL,
  `wire_color` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bomb_wire`
--

INSERT INTO `bomb_wire` (`id`, `bomb_id`, `wire_index`, `wire_color`) VALUES
(1, 1, 0, 'RED'),
(2, 1, 1, 'YELLOW'),
(3, 1, 2, 'GREEN'),
(4, 1, 3, 'BLUE'),
(5, 1, 4, 'PINK'),
(6, 1, 5, 'PURPLE'),
(7, 2, 0, 'RED'),
(8, 2, 1, 'YELLOW'),
(9, 2, 2, 'GREEN'),
(10, 2, 3, 'BLUE'),
(11, 2, 4, 'PINK'),
(12, 2, 5, 'PURPLE'),
(13, 3, 0, 'RED'),
(14, 3, 1, 'YELLOW'),
(15, 3, 2, 'GREEN'),
(16, 3, 3, 'BLUE'),
(17, 3, 4, 'PINK'),
(18, 3, 5, 'PURPLE'),
(19, 4, 0, 'RED'),
(20, 4, 1, 'YELLOW'),
(21, 4, 2, 'GREEN'),
(22, 4, 3, 'BLUE'),
(23, 4, 4, 'PINK'),
(24, 4, 5, 'PURPLE'),
(25, 5, 0, 'RED'),
(26, 5, 1, 'YELLOW'),
(27, 5, 2, 'GREEN'),
(28, 5, 3, 'BLUE'),
(29, 5, 4, 'PINK'),
(30, 5, 5, 'PURPLE'),
(31, 6, 0, 'RED'),
(32, 6, 1, 'YELLOW'),
(33, 6, 2, 'GREEN'),
(34, 6, 3, 'BLUE'),
(35, 6, 4, 'PINK'),
(36, 6, 5, 'PURPLE');

-- --------------------------------------------------------

--
-- Table structure for table `score`
--

CREATE TABLE `score` (
  `score_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `bomb_id` int(11) NOT NULL,
  `strike_left` int(11) NOT NULL,
  `time_left` int(11) NOT NULL,
  `game_score` enum('SS','A','B','C','F') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(12) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`) VALUES
(1, 'A', 'a');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bomb`
--
ALTER TABLE `bomb`
  ADD PRIMARY KEY (`bomb_id`),
  ADD UNIQUE KEY `serial_code` (`serial_code`);

--
-- Indexes for table `bomb_button`
--
ALTER TABLE `bomb_button`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bomb_id` (`bomb_id`);

--
-- Indexes for table `bomb_keypad`
--
ALTER TABLE `bomb_keypad`
  ADD PRIMARY KEY (`keypad_id`),
  ADD KEY `fk_keypad_bomb` (`bomb_id`);

--
-- Indexes for table `bomb_simon`
--
ALTER TABLE `bomb_simon`
  ADD PRIMARY KEY (`simon_id`),
  ADD KEY `fk_simon_bomb` (`bomb_id`);

--
-- Indexes for table `bomb_wire`
--
ALTER TABLE `bomb_wire`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bomb_id` (`bomb_id`);

--
-- Indexes for table `score`
--
ALTER TABLE `score`
  ADD PRIMARY KEY (`score_id`),
  ADD KEY `fk_score_user` (`user_id`),
  ADD KEY `fk_score_bomb` (`bomb_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `unique_username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bomb`
--
ALTER TABLE `bomb`
  MODIFY `bomb_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `bomb_button`
--
ALTER TABLE `bomb_button`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `bomb_keypad`
--
ALTER TABLE `bomb_keypad`
  MODIFY `keypad_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `bomb_simon`
--
ALTER TABLE `bomb_simon`
  MODIFY `simon_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `bomb_wire`
--
ALTER TABLE `bomb_wire`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `score`
--
ALTER TABLE `score`
  MODIFY `score_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bomb_button`
--
ALTER TABLE `bomb_button`
  ADD CONSTRAINT `bomb_button_ibfk_1` FOREIGN KEY (`bomb_id`) REFERENCES `bomb` (`bomb_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `bomb_keypad`
--
ALTER TABLE `bomb_keypad`
  ADD CONSTRAINT `fk_keypad_bomb` FOREIGN KEY (`bomb_id`) REFERENCES `bomb` (`bomb_id`) ON DELETE CASCADE;

--
-- Constraints for table `bomb_simon`
--
ALTER TABLE `bomb_simon`
  ADD CONSTRAINT `fk_simon_bomb` FOREIGN KEY (`bomb_id`) REFERENCES `bomb` (`bomb_id`) ON DELETE CASCADE;

--
-- Constraints for table `bomb_wire`
--
ALTER TABLE `bomb_wire`
  ADD CONSTRAINT `bomb_wire_ibfk_1` FOREIGN KEY (`bomb_id`) REFERENCES `bomb` (`bomb_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `score`
--
ALTER TABLE `score`
  ADD CONSTRAINT `fk_score_bomb` FOREIGN KEY (`bomb_id`) REFERENCES `bomb` (`bomb_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_score_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

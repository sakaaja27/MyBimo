-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 24, 2024 at 11:01 AM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mybimo`
--

-- --------------------------------------------------------

--
-- Table structure for table `general_quiz`
--

CREATE TABLE `general_quiz` (
  `id` int NOT NULL,
  `id_soalsubmateri` int NOT NULL,
  `created_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `materi`
--

CREATE TABLE `materi` (
  `id` int NOT NULL,
  `judul_materi` varchar(255) NOT NULL,
  `foto_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `status_materi` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `materi`
--

INSERT INTO `materi` (`id`, `judul_materi`, `foto_icon`, `status_materi`, `created_at`) VALUES
(1, 'vocabulary', '', 0, '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `pembayaran`
--

CREATE TABLE `pembayaran` (
  `id` int NOT NULL,
  `nama_pembayaran` varchar(255) NOT NULL,
  `harga` varchar(255) NOT NULL,
  `nomor_bank` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pembayaran`
--

INSERT INTO `pembayaran` (`id`, `nama_pembayaran`, `harga`, `nomor_bank`, `created_at`) VALUES
(1, 'Monthly Subscription', '130000', '123456789', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `soal_submateri`
--

CREATE TABLE `soal_submateri` (
  `id` int NOT NULL,
  `id_submateri` int NOT NULL,
  `nama_soal` varchar(255) NOT NULL,
  `jawaban_soal` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sub_materi`
--

CREATE TABLE `sub_materi` (
  `id` int NOT NULL,
  `id_materi` int NOT NULL,
  `nama_sub` varchar(255) NOT NULL,
  `upload_file` text NOT NULL,
  `created_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sub_materi`
--

INSERT INTO `sub_materi` (`id`, `id_materi`, `nama_sub`, `upload_file`, `created_at`) VALUES
(1, 1, 'idiom', '\"E:\\materi\\speech.pdf\"', '2024-10-24 03:54:28');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id` int NOT NULL,
  `id_pembayaran` int NOT NULL,
  `id_user` int NOT NULL,
  `id_materi` int NOT NULL,
  `upload_bukti` blob NOT NULL,
  `status` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone` varchar(255) NOT NULL DEFAULT '0',
  `role` int NOT NULL DEFAULT '0',
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `phone`, `role`, `password`) VALUES
(5, 'sakabramasta ', 'sakabramasta@gmail.com', '271024', 0, 'saka'),
(6, 'saka', 'admin@gmail.com', '085760199917', 0, '3cce4d2b949277a3abe98ed03b60bb14'),
(7, 'testes', 'testingoke@email.com', '085760199917', 0, '3cce4d2b949277a3abe98ed03b60bb14'),
(8, 'testing mas', 'testinggg@gmail.com', '085760199917', 0, '1bbd886460827015e5d605ed44252251'),
(9, 'sakakarna', 'zhapiaa@gmail.com', 'sakabramasta', 0, '3cce4d2b949277a3abe98ed03b60bb14'),
(11, 'dara', 'dara@gmail.com', 'dara8989', 0, 'fd07f53c4427fd6a6f3edba9fbdf6193');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `general_quiz`
--
ALTER TABLE `general_quiz`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `materi`
--
ALTER TABLE `materi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `soal_submateri`
--
ALTER TABLE `soal_submateri`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sub_materi`
--
ALTER TABLE `sub_materi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `general_quiz`
--
ALTER TABLE `general_quiz`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `materi`
--
ALTER TABLE `materi`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `pembayaran`
--
ALTER TABLE `pembayaran`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `soal_submateri`
--
ALTER TABLE `soal_submateri`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sub_materi`
--
ALTER TABLE `sub_materi`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

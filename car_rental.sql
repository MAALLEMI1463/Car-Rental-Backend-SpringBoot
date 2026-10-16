-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 19 sep. 2026 à 13:28
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `car_rental`
--

-- --------------------------------------------------------

--
-- Structure de la table `car`
--

CREATE TABLE `car` (
  `id` bigint(20) NOT NULL,
  `brand` varchar(255) NOT NULL,
  `car_status` enum('AVAILABLE','UNAVAILABLE') NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `image_public_id` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `model` varchar(255) NOT NULL,
  `price_per_day` decimal(10,2) NOT NULL,
  `registration_number` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `car`
--

INSERT INTO `car` (`id`, `brand`, `car_status`, `created_at`, `description`, `image_public_id`, `image_url`, `is_deleted`, `model`, `price_per_day`, `registration_number`) VALUES
(1, 'toyota', 'UNAVAILABLE', '2026-09-02 13:29:47.000000', 'yaris is good car', 'g24hufumnx0chi6mj40z', 'https://res.cloudinary.com/bimxdlug/image/upload/v1788355884/g24hufumnx0chi6mj40z.jpg', b'0', 'corola', 7000.00, '12345'),
(2, 'toyota', 'AVAILABLE', '2026-09-02 13:32:58.000000', 'is good car', 'kotoiqcw8n6u46554mpr', 'https://res.cloudinary.com/bimxdlug/image/upload/v1788355996/kotoiqcw8n6u46554mpr.jpg', b'0', 'yaris', 10000.00, '17586'),
(3, 'BMW', 'AVAILABLE', '2026-09-03 12:24:06.000000', 'is good car', 'sldcjulebl3srlcdgxvi', 'https://res.cloudinary.com/bimxdlug/image/upload/v1788438244/sldcjulebl3srlcdgxvi.jpg', b'0', 'BMW 350', 10000.00, '12378'),
(4, 'Kia', 'AVAILABLE', '2026-09-16 18:08:32.000000', 'Picanto is fast car', 'emlgm8vpyfzhengzlhdp', 'https://res.cloudinary.com/bimxdlug/image/upload/v1789582111/emlgm8vpyfzhengzlhdp.jpg', b'0', 'Picanto', 10000.00, '12987'),
(5, 'Porsh', 'AVAILABLE', '2026-09-16 18:09:27.000000', 'Porsh is allemagne car', 'qpkhlmfbfukwwdckbppr', 'https://res.cloudinary.com/bimxdlug/image/upload/v1789582167/qpkhlmfbfukwwdckbppr.jpg', b'0', 'GTB5', 20000.00, '18976'),
(6, 'Livan', 'AVAILABLE', '2026-09-16 18:10:30.000000', 'livan is good car', 'a6cu6ov367glz8rknrka', 'https://res.cloudinary.com/bimxdlug/image/upload/v1789582230/a6cu6ov367glz8rknrka.jpg', b'0', 'GX3 Pro', 15000.00, '83976'),
(7, 'Tigo', 'AVAILABLE', '2026-09-16 18:13:34.000000', 'Tigo is good car', 'mqwjduif28p8la9ktxgt', 'https://res.cloudinary.com/bimxdlug/image/upload/v1789582414/mqwjduif28p8la9ktxgt.jpg', b'0', 'Arrigo 5', 15000.00, '28474'),
(9, 'Golf', 'AVAILABLE', '2026-09-19 10:26:49.000000', 'is good car', 'xifxeiuuigr9cwoofknc', 'https://res.cloudinary.com/bimxdlug/image/upload/v1789813608/xifxeiuuigr9cwoofknc.jpg', b'0', 'C5', 10000.00, '18640');

-- --------------------------------------------------------

--
-- Structure de la table `cars`
--

CREATE TABLE `cars` (
  `id` bigint(20) NOT NULL,
  `brand` varchar(255) NOT NULL,
  `car_status` enum('AVAILABLE','UNAVAILABLE') NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` text DEFAULT NULL,
  `image_public_id` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `model` varchar(255) NOT NULL,
  `prive_per_day` decimal(10,2) NOT NULL,
  `regiatration_number` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

CREATE TABLE `reservation` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `end_date` datetime(6) NOT NULL,
  `price_per_day` decimal(10,2) NOT NULL,
  `reservation_status` enum('ACTIVE','COMPLETED','CONFIRMED','PENDING') NOT NULL,
  `start_date` datetime(6) NOT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `car_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reservation`
--

INSERT INTO `reservation` (`id`, `created_at`, `end_date`, `price_per_day`, `reservation_status`, `start_date`, `total_price`, `car_id`, `user_id`) VALUES
(2, '2026-08-08 10:59:43.000000', '2026-08-13 10:59:00.000000', 10000.00, 'COMPLETED', '2026-08-11 10:59:00.000000', 20000.00, 2, 2),
(4, '2026-07-08 11:32:05.000000', '2026-07-13 11:31:00.000000', 10000.00, 'ACTIVE', '2026-07-10 11:31:00.000000', 30000.00, 2, 3),
(8, '2026-09-16 17:46:23.000000', '2026-09-22 17:43:00.000000', 10000.00, 'PENDING', '2026-09-20 17:43:00.000000', 20000.00, 2, 3),
(9, '2026-09-17 18:59:07.000000', '2026-09-26 18:58:00.000000', 10000.00, 'PENDING', '2026-09-25 18:58:00.000000', 10000.00, 2, 3),
(10, '2026-09-19 10:24:29.000000', '2026-09-21 12:26:00.000000', 15000.00, 'CONFIRMED', '2026-09-20 12:26:00.000000', 15000.00, 7, 2),
(11, '2026-09-19 10:28:34.000000', '2026-09-21 12:30:00.000000', 10000.00, 'ACTIVE', '2026-09-20 12:30:00.000000', 10000.00, 4, 3),
(12, '2026-09-19 10:55:19.000000', '2026-09-22 02:30:00.000000', 10000.00, 'PENDING', '2026-09-20 02:30:00.000000', 20000.00, 3, 2),
(13, '2026-09-19 11:04:29.000000', '2026-09-21 02:30:00.000000', 15000.00, 'PENDING', '2026-09-20 02:30:00.000000', 15000.00, 6, 2);

-- --------------------------------------------------------

--
-- Structure de la table `reservations`
--

CREATE TABLE `reservations` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `end_date` date NOT NULL,
  `prive_per_day` decimal(10,2) NOT NULL,
  `reservation_status` enum('ACTIVE','COMPLETED','CONFIRMED','PENDING') NOT NULL,
  `start_date` date NOT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `car_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CLIENT') NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `created_at`, `password`, `phone`, `role`, `username`) VALUES
(1, '2026-08-30 22:53:52.000000', '$2a$10$xqmDVdFbZIh9RWhySvqPCO4iXr0Tc.nsn3AlLv2VTk7er8/U0tNi2', NULL, 'ADMIN', 'admin'),
(2, '2026-08-30 23:06:16.000000', '$2a$10$h5/B.zYPv/L.GXbBEVYk2eIO7dj.7bY74IEo0lhizKqunvPnbudi6', '0550833507', 'CLIENT', 'madjid'),
(3, '2026-08-30 23:07:08.000000', '$2a$10$2X9xQ77.2LPDuifbOFEE8u6atlPo.ffJZ.ncAhM2zb15b0gC6AZCm', '0550833507', 'CLIENT', 'chakib'),
(4, '2026-08-31 13:56:47.000000', '$2a$10$mOzTDvuIUDrlyY2arisLVOGNxXLa2MWBaP6T4b987Lkmk9yRhuLmy', '0550383507', 'CLIENT', 'riadh'),
(5, '2026-09-14 19:42:52.000000', '$2a$10$MLpkFN6hN1WHy1RcLw/Wke4LO6iyZUn081.0lavhbfkOsUGirtj.y', '0679395796', 'CLIENT', 'mourad');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CLIENT') NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `car`
--
ALTER TABLE `car`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK41gx8ie1hc8w4eylj60io4pvy` (`registration_number`);

--
-- Index pour la table `cars`
--
ALTER TABLE `cars`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKgkmbspv7rljixxoxo1af80lpp` (`car_id`),
  ADD KEY `FKm4oimk0l1757o9pwavorj6ljg` (`user_id`);

--
-- Index pour la table `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_reservation_car` (`car_id`),
  ADD KEY `fk_reservation_user` (`user_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `car`
--
ALTER TABLE `car`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `cars`
--
ALTER TABLE `cars`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT pour la table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `FKgkmbspv7rljixxoxo1af80lpp` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`),
  ADD CONSTRAINT `FKm4oimk0l1757o9pwavorj6ljg` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `fk_reservation_car` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`),
  ADD CONSTRAINT `fk_reservation_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

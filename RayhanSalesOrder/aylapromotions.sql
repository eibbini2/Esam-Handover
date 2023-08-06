-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 08, 2021 at 01:07 PM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 7.3.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `aylapromotions`
--

-- --------------------------------------------------------

--
-- Table structure for table `abs_promotion_days`
--

CREATE TABLE IF not exists `abs_promotion_days` (
  `PromoID` int(11) NOT NULL,
  `Sunday` tinyint(1) NOT NULL,
  `Monday` tinyint(1) NOT NULL,
  `Tuesday` tinyint(1) NOT NULL,
  `Wednesday` tinyint(1) NOT NULL,
  `Thursday` tinyint(1) NOT NULL,
  `Friday` tinyint(1) NOT NULL,
  `Saturday` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `abs_promo_buy_get`
--

CREATE TABLE IF not exists `abs_promo_buy_get` (
  `PromoID` int(11) NOT NULL,
  `Value` double DEFAULT NULL,
  `Category` varchar(3) CHARACTER SET utf8 DEFAULT NULL,
  `Type` varchar(5) CHARACTER SET utf8 DEFAULT NULL,
  `RequestQty` double DEFAULT NULL,
  `ReceiveQty` double DEFAULT NULL,
  `Active` varchar(1) CHARACTER SET utf8 DEFAULT NULL,
  `ValidFrom` datetime DEFAULT NULL,
  `ValidTo` datetime DEFAULT NULL,
  `ExcludePOS` longtext DEFAULT NULL,
  `CreatedAt` datetime DEFAULT NULL,
  `Days` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `abs_promo_buy_get`
--

INSERT INTO `abs_promo_buy_get` (`PromoID`, `Value`, `Category`, `Type`, `RequestQty`, `ReceiveQty`, `Active`, `ValidFrom`, `ValidTo`, `ExcludePOS`, `CreatedAt`, `Days`) VALUES
(34, 10, 'C', 'P', 2, 1, 'Y', '2021-09-01 00:00:00.000', '2021-09-30 00:00:00.000', '', '2021-09-26 13:55:30', '0,1,2,3,4,5,6');

-- --------------------------------------------------------

--
-- Table structure for table `abs_promo_detail`
--

CREATE TABLE  IF not exists `abs_promo_detail` (
  `PromoID` int(11) NOT NULL,
  `Code` varchar(100) DEFAULT NULL,
  `Type` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `abs_promo_detail_history`
--

CREATE TABLE  IF not exists `abs_promo_detail_history` (
  `PromoID` int(11) NOT NULL,
  `Code` varchar(100) DEFAULT NULL,
  `DeletedAt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `abs_promo_get_percent`
--

CREATE TABLE IF not exists `abs_promo_get_percent` (
  `PromoID` int(11) NOT NULL,
  `Value` double DEFAULT NULL,
  `Category` varchar(3) CHARACTER SET utf8 DEFAULT NULL,
  `Type` varchar(5) DEFAULT NULL,
  `Active` varchar(1) CHARACTER SET utf8 DEFAULT NULL,
  `ValidFrom` datetime DEFAULT NULL,
  `ValidTo` datetime DEFAULT NULL,
  `ExcludePOS` longtext DEFAULT NULL,
  `CreatedAt` datetime DEFAULT NULL,
  `Days` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `abs_promo_history`
--

CREATE TABLE IF not exists `abs_promo_history` (
  `PromoID` int(11) NOT NULL,
  `Store` varchar(200) DEFAULT NULL,
  `StoreExc` varchar(200) DEFAULT NULL,
  `Brand` varchar(400) DEFAULT NULL,
  `BrandExc` varchar(400) DEFAULT NULL,
  `Season` varchar(100) DEFAULT NULL,
  `SeasonExc` varchar(100) DEFAULT NULL,
  `Size` varchar(400) DEFAULT NULL,
  `SizeExc` varchar(400) DEFAULT NULL,
  `Color` varchar(400) DEFAULT NULL,
  `ColorExc` varchar(400) DEFAULT NULL,
  `Item` longtext DEFAULT NULL,
  `ItemExc` longtext DEFAULT NULL,
  `ItemGroup` varchar(400) DEFAULT NULL,
  `ItemGroupExc` varchar(400) DEFAULT NULL,
  `Customer` longtext DEFAULT NULL,
  `CustomerExc` longtext DEFAULT NULL,
  `CustomerGroup` varchar(400) DEFAULT NULL,
  `CustomerGroupExc` varchar(400) DEFAULT NULL,
  `Family` varchar(400) DEFAULT NULL,
  `FamilyExc` varchar(400) DEFAULT NULL,
  `SubFamily1` varchar(400) DEFAULT NULL,
  `SubFamilyExc1` varchar(400) DEFAULT NULL,
  `SubFamily2` varchar(400) DEFAULT NULL,
  `SubFamilyExc2` varchar(400) DEFAULT NULL,
  `SubFamily3` varchar(400) DEFAULT NULL,
  `SubFamilyExc3` varchar(400) DEFAULT NULL,
  `SubFamily4` varchar(400) DEFAULT NULL,
  `SubFamilyExc4` varchar(400) DEFAULT NULL,
  `SubFamily5` varchar(400) DEFAULT NULL,
  `SubFamilyExc5` varchar(400) DEFAULT NULL,
  `SubFamily6` varchar(400) DEFAULT NULL,
  `SubFamilyExc6` varchar(400) DEFAULT NULL,
  `SubFamily7` varchar(400) DEFAULT NULL,
  `SubFamilyExc7` varchar(400) DEFAULT NULL,
  `SubFamily8` varchar(400) DEFAULT NULL,
  `SubFamilyExc8` varchar(400) DEFAULT NULL,
  `SubFamily9` varchar(400) DEFAULT NULL,
  `SubFamilyExc9` varchar(400) DEFAULT NULL,
  `SubFamily10` varchar(400) DEFAULT NULL,
  `SubFamilyExc10` varchar(400) DEFAULT NULL,
  `UserSign` int(11) DEFAULT NULL,
  `CreateDateTime` datetime DEFAULT NULL,
  `Dept` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `DeptExc` varchar(800) CHARACTER SET utf8 DEFAULT NULL,
  `Gender` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `GenderExc` varchar(800) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `abs_users`
--

CREATE TABLE IF not exists `abs_users` (
  `UserID` int(11) NOT NULL,
  `UserCode` varchar(50) DEFAULT NULL,
  `Password` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `abs_users`
--

INSERT INTO `abs_users` (`UserID`, `UserCode`, `Password`) VALUES
(0, 'Maldaqqa', '912345678'),
(1, 'ayla', '1234');

-- --------------------------------------------------------

--
-- Table structure for table `customerscards`
--

CREATE TABLE IF not exists `customerscards` (
  `CustomerCode` varchar(50) NOT NULL,
  `CustomerName` varchar(100) CHARACTER SET utf8 NOT NULL,
  `CardNumber` varchar(50) NOT NULL,
  `ExpiryDate` datetime NOT NULL,
  `Active` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `syncflags`
--

CREATE TABLE IF not exists `syncflags` (
  `POS` varchar(10) NOT NULL,
  `Flag` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `synclog`
--

CREATE TABLE IF not exists `synclog` (
  `POS` int(11) NOT NULL,
  `Type` varchar(50) NOT NULL,
  `Time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `synclog`
--

INSERT INTO `synclog` (`POS`, `Type`, `Time`) VALUES
(1, 'BuyGet', '2021-06-07 17:04:53'),
(1, 'BuyGet', '2021-06-07 17:07:26');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `abs_promo_buy_get`
--
ALTER TABLE `abs_promo_buy_get`
  ADD PRIMARY KEY (`PromoID`);

--
-- Indexes for table `abs_promo_get_percent`
--
ALTER TABLE `abs_promo_get_percent`
  ADD PRIMARY KEY (`PromoID`);

--
-- Indexes for table `abs_users`
--
ALTER TABLE `abs_users`
  ADD PRIMARY KEY (`UserID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
-- phpMyAdmin SQL Dump
-- version 4.3.8
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 05, 2016 at 06:25 AM
-- Server version: 5.5.48-37.8
-- PHP Version: 5.4.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `webosg_iclean`
--

-- --------------------------------------------------------

--
-- Table structure for table `appuser`
--

CREATE TABLE IF NOT EXISTS `appuser` (
  `id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `mobile_no` varchar(11) COLLATE utf8_unicode_ci NOT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `appuser`
--

INSERT INTO `appuser` (`id`, `name`, `mobile_no`, `create`) VALUES
(1, 'Pranjal Srivastava', '8527950535', '2016-06-05 09:43:31'),
(2, 'Adarsh Kumar', '9716114220', '2016-06-05 10:51:15'),
(3, 'adarsh', '9716114221', '2016-06-05 11:01:59'),
(4, 'adarsh kumar', '9716114230', '2016-06-05 11:04:50'),
(5, 'adarsh kumar', '9716114230', '2016-06-05 11:05:42'),
(6, 'adarsh kumar', '9716114230', '2016-06-05 11:06:42');

-- --------------------------------------------------------

--
-- Table structure for table `area`
--

CREATE TABLE IF NOT EXISTS `area` (
  `area_code` int(11) NOT NULL,
  `area_name` varchar(25) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `area`
--

INSERT INTO `area` (`area_code`, `area_name`) VALUES
(1, 'Sector 1'),
(2, 'Sector 2'),
(3, 'Sector 3'),
(4, 'Sector 4'),
(5, 'Sector 5'),
(6, 'Sector 6'),
(7, 'Sector 7'),
(8, 'Sector 8'),
(9, 'Sector 9'),
(10, 'Sector 10'),
(11, 'Sector 11'),
(12, 'Sector 12'),
(13, 'Sector 13'),
(14, 'Sector 14'),
(15, 'Sector 15'),
(16, 'Sector 16');

-- --------------------------------------------------------

--
-- Table structure for table `complaint`
--

CREATE TABLE IF NOT EXISTS `complaint` (
  `id` int(11) NOT NULL,
  `complain` text NOT NULL,
  `address` varchar(50) NOT NULL,
  `area_code` varchar(11) NOT NULL,
  `mobile_no` varchar(10) NOT NULL,
  `photo` text NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comment` text NOT NULL,
  `status` int(1) NOT NULL DEFAULT '0',
  `volid` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `complaint`
--

INSERT INTO `complaint` (`id`, `complain`, `address`, `area_code`, `mobile_no`, `photo`, `time`, `comment`, `status`, `volid`) VALUES
(1, 'shhsbs', 'hdbdbd', '1', '9716114230', 'IMG_8547', '2016-06-05 11:23:54', '', 2, 0);

-- --------------------------------------------------------

--
-- Table structure for table `media`
--

CREATE TABLE IF NOT EXISTS `media` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `company` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `media`
--

INSERT INTO `media` (`id`, `name`, `phone`, `email`, `company`, `created_at`) VALUES
(1, 'Media Person 1', '9823232323', 'med@media.com', 'Media House 1', '2016-06-05 08:43:25');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(11) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `role` int(11) NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `name`, `username`, `password`, `role`) VALUES
(1, 'Administrator', 'admin', '5f4dcc3b5aa765d61d8327deb882cf99', 1),
(16, 'Media Person 1', '9823232323', 'e10adc3949ba59abbe56e057f20f883e', 2),
(17, 'Pranjal Srivastava', '8527950535', '5f4dcc3b5aa765d61d8327deb882cf99', 3);

-- --------------------------------------------------------

--
-- Table structure for table `volrequest`
--

CREATE TABLE IF NOT EXISTS `volrequest` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `dob` varchar(30) NOT NULL,
  `mobile_no` varchar(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `job` varchar(20) NOT NULL,
  `area_code` int(11) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `volunteer`
--

CREATE TABLE IF NOT EXISTS `volunteer` (
  `id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `dob` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `phone` varchar(11) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `job` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `area_code` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `approved` int(11) DEFAULT '0'
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `volunteer`
--

INSERT INTO `volunteer` (`id`, `name`, `dob`, `phone`, `email`, `job`, `area_code`, `created_at`, `approved`) VALUES
(1, 'Pranjal Srivastava', '20-6-1995', '8527950535', '', 'Student', 1, '2016-06-05 09:44:31', 1);

-- --------------------------------------------------------

--
-- Table structure for table `wardmanager`
--

CREATE TABLE IF NOT EXISTS `wardmanager` (
  `wmid` int(11) NOT NULL,
  `fname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `lname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `mname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `wid` int(11) NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `gender` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `dob` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `wardmanager`
--

INSERT INTO `wardmanager` (`wmid`, `fname`, `lname`, `mname`, `wid`, `email`, `phone`, `gender`, `dob`) VALUES
(0, ' Manager 1', '', NULL, 1, NULL, '', '', ''),
(0, 'Manager 2', '', NULL, 2, NULL, '', '', ''),
(0, 'Manager 3', '', NULL, 3, NULL, '', '', ''),
(0, 'Manager 4', '', NULL, 4, NULL, '', '', ''),
(0, 'Manager 5', '', NULL, 5, NULL, '', '', ''),
(0, 'Manager 6', '', NULL, 6, NULL, '', '', ''),
(0, 'Manager 7', '', NULL, 7, NULL, '', '', ''),
(0, 'Manager 8', '', NULL, 8, NULL, '', '', ''),
(0, 'Manager 9', '', NULL, 9, NULL, '', '', ''),
(0, 'Manager 10', '', NULL, 10, NULL, '', '', ''),
(0, 'Manager 11', '', NULL, 11, NULL, '', '', ''),
(0, 'Manager 12', '', NULL, 12, NULL, '', '', ''),
(0, 'Manager 13', '', NULL, 13, NULL, '', '', ''),
(0, 'Manager 14', '', NULL, 14, NULL, '', '', ''),
(0, 'Manager 15', '', NULL, 15, NULL, '', '', ''),
(0, 'Manager 16', '', NULL, 16, NULL, '', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `appuser`
--
ALTER TABLE `appuser`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `area`
--
ALTER TABLE `area`
  ADD PRIMARY KEY (`area_code`);

--
-- Indexes for table `complaint`
--
ALTER TABLE `complaint`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `media`
--
ALTER TABLE `media`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `volrequest`
--
ALTER TABLE `volrequest`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `volunteer`
--
ALTER TABLE `volunteer`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `appuser`
--
ALTER TABLE `appuser`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `area`
--
ALTER TABLE `area`
  MODIFY `area_code` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `complaint`
--
ALTER TABLE `complaint`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `media`
--
ALTER TABLE `media`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT for table `volrequest`
--
ALTER TABLE `volrequest`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `volunteer`
--
ALTER TABLE `volunteer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

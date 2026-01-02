-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 09, 2024 at 12:55 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `qlchungcu`
--

-- --------------------------------------------------------

--
-- Table structure for table `capnhatphisinhhoat`
--

CREATE TABLE `capnhatphisinhhoat` (
  `MaHoKhau` varchar(10) DEFAULT NULL,
  `TienDien` float DEFAULT 0,
  `TienNuoc` float DEFAULT 0,
  `TienInternet` float DEFAULT 0,
  `Thang` int(11) DEFAULT NULL,
  `Nam` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `danhsachphidonggop`
--

CREATE TABLE `danhsachphidonggop` (
  `TenPhi` varchar(30) NOT NULL,
  `SoTienGoiY` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `danhsachphidonggop`
--

INSERT INTO `danhsachphidonggop` (`TenPhi`, `SoTienGoiY`) VALUES
('Quỹ khuyến học', 120000),
('Quỹ người nghèo', 100000),
('Quỹ từ thiện', 50000);

-- --------------------------------------------------------

--
-- Table structure for table `hokhau`
--

CREATE TABLE `hokhau` (
  `MaHoKhau` varchar(10) NOT NULL,
  `DiaChi` varchar(30) NOT NULL,
  `NgayLap` date DEFAULT curdate(),
  `NgayChuyenDi` date DEFAULT curdate(),
  `LyDoChuyen` varchar(30) DEFAULT 'Không',
  `dienTichHo` float DEFAULT 0,
  `SoXeMay` int(11) DEFAULT 0,
  `SoOTo` int(11) DEFAULT 0,
  `SoXeDap` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `hokhau`
--

INSERT INTO `hokhau` (`MaHoKhau`, `DiaChi`, `NgayLap`, `NgayChuyenDi`, `LyDoChuyen`, `dienTichHo`, `SoXeMay`, `SoOTo`, `SoXeDap`) VALUES
('HK001', '123 Đường ABC', '2023-01-17', '2023-02-18', 'Công việc', 70, 1, 0, 1),
('HK002', '456 Đường XYZ', '2023-03-19', '2023-05-20', 'Công việc', 80, 0, 1, 1),
('HK003', '789 Đường KMN', '2023-09-21', '2023-11-14', 'Công việc', 90, 1, 2, 2);

--
-- Triggers `hokhau`
--
DELIMITER $$
CREATE TRIGGER `after_delete_HoKhau` AFTER DELETE ON `hokhau` FOR EACH ROW BEGIN
    -- Xóa toàn bộ nhân khẩu có MaHoKhau là MaHoKhau của hộ khẩu bị xóa
    DELETE FROM NhanKhau WHERE NhanKhau.MaHoKhau = OLD.MaHoKhau;

    -- Xóa dữ liệu trong các bảng PhiDichVu, PhiGuiXe, PhiGuiXe, PhiSinhHoat, PhiQuanLy có MaHoKhau là MaHoKhau của hộ khẩu bị xóa
    DELETE FROM PhiDichVu WHERE PhiDichVu.MaHoKhau = OLD.MaHoKhau;
    DELETE FROM PhiGuiXe WHERE PhiGuiXe.MaHoKhau = OLD.MaHoKhau;
    DELETE FROM PhiSinhHoat WHERE PhiSinhHoat.MaHoKhau = OLD.MaHoKhau;
    DELETE FROM PhiQuanLy WHERE PhiQuanLy.MaHoKhau = OLD.MaHoKhau;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_insert_HoKhau` AFTER INSERT ON `hokhau` FOR EACH ROW BEGIN
    DECLARE namHienTai INT;
    DECLARE namMax INT;
    SET @giaPhiDichVu := (SELECT GiaPhi FROM phidichvu LIMIT 1);
    
    SET @giaPhiQuanLy := (SELECT GiaPhi FROM phiquanly LIMIT 1);

    SET @giaXeMay := (SELECT GiaXeMay FROM phiguixe LIMIT 1);

    SET @giaOTo := (SELECT GiaOTo FROM phiguixe LIMIT 1);

    SET @giaXeDap := (SELECT GiaXeDap FROM phiguixe LIMIT 1);

    SET namHienTai := YEAR(CURRENT_DATE());

    -- Select the maximum value of Nam into namMax from PhiGuiXe
    SET namMax := (SELECT MAX(Nam) FROM PhiGuiXe);
    
    WHILE namHienTai <= namMax DO
        INSERT INTO phidichvu (MaHoKhau, GiaPhi, Nam)
        VALUES (NEW.MaHoKhau, @giaPhiDichVu, namHienTai);
		
        INSERT INTO phiquanly (MaHoKhau, GiaPhi, Nam)
        VALUES (NEW.MaHoKhau, @giaPhiQuanLy, namHienTai);

		INSERT INTO phiguixe (MaHoKhau, GiaXeMay, GiaOTo, GiaXeDap, Nam)
        VALUES (NEW.MaHoKhau, @giaXeMay, @giaOTo, @giaXeDap, namHienTai);
        
        INSERT INTO phisinhhoat (MaHoKhau, Nam)
        VALUES (NEW.MaHoKhau, namHienTai);

        SET namHienTai = namHienTai + 1;
    END WHILE;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `nhankhau`
--

CREATE TABLE `nhankhau` (
  `MaHoKhau` varchar(10) DEFAULT 'Không',
  `HoTen` varchar(30) NOT NULL,
  `Tuoi` int(11) NOT NULL,
  `GioiTinh` varchar(5) NOT NULL,
  `SoCMND_CCCD` varchar(15) NOT NULL,
  `SoDT` varchar(10) NOT NULL,
  `QuanHe` varchar(30) DEFAULT 'Không',
  `TamVang` int(11) DEFAULT 0,
  `TamTru` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `nhankhau`
--

INSERT INTO `nhankhau` (`MaHoKhau`, `HoTen`, `Tuoi`, `GioiTinh`, `SoCMND_CCCD`, `SoDT`, `QuanHe`, `TamVang`, `TamTru`) VALUES
('HK001', 'Nguyễn Văn A', 25, 'Nam', '0123456789', '0123456978', 'Chủ Hộ', 0, 0),
('HK001', 'Nguyễn Văn C', 18, 'Nam', '0123456978', '0915346789', 'Con', 0, 0),
('HK001', 'Trần Thị B', 22, 'Nữ', '0123456987', '0924892383', 'Vợ', 0, 0),
('HK003', 'Trịnh Văn H', 50, 'Nam', '1122334455', '0928321378', 'Chủ Hộ', 0, 0),
('HK003', 'Trần Thị M', 48, 'Nữ', '5544332211', '0928321982', 'Vợ', 0, 0),
('HK002', 'Lê Văn D', 30, 'Nam', '9876543210', '0921378833', 'Chủ Hộ', 0, 0),
('HK002', 'Lê Văn G', 10, 'Nam', '9876546781', '0823728456', 'Con', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `phidichvu`
--

CREATE TABLE `phidichvu` (
  `MaHoKhau` varchar(10) DEFAULT NULL,
  `GiaPhi` float NOT NULL,
  `TienNopMoiThang` float DEFAULT 0,
  `Nam` int(11) NOT NULL,
  `Thang1` float DEFAULT 0,
  `Thang2` float DEFAULT 0,
  `Thang3` float DEFAULT 0,
  `Thang4` float DEFAULT 0,
  `Thang5` float DEFAULT 0,
  `Thang6` float DEFAULT 0,
  `Thang7` float DEFAULT 0,
  `Thang8` float DEFAULT 0,
  `Thang9` float DEFAULT 0,
  `Thang10` float DEFAULT 0,
  `Thang11` float DEFAULT 0,
  `Thang12` float DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `phidichvu`
--

INSERT INTO `phidichvu` (`MaHoKhau`, `GiaPhi`, `TienNopMoiThang`, `Nam`, `Thang1`, `Thang2`, `Thang3`, `Thang4`, `Thang5`, `Thang6`, `Thang7`, `Thang8`, `Thang9`, `Thang10`, `Thang11`, `Thang12`) VALUES
('HK001', 16500, 1155000, 2023, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK002', 16500, 1320000, 2023, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK001', 16500, 1155000, 2024, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK002', 16500, 1320000, 2024, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK001', 16500, 1155000, 2025, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK002', 16500, 1320000, 2025, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK003', 16500, 1485000, 2024, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK003', 16500, 1485000, 2025, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK003', 16500, 1485000, 2023, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `phidonggop`
--

CREATE TABLE `phidonggop` (
  `MaHoKhau` varchar(10) DEFAULT NULL,
  `TenPhi` varchar(30) DEFAULT NULL,
  `SoTien` float NOT NULL,
  `NgayDongGop` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `phidonggop`
--

INSERT INTO `phidonggop` (`MaHoKhau`, `TenPhi`, `SoTien`, `NgayDongGop`) VALUES
('HK001', 'Quỹ người nghèo', 15000, '2023-08-12'),
('HK002', 'Quỹ từ thiện', 10000, '2023-12-12');

-- --------------------------------------------------------

--
-- Table structure for table `phiguixe`
--

CREATE TABLE `phiguixe` (
  `MaHoKhau` varchar(10) DEFAULT NULL,
  `GiaXeMay` float NOT NULL,
  `GiaOTo` float NOT NULL,
  `GiaXeDap` float NOT NULL,
  `TienNopMoiThang` float DEFAULT NULL,
  `Nam` int(11) NOT NULL,
  `Thang1` float DEFAULT 0,
  `Thang2` float DEFAULT 0,
  `Thang3` float DEFAULT 0,
  `Thang4` float DEFAULT 0,
  `Thang5` float DEFAULT 0,
  `Thang6` float DEFAULT 0,
  `Thang7` float DEFAULT 0,
  `Thang8` float DEFAULT 0,
  `Thang9` float DEFAULT 0,
  `Thang10` float DEFAULT 0,
  `Thang11` float DEFAULT 0,
  `Thang12` float DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `phiguixe`
--

INSERT INTO `phiguixe` (`MaHoKhau`, `GiaXeMay`, `GiaOTo`, `GiaXeDap`, `TienNopMoiThang`, `Nam`, `Thang1`, `Thang2`, `Thang3`, `Thang4`, `Thang5`, `Thang6`, `Thang7`, `Thang8`, `Thang9`, `Thang10`, `Thang11`, `Thang12`) VALUES
('HK001', 70000, 1200000, 50000, 120000, 2023, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK002', 70000, 1200000, 50000, 1250000, 2023, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK001', 70000, 1200000, 50000, 120000, 2024, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK002', 70000, 1200000, 50000, 1250000, 2024, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK001', 70000, 1200000, 50000, 120000, 2025, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK002', 70000, 1200000, 50000, 1250000, 2025, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK003', 70000, 1200000, 50000, 2570000, 2024, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK003', 70000, 1200000, 50000, 2570000, 2025, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK003', 70000, 1200000, 50000, 2570000, 2023, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `phiquanly`
--

CREATE TABLE `phiquanly` (
  `MaHoKhau` varchar(10) DEFAULT NULL,
  `GiaPhi` float NOT NULL,
  `TienNopMoiThang` float DEFAULT 0,
  `Nam` int(11) NOT NULL,
  `Thang1` float DEFAULT 0,
  `Thang2` float DEFAULT 0,
  `Thang3` float DEFAULT 0,
  `Thang4` float DEFAULT 0,
  `Thang5` float DEFAULT 0,
  `Thang6` float DEFAULT 0,
  `Thang7` float DEFAULT 0,
  `Thang8` float DEFAULT 0,
  `Thang9` float DEFAULT 0,
  `Thang10` float DEFAULT 0,
  `Thang11` float DEFAULT 0,
  `Thang12` float DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `phiquanly`
--

INSERT INTO `phiquanly` (`MaHoKhau`, `GiaPhi`, `TienNopMoiThang`, `Nam`, `Thang1`, `Thang2`, `Thang3`, `Thang4`, `Thang5`, `Thang6`, `Thang7`, `Thang8`, `Thang9`, `Thang10`, `Thang11`, `Thang12`) VALUES
('HK001', 7000, 490000, 2023, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK002', 7000, 560000, 2023, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK001', 7000, 490000, 2024, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK002', 7000, 560000, 2024, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK001', 7000, 490000, 2025, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK002', 7000, 560000, 2025, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK003', 7000, 630000, 2024, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK003', 7000, 630000, 2025, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('HK003', 7000, 630000, 2023, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `phisinhhoat`
--

CREATE TABLE `phisinhhoat` (
  `MaHoKhau` varchar(10) DEFAULT NULL,
  `Thang1` float DEFAULT 0,
  `Thang2` float DEFAULT 0,
  `Thang3` float DEFAULT 0,
  `Thang4` float DEFAULT 0,
  `Thang5` float DEFAULT 0,
  `Thang6` float DEFAULT 0,
  `Thang7` float DEFAULT 0,
  `Thang8` float DEFAULT 0,
  `Thang9` float DEFAULT 0,
  `Thang10` float DEFAULT 0,
  `Thang11` float DEFAULT 0,
  `Thang12` float DEFAULT 0,
  `Nam` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `phisinhhoat`
--

INSERT INTO `phisinhhoat` (`MaHoKhau`, `Thang1`, `Thang2`, `Thang3`, `Thang4`, `Thang5`, `Thang6`, `Thang7`, `Thang8`, `Thang9`, `Thang10`, `Thang11`, `Thang12`, `Nam`) VALUES
('HK001', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2023),
('HK002', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2023),
('HK001', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2024),
('HK002', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2024),
('HK001', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2025),
('HK002', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2025),
('HK003', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2024),
('HK003', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2025);

-- --------------------------------------------------------

--
-- Table structure for table `tamtru`
--

CREATE TABLE `tamtru` (
  `MaTamTru` varchar(10) NOT NULL,
  `SoCMND_CCCD` varchar(15) NOT NULL,
  `LyDo` varchar(50) NOT NULL,
  `TuNgay` date NOT NULL,
  `DenNgay` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Triggers `tamtru`
--
DELIMITER $$
CREATE TRIGGER `after_delete_TamTru` AFTER DELETE ON `tamtru` FOR EACH ROW BEGIN
    -- Cập nhật giá trị cột TamVang của bảng NhanKhau về 0
    UPDATE NhanKhau
    SET TamTru = 0
    WHERE SoCMND_CCCD = OLD.SoCMND_CCCD;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_insert_TamTru` AFTER INSERT ON `tamtru` FOR EACH ROW BEGIN
    -- Cập nhật giá trị cột TamTru của bảng NhanKhau
    UPDATE NhanKhau
    SET TamTru = 1
    WHERE SoCMND_CCCD = NEW.SoCMND_CCCD;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tamvang`
--

CREATE TABLE `tamvang` (
  `MaTamVang` varchar(10) NOT NULL,
  `SoCMND_CCCD` varchar(15) NOT NULL,
  `NoiTamTru` varchar(50) NOT NULL,
  `TuNgay` date NOT NULL,
  `DenNgay` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Triggers `tamvang`
--
DELIMITER $$
CREATE TRIGGER `after_delete_TamVang` AFTER DELETE ON `tamvang` FOR EACH ROW BEGIN
    -- Cập nhật giá trị cột TamVang của bảng NhanKhau về 0
    UPDATE NhanKhau
    SET TamVang = 0
    WHERE SoCMND_CCCD = OLD.SoCMND_CCCD;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_insert_TamVang` AFTER INSERT ON `tamvang` FOR EACH ROW BEGIN
    -- Cập nhật giá trị cột TamVang của bảng NhanKhau
    UPDATE NhanKhau
    SET TamVang = 1
    WHERE SoCMND_CCCD = NEW.SoCMND_CCCD;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `thanhtoan`
--

CREATE TABLE `thanhtoan` (
  `MaHoKhau` varchar(10) NOT NULL,
  `SoTienThanhToan` float NOT NULL,
  `NgayThanhToan` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `UserName` varchar(30) NOT NULL,
  `Password` varchar(30) NOT NULL,
  `HoTen` varchar(30) NOT NULL,
  `Email` varchar(30) NOT NULL,
  `SoDT` varchar(30) NOT NULL,
  `DiaChi` varchar(30) NOT NULL,
  `Tuoi` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`UserName`, `Password`, `HoTen`, `Email`, `SoDT`, `DiaChi`, `Tuoi`) VALUES
('admin', '123456', 'Admin', 'admin@gmail.com', '0123456789', 'Hai Bà Trưng, HN', 31);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `capnhatphisinhhoat`
--
ALTER TABLE `capnhatphisinhhoat`
  ADD KEY `FK_CapNhatPhiSinhHoat_HoKhau` (`MaHoKhau`);

--
-- Indexes for table `danhsachphidonggop`
--
ALTER TABLE `danhsachphidonggop`
  ADD PRIMARY KEY (`TenPhi`);

--
-- Indexes for table `hokhau`
--
ALTER TABLE `hokhau`
  ADD PRIMARY KEY (`MaHoKhau`);

--
-- Indexes for table `nhankhau`
--
ALTER TABLE `nhankhau`
  ADD PRIMARY KEY (`SoCMND_CCCD`),
  ADD KEY `FK_NhanKhau_HoKhau` (`MaHoKhau`);

--
-- Indexes for table `phidichvu`
--
ALTER TABLE `phidichvu`
  ADD KEY `FK_PhiDichVu_HoKhau` (`MaHoKhau`);

--
-- Indexes for table `phidonggop`
--
ALTER TABLE `phidonggop`
  ADD KEY `FK_TenPhi` (`TenPhi`),
  ADD KEY `FK_PhiDongGop_HoKhau` (`MaHoKhau`);

--
-- Indexes for table `phiguixe`
--
ALTER TABLE `phiguixe`
  ADD KEY `FK_PhiGuiXe_HoKhau` (`MaHoKhau`);

--
-- Indexes for table `phiquanly`
--
ALTER TABLE `phiquanly`
  ADD KEY `FK_PhiQuanLy_HoKhau` (`MaHoKhau`);

--
-- Indexes for table `phisinhhoat`
--
ALTER TABLE `phisinhhoat`
  ADD KEY `FK_PhiSinhHoat_HoKhau` (`MaHoKhau`);

--
-- Indexes for table `tamtru`
--
ALTER TABLE `tamtru`
  ADD PRIMARY KEY (`MaTamTru`),
  ADD KEY `FK_TamTru_NhanKhau` (`SoCMND_CCCD`);

--
-- Indexes for table `tamvang`
--
ALTER TABLE `tamvang`
  ADD PRIMARY KEY (`MaTamVang`),
  ADD KEY `FK_TamVang_NhanKhau` (`SoCMND_CCCD`);

--
-- Indexes for table `thanhtoan`
--
ALTER TABLE `thanhtoan`
  ADD KEY `MaHoKhau` (`MaHoKhau`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UserName`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `capnhatphisinhhoat`
--
ALTER TABLE `capnhatphisinhhoat`
  ADD CONSTRAINT `FK_CapNhatPhiSinhHoat_HoKhau` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`) ON DELETE CASCADE,
  ADD CONSTRAINT `capnhatphisinhhoat_ibfk_1` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`);

--
-- Constraints for table `nhankhau`
--
ALTER TABLE `nhankhau`
  ADD CONSTRAINT `FK_NhanKhau_HoKhau` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`) ON DELETE CASCADE;

--
-- Constraints for table `phidichvu`
--
ALTER TABLE `phidichvu`
  ADD CONSTRAINT `FK_PhiDichVu_HoKhau` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`) ON DELETE CASCADE,
  ADD CONSTRAINT `phidichvu_ibfk_1` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`);

--
-- Constraints for table `phidonggop`
--
ALTER TABLE `phidonggop`
  ADD CONSTRAINT `FK_PhiDongGop_HoKhau` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_TenPhi` FOREIGN KEY (`TenPhi`) REFERENCES `danhsachphidonggop` (`TenPhi`),
  ADD CONSTRAINT `phidonggop_ibfk_1` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`);

--
-- Constraints for table `phiguixe`
--
ALTER TABLE `phiguixe`
  ADD CONSTRAINT `FK_PhiGuiXe_HoKhau` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`) ON DELETE CASCADE,
  ADD CONSTRAINT `phiguixe_ibfk_1` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`);

--
-- Constraints for table `phiquanly`
--
ALTER TABLE `phiquanly`
  ADD CONSTRAINT `FK_PhiQuanLy_HoKhau` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`) ON DELETE CASCADE,
  ADD CONSTRAINT `phiquanly_ibfk_1` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`);

--
-- Constraints for table `phisinhhoat`
--
ALTER TABLE `phisinhhoat`
  ADD CONSTRAINT `FK_PhiSinhHoat_HoKhau` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`) ON DELETE CASCADE,
  ADD CONSTRAINT `phisinhhoat_ibfk_1` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`);

--
-- Constraints for table `tamtru`
--
ALTER TABLE `tamtru`
  ADD CONSTRAINT `FK_TamTru_NhanKhau` FOREIGN KEY (`SoCMND_CCCD`) REFERENCES `nhankhau` (`SoCMND_CCCD`) ON DELETE CASCADE,
  ADD CONSTRAINT `tamtru_ibfk_1` FOREIGN KEY (`SoCMND_CCCD`) REFERENCES `nhankhau` (`SoCMND_CCCD`);

--
-- Constraints for table `tamvang`
--
ALTER TABLE `tamvang`
  ADD CONSTRAINT `FK_TamVang_NhanKhau` FOREIGN KEY (`SoCMND_CCCD`) REFERENCES `nhankhau` (`SoCMND_CCCD`) ON DELETE CASCADE,
  ADD CONSTRAINT `tamvang_ibfk_1` FOREIGN KEY (`SoCMND_CCCD`) REFERENCES `nhankhau` (`SoCMND_CCCD`);

--
-- Constraints for table `thanhtoan`
--
ALTER TABLE `thanhtoan`
  ADD CONSTRAINT `thanhtoan_ibfk_1` FOREIGN KEY (`MaHoKhau`) REFERENCES `hokhau` (`MaHoKhau`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

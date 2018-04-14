-- MySQL dump 10.13  Distrib 5.6.28, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: CAT
-- ------------------------------------------------------
-- Server version	5.6.28-0ubuntu0.15.10.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `suggestionTable`
--

DROP TABLE IF EXISTS `suggestionTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `suggestionTable` (
  `id` varchar(100) NOT NULL DEFAULT '',
  `suggestion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suggestionTable`
--

LOCK TABLES `suggestionTable` WRITE;
/*!40000 ALTER TABLE `suggestionTable` DISABLE KEYS */;
INSERT INTO `suggestionTable` VALUES ('arcRemovalMSRCTC','Generate arcs with different color'),('boundaryNoiseRmvImg','Make boundaries with different pattern or merge boundaries with charchters pixels '),('getAdvDotRmvObj','Add intense amount of dots yet, preserving the visibility of characters '),('getBinarizedImage','Rotate the image slightly!'),('getCharRepaired','Distort the charchaters well!'),('getDotRmvObj','Generate Automatic Dots!'),('getEdgeRmvImg','Invert the image!'),('getHorLineRmvObj','Generate thick lines!'),('getHorLineRmvObj2px','Generate thick lines!'),('getRevGrayscaledObj','Change background to a natural image!'),('getSlant120ThickCharObj','Genrate distortion of charchters in 120degrees'),('getSlant60ThickCharObj','Generate distortion of charchters in 60degrees.'),('getSlantCharRepairedObj','Generate distortion of charchters in 60 and 120degrees'),('getThickHCharObj','Change character colour'),('getThickWCharObj','Change character colour'),('getVerLineRmvObj','Add vertical Lines with 0px gap'),('getVerLineRmvObj2px','add 3 lines with 1px gap! Repeat this possibly few times on captcha'),('Segmentation.DisconnectedSegment$1','Gaps between charachters are more(Check the vertical projection to see the gaps!)'),('Segmentation.EightConnectivity$1','Characters are eight neighbour connected'),('Segmentation.Overlapped$1','Characters should be connected .Avoid gaps!'),('Segmentation.SnakeSegmentation$1','Unaligned Segmentation Broke'),('unknownNoiseRemoval','Rotate image more than 30 units in any direction');
/*!40000 ALTER TABLE `suggestionTable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-26 20:22:04

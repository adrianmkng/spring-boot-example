CREATE DATABASE IF NOT EXISTS `spring_boot_example` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `spring_boot_example`;

-- structure for table account
CREATE TABLE IF NOT EXISTS `account` (
  `email` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- structure for table spring_boot_example.users
CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` boolean NOT NULL DEFAULT '1',
  UNIQUE index username_idx_1 (`username`),
  FOREIGN KEY (`username`) REFERENCES account(`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- structure for table spring_boot_example.user_roles
CREATE TABLE IF NOT EXISTS `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  FOREIGN KEY (`username`) REFERENCES users (`username`),
  UNIQUE index authorities_idx_1 (`username`, `authority`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `assignments`;
DROP TABLE IF EXISTS `tags`;
DROP TABLE IF EXISTS `problems`;
DROP TABLE IF EXISTS `usergroups`;
DROP TABLE IF EXISTS `groups`;
DROP TABLE IF EXISTS `userroles`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `roles`;

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) NOT NULL,
  `description` varchar(300) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `rolename` (`rolename`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `roles` VALUES (1,'admin','admin role'),(2,'teacher','teacher role'),(3,'user','user role');

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `passwordhash` varchar(128) NOT NULL,
  `passwordsalt` varchar(128) NOT NULL,
  `isdisabled` tinyint(1) DEFAULT NULL,
  `validationcode` varchar(128) NOT NULL DEFAULT "",
  `changepasswordcode` varchar(128) DEFAULT NULL,
  `registrationdate` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `roleid` (`roleid`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`roleid`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `users` (id, roleid, username, firstname, lastname, email, passwordhash, passwordsalt, isdisabled) VALUES (1,1,'pesho','Petar','Petrov','pppepito86@gmail.com','$2a$10$xJQ1V9Xu1x5b5jphJIOwOuTW3YZKeu/UXyQUIMW5EX914cyiz1XQG','$2a$10$xJQ1V9Xu1x5b5jphJIOwOuTW3YZKeu/UXyQUIMW5EX914cyiz1XQG',0);
INSERT INTO `users` (id, roleid, username, firstname, lastname, email, passwordhash, passwordsalt, isdisabled) VALUES (2,2,'teacher','Daskal','Daskalov','teacher@gmail.com','$2a$10$xJQ1V9Xu1x5b5jphJIOwOuTW3YZKeu/UXyQUIMW5EX914cyiz1XQG','$2a$10$xJQ1V9Xu1x5b5jphJIOwOuTW3YZKeu/UXyQUIMW5EX914cyiz1XQG',0);

--
-- Table structure for table `userroles`
--

CREATE TABLE `userroles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) NOT NULL,
  `description` varchar(300) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `rolename` (`rolename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupname` varchar(50) NOT NULL,
  `description` varchar(300) NOT NULL,
  `creatorid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupname` (`groupname`),
  KEY `creatorid` (`creatorid`),
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`creatorid`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `groups` VALUES (1,'public','public',1);

--
-- Table structure for table `usergroups`
--

CREATE TABLE `usergroups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `groupid` int(11) NOT NULL,
  `roleid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_group` (`userid`,`groupid`),
  KEY `groupid` (`groupid`),
  CONSTRAINT `usergroups_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`),
  CONSTRAINT `usergroups_ibfk_2` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `problems`
--

CREATE TABLE `problems` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `version` varchar(10) DEFAULT NULL,
  `description` varchar(10000) NOT NULL,
  `languages` varchar(1000) NOT NULL,
  `points` int(11) NOT NULL,
  `visibility` varchar(10) NOT NULL,
  `author` int(11) NOT NULL,
  `tests` int(11) DEFAULT NULL,
  `source_checker` varchar(100) DEFAULT NULL,
  `test_checker` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `problem_version` (`name`,`version`),
  KEY `author` (`author`),
  CONSTRAINT `problems_ibfk_1` FOREIGN KEY (`author`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO problems(id, name, version, description, languages, visibility, author, points) VALUES(1, 'a+b', 'version', 'namerete sbora na chislata a i b', 'java', 'private', 1, 100);

--
-- Table structure for table `tags` 
--

CREATE TABLE `tags` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `problemid` int(11) NOT NULL,
    `tag` varchar(64) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `tags_ibfk_1` FOREIGN KEY (`problemid`) REFERENCES problems(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO tags(id, problemid, tag) VALUES(1, 1, 'dp');
INSERT INTO tags(id, problemid, tag) VALUES(2, 1, 'binarysearch');

--
-- Table structure for table `assignments`
--

CREATE TABLE `assignments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `starttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `endtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `author` int(11) NOT NULL,
  `groupid` int(11) DEFAULT NULL,
  `testinfo` VARCHAR(16) NOT NULL,
  `standings` VARCHAR(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `author` (`author`),
  KEY `groupid` (`groupid`),
  CONSTRAINT `assignments_ibfk_1` FOREIGN KEY (`author`) REFERENCES `users` (`id`),
  CONSTRAINT `assignments_ibfk_2` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

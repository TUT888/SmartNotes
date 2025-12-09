CREATE DATABASE IF NOT EXISTS `testdb`;
USE `testdb`;

CREATE TABLE IF NOT EXISTS `user`
(
    `id`         int          NOT NULL AUTO_INCREMENT,
    `email`      varchar(255) NOT NULL,
    `password`   varchar(255) NOT NULL,
    `name`       varchar(255) NOT NULL,
    `avatar_url` tinytext,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
);

CREATE TABLE IF NOT EXISTS `tag`
(
    `id`      int         NOT NULL AUTO_INCREMENT,
    `user_id` int         NOT NULL,
    `name`    varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_tags_user_id` (`user_id`),
    CONSTRAINT `fk_tags_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `document`
(
    `id`         int                 NOT NULL AUTO_INCREMENT,
    `user_id`    int                 NOT NULL,
    `title`      varchar(255)        NOT NULL,
    `type`       enum ('NOTE','PDF') NOT NULL,
    `created_at` datetime            NOT NULL,
    `updated_at` datetime DEFAULT NULL,
    `content`    text,
    `file_url`   tinytext,
    `file_size`  int      DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_documents_user_id` (`user_id`),
    CONSTRAINT `fk_documents_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `chk_document_type_content_file` CHECK ((((`type` = _utf8mb4'NOTE') and (`content` is not null)) or
                                                        ((`type` = _utf8mb4'PDF') and (`file_url` is not null) and
                                                         (`file_size` is not null))))
);

CREATE TABLE IF NOT EXISTS `document_tag`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `document_id` int NOT NULL,
    `tag_id`      int DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_document_tags_document_id` (`document_id`),
    KEY `fk_document_tags_tag_id` (`tag_id`),
    CONSTRAINT `fk_document_tags_document_id` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`),
    CONSTRAINT `fk_document_tags_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
);

CREATE TABLE IF NOT EXISTS `flashcard_set`
(
    `id`          int                          NOT NULL AUTO_INCREMENT,
    `user_id`     int                          NOT NULL,
    `title`       varchar(255)                 NOT NULL,
    `origin_type` enum ('AI','USER','DEFAULT') NOT NULL,
    `created_at`  datetime DEFAULT NULL,
    `updated_at`  datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_flashcard_sets_user_id` (`user_id`),
    CONSTRAINT `fk_flashcard_sets_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `flashcard`
(
    `id`                 int  NOT NULL AUTO_INCREMENT,
    `flashcard_set_id`   int  NOT NULL,
    `front_content`      text NOT NULL,
    `back_content`       text NOT NULL,
    `source_document_id` int      DEFAULT NULL,
    `created_at`         datetime DEFAULT NULL,
    `updated_at`         datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_flashcards_flashcard_set_id` (`flashcard_set_id`),
    KEY `fk_flashcards_source_document_id` (`source_document_id`),
    CONSTRAINT `fk_flashcards_flashcard_set_id` FOREIGN KEY (`flashcard_set_id`) REFERENCES `flashcard_set` (`id`),
    CONSTRAINT `fk_flashcards_source_document_id` FOREIGN KEY (`source_document_id`) REFERENCES `document` (`id`)
);

CREATE TABLE IF NOT EXISTS `quiz_set`
(
    `id`          int                          NOT NULL AUTO_INCREMENT,
    `user_id`     int                          NOT NULL,
    `title`       varchar(255)                 NOT NULL,
    `origin_type` enum ('AI','USER','DEFAULT') NOT NULL,
    `created_at`  datetime DEFAULT NULL,
    `updated_at`  datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_quiz_set_user_id_idx` (`user_id`),
    CONSTRAINT `fk_quiz_set_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `quiz`
(
    `id`                 int          NOT NULL AUTO_INCREMENT,
    `quiz_set_id`        int          NOT NULL,
    `source_document_id` int      DEFAULT NULL,
    `title`              varchar(255) NOT NULL,
    `created_at`         datetime DEFAULT NULL,
    `updated_at`         datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_quiz_set_id_idx` (`quiz_set_id`),
    KEY `fk_quiz_src_document_id` (`source_document_id`),
    CONSTRAINT `fk_quiz_set_id` FOREIGN KEY (`quiz_set_id`) REFERENCES `quiz_set` (`id`),
    CONSTRAINT `fk_quiz_src_document_id` FOREIGN KEY (`source_document_id`) REFERENCES `document` (`id`)
);

CREATE TABLE IF NOT EXISTS `question`
(
    `id`             int          NOT NULL AUTO_INCREMENT,
    `quiz_id`        int          NOT NULL,
    `question_text`  varchar(255) NOT NULL,
    `option_a`       varchar(255) NOT NULL,
    `option_b`       varchar(255) NOT NULL,
    `option_c`       varchar(255) NOT NULL,
    `option_d`       varchar(255) NOT NULL,
    `correct_answer` char(1)      NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_questions_quiz_id` (`quiz_id`),
    CONSTRAINT `fk_questions_quiz_id` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`)
);

CREATE TABLE IF NOT EXISTS `attempt`
(
    `id`             int      NOT NULL AUTO_INCREMENT,
    `quiz_id`        int      NOT NULL,
    `attempt_at`     datetime NOT NULL,
    `total_question` int      NOT NULL,
    `score`          int DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `fk_attempts_quiz_id_idx` (`quiz_id`),
    CONSTRAINT `fk_attempts_quiz_id` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`)
);

CREATE TABLE IF NOT EXISTS `attempt_detail`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `attempt_id`  int NOT NULL,
    `question_id` int NOT NULL,
    `user_answer` char(1)    DEFAULT NULL,
    `is_correct`  tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_attempt_details_attempt_id` (`attempt_id`),
    KEY `fk_attempt_details_question_id_idx` (`question_id`),
    CONSTRAINT `fk_attempt_details_attempt_id` FOREIGN KEY (`attempt_id`) REFERENCES `attempt` (`id`),
    CONSTRAINT `fk_attempt_details_question_id` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
);
DROP DATABASE IF EXISTS smartnotes;

CREATE DATABASE smartnotes;
USE smartnotes;

CREATE TABLE users (
	id INTEGER NOT NULL AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    avatar_url TINYTEXT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE tags (
	id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tags_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE documents (
	id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    title VARCHAR(128) NOT NULL,
    type ENUM('note', 'pdf') NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_documents_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE document_tags (
	id INTEGER NOT NULL AUTO_INCREMENT,
    document_id INTEGER NOT NULL,
    tag_id INTEGER,
    PRIMARY KEY (id),
    CONSTRAINT fk_document_tags_document_id FOREIGN KEY (document_id) REFERENCES documents (id),
    CONSTRAINT fk_document_tags_tag_id FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE TABLE notes (
	id INTEGER NOT NULL,
    content TEXT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_notes_id FOREIGN KEY (id) REFERENCES documents (id)
);

CREATE TABLE pdfs (
	id INTEGER NOT NULL,
    file_url TINYTEXT NOT NULL,
    size INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_pdfs_id FOREIGN KEY (id) REFERENCES documents (id)
);

CREATE TABLE flashcard_sets (
	id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    title VARCHAR(128) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_flashcard_sets_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE flashcards (
	id INTEGER NOT NULL AUTO_INCREMENT,
    flashcard_set_id INTEGER NOT NULL,
    front_content TEXT NOT NULL,
    back_content TEXT NOT NULL,
    source_document_id INTEGER NOT NULL,
    created_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_flashcards_flashcard_set_id FOREIGN KEY (flashcard_set_id) REFERENCES flashcard_sets (id),
    CONSTRAINT fk_flashcards_source_document_id FOREIGN KEY (source_document_id) REFERENCES documents (id)
);

CREATE TABLE quizzes (
	id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    title VARCHAR(128) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_quizzes_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE questions (
	id INTEGER NOT NULL AUTO_INCREMENT,
    quiz_id INTEGER NOT NULL,
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    correct_answer CHAR(1) NOT NULL,
    source_document_id INTEGER NOT NULL,
    created_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_questions_quiz_id FOREIGN KEY (quiz_id) REFERENCES quizzes (id),
    CONSTRAINT fk_questions_source_document_id FOREIGN KEY (source_document_id) REFERENCES documents (id)
);

CREATE TABLE attempts (
	id INTEGER NOT NULL AUTO_INCREMENT,
    quiz_id INTEGER NOT NULL,
    attempt_at DATETIME NOT NULL,
    total_question INTEGER NOT NULL,
    score INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_attempts_quiz_id FOREIGN KEY (quiz_id) REFERENCES quizzes (id)
);

CREATE TABLE attempt_details (
	id INTEGER NOT NULL AUTO_INCREMENT,
    attempt_id INTEGER NOT NULL,
    user_answer CHAR(1) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_attempt_details_attempt_id FOREIGN KEY (attempt_id) REFERENCES attempts (id)
);

-- INSERTION
INSERT INTO users(email, password, name, created_at) VALUES
('john@gmail.com', 'john', 'John', '2025-10-13 18:00:00'),
('mary@gmail.com', 'mary', 'Mary', '2025-10-22 18:00:00');

INSERT INTO documents(user_id, title, type, created_at) VALUES
(1, 'Time and Space Complexity', 'note', '2025-10-13 18:00:00'),
(2, 'Object-Oriented Programming Concepts', 'note', '2025-10-14 22:12:25');

-- INSERT INTO notes(id, content) VALUES
-- (1, ''),
-- (2, '');

-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: smartnotes
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attempt_details`
--

DROP TABLE IF EXISTS `attempt_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attempt_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `attempt_id` int NOT NULL,
  `user_answer` char(1) NOT NULL,
  `is_correct` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_attempt_details_attempt_id` (`attempt_id`),
  CONSTRAINT `fk_attempt_details_attempt_id` FOREIGN KEY (`attempt_id`) REFERENCES `attempts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attempt_details`
--

LOCK TABLES `attempt_details` WRITE;
/*!40000 ALTER TABLE `attempt_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `attempt_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attempts`
--

DROP TABLE IF EXISTS `attempts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attempts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quiz_id` int NOT NULL,
  `attempt_at` datetime NOT NULL,
  `total_question` int NOT NULL,
  `score` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_attempts_quiz_id` (`quiz_id`),
  CONSTRAINT `fk_attempts_quiz_id` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attempts`
--

LOCK TABLES `attempts` WRITE;
/*!40000 ALTER TABLE `attempts` DISABLE KEYS */;
/*!40000 ALTER TABLE `attempts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document_tags`
--

DROP TABLE IF EXISTS `document_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document_tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `document_id` int NOT NULL,
  `tag_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_document_tags_document_id` (`document_id`),
  KEY `fk_document_tags_tag_id` (`tag_id`),
  CONSTRAINT `fk_document_tags_document_id` FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`),
  CONSTRAINT `fk_document_tags_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document_tags`
--

LOCK TABLES `document_tags` WRITE;
/*!40000 ALTER TABLE `document_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `document_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documents`
--

DROP TABLE IF EXISTS `documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documents` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(128) NOT NULL,
  `type` enum('note','pdf') NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_documents_user_id` (`user_id`),
  CONSTRAINT `fk_documents_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documents`
--

LOCK TABLES `documents` WRITE;
/*!40000 ALTER TABLE `documents` DISABLE KEYS */;
INSERT INTO `documents` VALUES (1,1,'Time and Space Complexity','note','2025-10-13 18:00:00',NULL),(2,2,'Object-Oriented Programming Concepts','note','2025-10-14 22:12:25',NULL);
/*!40000 ALTER TABLE `documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flashcard_sets`
--

DROP TABLE IF EXISTS `flashcard_sets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flashcard_sets` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_flashcard_sets_user_id` (`user_id`),
  CONSTRAINT `fk_flashcard_sets_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flashcard_sets`
--

LOCK TABLES `flashcard_sets` WRITE;
/*!40000 ALTER TABLE `flashcard_sets` DISABLE KEYS */;
/*!40000 ALTER TABLE `flashcard_sets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flashcards`
--

DROP TABLE IF EXISTS `flashcards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flashcards` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flashcard_set_id` int NOT NULL,
  `front_content` text NOT NULL,
  `back_content` text NOT NULL,
  `source_document_id` int NOT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_flashcards_flashcard_set_id` (`flashcard_set_id`),
  KEY `fk_flashcards_source_document_id` (`source_document_id`),
  CONSTRAINT `fk_flashcards_flashcard_set_id` FOREIGN KEY (`flashcard_set_id`) REFERENCES `flashcard_sets` (`id`),
  CONSTRAINT `fk_flashcards_source_document_id` FOREIGN KEY (`source_document_id`) REFERENCES `documents` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flashcards`
--

LOCK TABLES `flashcards` WRITE;
/*!40000 ALTER TABLE `flashcards` DISABLE KEYS */;
/*!40000 ALTER TABLE `flashcards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notes`
--

DROP TABLE IF EXISTS `notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notes` (
  `id` int NOT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_notes_id` FOREIGN KEY (`id`) REFERENCES `documents` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notes`
--

LOCK TABLES `notes` WRITE;
/*!40000 ALTER TABLE `notes` DISABLE KEYS */;
INSERT INTO `notes` VALUES (1,'# Time and Space Complexity - Sample study note generated by AI ## What is Time Complexity? Time complexity measures how the runtime of an algorithm grows as the input size increases. We use Big O notation to describe the worst-case scenario. It helps us compare algorithms and predict performance with large datasets. ## What is Space Complexity? Space complexity measures how much memory an algorithm uses relative to input size. This includes both the space for input data and any extra space the algorithm needs to work. ## Big O Notation Basics We focus on the dominant term and ignore constants. If an algorithm takes 5n² + 3n + 7 steps, we say it\'s O(n²) because n² grows much faster than n or constant terms when n gets large. ## Common Time Complexities **O(1) - Constant Time** Takes same time regardless of input size. Examples: accessing array element by index, basic math operations, hash table lookup in best case. **O(log n) - Logarithmic Time** Very efficient, grows slowly. Examples: binary search, finding element in balanced binary search tree. If you double input size, you only add one more step. **O(n) - Linear Time** Time grows directly with input size. Examples: linear search through array, printing all elements, finding maximum value in unsorted array. **O(n log n) - Linearithmic Time** Common in good sorting algorithms. Examples: merge sort, heap sort, quick sort average case. Much better than O(n²) but slower than O(n). **O(n²) - Quadratic Time** Time grows with square of input size. Examples: bubble sort, selection sort, nested loops checking all pairs. Gets slow quickly with large inputs. **O(2ⁿ) - Exponential Time** Extremely slow for large inputs. Examples: recursive fibonacci without memoization, trying all subsets of a set. Avoid if possible. ## Space Complexity Examples **O(1) Space** - Algorithm uses same amount of extra memory regardless of input size. Examples: swapping two variables, iterative algorithms that only use a few variables. **O(n) Space** - Memory usage grows with input size. Examples: creating copy of array, recursive algorithms (call stack), merge sort temporary arrays. **O(log n) Space** - Usually from recursion depth. Examples: binary search recursive implementation, balanced tree operations. ## Analyzing Algorithms For loops: if loop runs n times, that\'s O(n). Nested loops multiply complexities together. Recursion: look at how many times function calls itself and how much work each call does. Tree-like recursion can be exponential. ## Trade-offs Sometimes we can trade time for space or vice versa. Dynamic programming uses more memory to avoid recalculating same values. Hash tables use extra space to get faster lookups. ## Practical Tips - O(1) and O(log n) are excellent for any input size - O(n) and O(n log n) are good for most practical purposes - O(n²) starts getting slow around 10,000 elements - O(2ⁿ) is only practical for very small inputs (maybe 20-30 elements) ## Appendix **Common Mistakes:** Confusing best case with worst case. Hash tables are O(1) average but O(n) worst case. Always consider worst case for Big O analysis. **Important Questions:** How does choice of data structure affect complexity? When might we prefer a slower algorithm? What\'s the relationship between recursion depth and space complexity? **Examples to Remember:** Binary search: O(log n) time, O(1) space iterative or O(log n) space recursive Merge sort: O(n log n) time, O(n) space Bubble sort: O(n²) time, O(1) space Fibonacci recursive: O(2ⁿ) time, O(n) space'),(2,'# Object-Oriented Programming Concepts - Sample study note generated by AI ## What is Object-Oriented Programming? OOP is a programming approach that organizes code around objects rather than functions. An object contains both data (attributes) and methods (functions) that work with that data. Think of it like a blueprint for creating things. ## Four Main Principles - **Encapsulation**: Bundling data and methods together in a class. We hide internal details and only expose what\'s necessary. Like a car - you use the steering wheel and pedals, but don\'t need to know how the engine works internally. - **Inheritance**: Creating new classes based on existing ones. The new class gets all features of the parent class and can add its own. Example: Animal class has eat() method, Dog class inherits from Animal and adds bark() method. - **Polymorphism**: Same method name can behave differently in different classes. A draw() method works differently for Circle, Rectangle, and Triangle classes, but they all draw shapes. - **Abstraction**: Focusing on essential features while hiding complex implementation details. We know what a method does without caring about how it does it. ## Classes vs Objects Class is like a blueprint or template. Object is an actual instance created from that class. One Person class can create many person objects like john, mary, alex. ## Benefits of OOP - Code reusability through inheritance - Easier to maintain and modify - Better organization of complex programs - Matches real-world thinking patterns - Team development becomes easier ## Common Mistakes to Avoid - Making everything public instead of using proper access modifiers - Creating classes that try to do too many things - Not using inheritance when it would be helpful - Overcomplicating simple problems with unnecessary objects ## Key Terms - **Constructor** - special method that runs when object is created - **Method overriding** - child class changes parent\'s method behavior - **Interface** - contract that classes must follow - **Static methods** - belong to class, not individual objects ## Example Structure ```java class Student { private String name; private int age; public Student(String name, int age) { this.name = name; this.age = age; } public void study() { System.out.println(name + \" is studying\"); } } ```');
/*!40000 ALTER TABLE `notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pdfs`
--

DROP TABLE IF EXISTS `pdfs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pdfs` (
  `id` int NOT NULL,
  `file_url` tinytext NOT NULL,
  `size` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_pdfs_id` FOREIGN KEY (`id`) REFERENCES `documents` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pdfs`
--

LOCK TABLES `pdfs` WRITE;
/*!40000 ALTER TABLE `pdfs` DISABLE KEYS */;
/*!40000 ALTER TABLE `pdfs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quiz_id` int NOT NULL,
  `question_text` text NOT NULL,
  `option_a` text NOT NULL,
  `option_b` text NOT NULL,
  `option_c` text NOT NULL,
  `option_d` text NOT NULL,
  `correct_answer` char(1) NOT NULL,
  `source_document_id` int NOT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_questions_quiz_id` (`quiz_id`),
  KEY `fk_questions_source_document_id` (`source_document_id`),
  CONSTRAINT `fk_questions_quiz_id` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`),
  CONSTRAINT `fk_questions_source_document_id` FOREIGN KEY (`source_document_id`) REFERENCES `documents` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quizzes`
--

DROP TABLE IF EXISTS `quizzes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quizzes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_quizzes_user_id` (`user_id`),
  CONSTRAINT `fk_quizzes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quizzes`
--

LOCK TABLES `quizzes` WRITE;
/*!40000 ALTER TABLE `quizzes` DISABLE KEYS */;
/*!40000 ALTER TABLE `quizzes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tags_user_id` (`user_id`),
  CONSTRAINT `fk_tags_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `avatar_url` tinytext,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'john@gmail.com','john','John',NULL,'2025-10-13 18:00:00',NULL),(2,'mary@gmail.com','mary','Mary',NULL,'2025-10-22 18:00:00',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-07 18:17:06

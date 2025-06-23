# ArrayList vs LinkedList in Java - Sample Study Notes

## 1. Introduction

Java provides two commonly used List implementations:

- **ArrayList**: Backed by a dynamic array.
- **LinkedList**: Backed by a doubly linked list.

Both implement the `List` interface, but have different performance characteristics.

---

## 2. ArrayList

### Features

- Stores elements in a **resizable array**
- Allows **random access** with `O(1)` time
- Slower for insertion/removal at arbitrary positions (`O(n)`)

### Example
```java
ArrayList<String> list = new ArrayList<>();
list.add("A");
list.add("B");
list.get(1);        // Returns "B"
list.remove(0);     // Removes "A"
````

### Time Complexity

| Operation              | Time               |
| ---------------------- | ------------------ |
| Access by index        | O(1)               |
| Insert/remove at end   | O(1)\* (amortized) |
| Insert/remove at index | O(n)               |
| Search                 | O(n)               |

---

## 3. LinkedList

### Features

* Uses a **doubly linked list** structure
* Efficient insertion and deletion at both ends
* **No random access** — traversal required

### Example

```java
LinkedList<String> list = new LinkedList<>();
list.add("X");
list.add("Y");
list.get(1);        // Returns "Y" (traverse from head)
list.removeFirst(); // Removes "X"
```

### Time Complexity

| Operation                  | Time |
| -------------------------- | ---- |
| Access by index            | O(n) |
| Insert/remove at head/tail | O(1) |
| Insert/remove at index     | O(n) |
| Search                     | O(n) |

---

## 4. Key Differences

| Feature             | ArrayList               | LinkedList                    |
| ------------------- | ----------------------- | ----------------------------- |
| Memory              | Contiguous              | Non-contiguous (node objects) |
| Access speed        | Fast (O(1))             | Slow (O(n))                   |
| Insert/delete start | Slow (O(n))             | Fast (O(1))                   |
| Insert/delete end   | Fast (O(1)) (amortized) | Fast (O(1))                   |
| Insert in middle    | Slow (O(n))             | Moderate (O(n))               |
| Use case            | Frequent access         | Frequent insert/delete        |

---

## 5. When to Use What?

| Situation                                       | Recommended                     |
| ----------------------------------------------- | ------------------------------- |
| Frequent random access                          | ArrayList                       |
| Frequent insertions/deletions at ends or middle | LinkedList                      |
| Memory efficiency is important                  | LinkedList (no resizing buffer) |
| Need predictable iteration performance          | ArrayList                       |

---

## 6. Conclusion
* Use **ArrayList** when you need **fast access** and **rare inserts/removals**.
* Use **LinkedList** when you need **frequent insertions/deletions**, especially at the ends.
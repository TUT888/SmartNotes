# Time and Space Complexity – Sample Study Notes

## 1. Introduction

**Time Complexity** and **Space Complexity** are used to evaluate the efficiency of an algorithm.

- **Time Complexity**: Measures how the runtime of an algorithm increases with input size.
- **Space Complexity**: Measures how much memory an algorithm uses as input size grows.

---

## 2. Time Complexity

### Common Time Complexities

| Complexity | Name             | Example                             |
|------------|------------------|-------------------------------------|
| O(1)       | Constant time     | Accessing array element by index    |
| O(log n)   | Logarithmic       | Binary Search                       |
| O(n)       | Linear            | Traversing an array                 |
| O(n log n) | Linearithmic      | Merge Sort, Quick Sort (avg case)   |
| O(n²)      | Quadratic         | Nested loops (Bubble Sort)          |
| O(2ⁿ)      | Exponential       | Recursive Fibonacci                 |
| O(n!)      | Factorial         | Brute-force permutations            |

### Tips:
- Only the highest-order term matters (as `n` becomes large)
- Ignore constants and lower-order terms in Big-O notation

---

## 3. Space Complexity

- Includes memory for:
  - Input data
  - Variables
  - Recursion stack
  - Data structures used

### Examples:
- Iterative loop using a few variables → O(1)
- Using a new array → O(n)
- Recursive function using call stack → depends on depth of recursion

---

## 4. Best, Average, and Worst Case

| Case       | Description                                |
|------------|--------------------------------------------|
| Best       | Minimum steps the algorithm can take       |
| Average    | Expected number of steps over many inputs  |
| Worst      | Maximum steps it will take on any input    |

> **Example: Linear Search**
> - Best Case: O(1) (found at start)
> - Worst Case: O(n) (found at end or not found)

---

## 5. Practical Examples

### Example 1: Array Sum
```java
int sum(int[] a) {
    int total = 0;
    for (int i = 0; i < a.length; i++) {
        total += a[i];
    }
    return total;
}
```

- Time: O(n)
- Space: O(1)

### Example 2: Recursive Factorial
```java
int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}
```
- Time: O(n)
- Space: O(n) (recursion stack)

## 6. Summary
- Use Big-O to express algorithm growth rate
- Focus on scalability as input size grows
- Optimize for both time and space depending on problem constraints
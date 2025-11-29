public class PerformanceBenchmark {

    private static final int[] SIZES = {100, 1000, 10000};

    public static void main(String[] args) {
        testDoublyLinkedList();
        System.out.println();
        testBSTOperations();
        System.out.println();
        testTraversals();
        System.out.println();
        compareWithJavaCollections();
    }

    private static void testDoublyLinkedList() {
        System.out.println("---- Doubly Linked List Performance ----");

        for (int n : SIZES) {
            DoublyLinkedList<Integer> list = new MyDoublyLinkedList<>();

            long startInsert = System.nanoTime();
            for (int i = 0; i < n; i++) {
                list.addLast(i);
            }
            long insertTime = System.nanoTime() - startInsert;

            long startSearch = System.nanoTime();
            for (int i = 0; i < n; i++) {
                list.contains(i);
            }
            long searchTime = System.nanoTime() - startSearch;

            long startDelete = System.nanoTime();
            while (!list.isEmpty()) {
                list.removeFirst();
            }
            long deleteTime = System.nanoTime() - startDelete;

            System.out.println("n=" + n +
                    " | insert=" + toMs(insertTime) +
                    "ms | search=" + toMs(searchTime) +
                    "ms | delete=" + toMs(deleteTime) + "ms");
        }
    }

    private static void testBSTOperations() {
        System.out.println("---- BST Insert/Search/Delete ----");

        for (int n : SIZES) {
            BinarySearchTree<Integer> tree = new MyBinarySearchTree<>();

            long startInsert = System.nanoTime();
            for (int i = 0; i < n; i++) {
                tree.insert(i);
            }
            long insertTime = System.nanoTime() - startInsert;

            long startSearch = System.nanoTime();
            for (int i = 0; i < n; i++) {
                tree.contains(i);
            }
            long searchTime = System.nanoTime() - startSearch;

            long startDelete = System.nanoTime();
            for (int i = 0; i < n; i++) {
                tree.delete(i);
            }
            long deleteTime = System.nanoTime() - startDelete;

            System.out.println("n=" + n +
                    " | insert=" + toMs(insertTime) +
                    "ms | search=" + toMs(searchTime) +
                    "ms | delete=" + toMs(deleteTime) + "ms");
        }
    }

    private static void testTraversals() {
        System.out.println("---- BST Traversal Times ----");

        for (int n : SIZES) {
            BinarySearchTree<Integer> tree = new MyBinarySearchTree<>();
            for (int i = 0; i < n; i++) {
                tree.insert(i);
            }

            long t1 = System.nanoTime();
            tree.inOrderTraversal();
            long inOrder = System.nanoTime() - t1;

            long t2 = System.nanoTime();
            tree.preOrderTraversal();
            long preOrder = System.nanoTime() - t2;

            long t3 = System.nanoTime();
            tree.postOrderTraversal();
            long postOrder = System.nanoTime() - t3;

            long t4 = System.nanoTime();
            tree.levelOrderTraversal();
            long levelOrder = System.nanoTime() - t4;

            System.out.println("n=" + n +
                    " | inOrder=" + toMs(inOrder) +
                    "ms | preOrder=" + toMs(preOrder) +
                    "ms | postOrder=" + toMs(postOrder) +
                    "ms | level=" + toMs(levelOrder) + "ms");
        }
    }

    private static void compareWithJavaCollections() {
        System.out.println("---- Comparison with Java Collections ----");

        for (int n : SIZES) {
            DoublyLinkedList<Integer> myList = new MyDoublyLinkedList<>();
            java.util.LinkedList<Integer> javaList = new java.util.LinkedList<>();

            long t1 = System.nanoTime();
            for (int i = 0; i < n; i++) myList.addLast(i);
            long myDLL = System.nanoTime() - t1;

            long t2 = System.nanoTime();
            for (int i = 0; i < n; i++) javaList.addLast(i);
            long jDLL = System.nanoTime() - t2;

            BinarySearchTree<Integer> myTree = new MyBinarySearchTree<>();
            java.util.TreeSet<Integer> javaTree = new java.util.TreeSet<>();

            long t3 = System.nanoTime();
            for (int i = 0; i < n; i++) myTree.insert(i);
            long myBSTinsert = System.nanoTime() - t3;

            long t4 = System.nanoTime();
            for (int i = 0; i < n; i++) javaTree.add(i);
            long javaBSTinsert = System.nanoTime() - t4;

            long t5 = System.nanoTime();
            for (int i = 0; i < n; i++) myTree.contains(i);
            long myBSTsearch = System.nanoTime() - t5;

            long t6 = System.nanoTime();
            for (int i = 0; i < n; i++) javaTree.contains(i);
            long javaBSTsearch = System.nanoTime() - t6;

            System.out.println("n=" + n);
            System.out.println("DLL addLast: mine=" + toMs(myDLL) +
                    "ms, java=" + toMs(jDLL) + "ms");
            System.out.println("BST insert: mine=" + toMs(myBSTinsert) +
                    "ms, java=" + toMs(javaBSTinsert) + "ms");
            System.out.println("BST search: mine=" + toMs(myBSTsearch) +
                    "ms, java=" + toMs(javaBSTsearch) + "ms");
            System.out.println();
        }
    }

    private static double toMs(long nanos) {
        return nanos / 1_000_000.0;
    }
}

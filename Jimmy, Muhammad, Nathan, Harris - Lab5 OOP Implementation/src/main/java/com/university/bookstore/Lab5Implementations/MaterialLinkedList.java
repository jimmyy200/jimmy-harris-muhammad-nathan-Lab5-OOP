package com.university.bookstore.Lab5Implementations;

public class MaterialLinkedList<T> implements DoublyLinkedList<T>{
    private Node<T> head;
    private Node<T> tail;
    private int size;

    /** 
     * Returns the size of the DoublyLinkedList
     * @return size
     */
    @Override
    public int size(){
        return size;
    }
    
    /** 
     * Constructs a Doubly Linked List if no arguments are provided
     */
    public MaterialLinkedList(){
        this.head = null;
        this.tail = null;
        size = 0;
    }

    /** 
     * Constructs a Doubly Linked List with a Node if provided
     * @param first Node inputted as an argument to the constructor
     */
    public MaterialLinkedList(Node first){
        this.head = first;
        this.tail = first;
        size = 1;
    }

    /**
     * Constructs a Doubly Linked List with a data type if provided
     * @param data  generic T data that accepts any type
     */
    public MaterialLinkedList(T data){ 
        Node newNode = new Node(data);
        this.head = newNode;
        this.tail = newNode;
        size = 1;
    }
    /**
     * Will turn the input into a node and then add it to the head of the linked list
     * @param data generic T data that accepts any type
     */
    @Override
    public void addFirst(T data){
        Node<T> newNode = new Node<>(data);
        if (head != null){
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        else {
            head = newNode;
        }
        if (tail == null){
            tail = newNode;
        }
        size++;
    }

    /**
     * Inserts a node into the head of the linked list
     * @param newNode Input Node
     */
    @Override
    public void addFirst(Node<T> newNode){
        if (head != null){
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        else {
            head = newNode;
        }
        if (tail == null){
            tail = newNode;
        }
        size++;
    }

    /**
     * turn the data into a node and insert it at the tail
     * @param data generic T data that accepts any type
     */
    @Override
    public void addLast(T data){
        Node<T> newNode = new Node<>(data);
        if (tail != null){
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        else {
            tail = newNode;
        }
        if (head == null){
            head = newNode;
        }
        size++;
    }
    /**
     * Inserts a node into the tail of the linked list
     * @param newNode Input Node
     */
    @Override
    public void addLast(Node<T> newNode){
        if (tail != null){
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        else {
            tail = newNode;
        }
        if (head == null){
            head = newNode;
        }
        size++;
    }

    /**
     * Given an valid index, the function will turn the data into a node and insert it at the index
     * @param index index value has to be less than size and greater than 0
     * @param data generic T data
     */
    @Override
    public void insertAt(int index, T data){
        Node<T> newNode = new Node<>(data);
        if (index > size || index < 0){
            throw new Exception("Index out of bounds");
        }
        else if (size == index){
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        else {
            Node crNode = head;
            for (int x = 0; x < index; x++){
                crNode = crNode.next;
            }
            Node prevNode = crNode.prev;
            crNode.prev = newNode;
            newNode.next = crNode;
            prevNode.next = newNode;
            newNode.prev = prevNode;
        }
        size++;
    }
    /**
     * Given a valid index, the function will insert a node at the index
     * @param index must be a valid index
     * @param newNode Input Node
     */
    @Override
    public void insertAt(int index, Node newNode){
        if (index > size || index < 0){
            throw new Exception("Index out of bounds");
        }
        else if (size == index){
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        else {
            Node crNode = head;
            for (int x = 0; x < index; x++){
                crNode = crNode.next;
            }
            Node prevNode = crNode.prev;
            crNode.prev = newNode;
            newNode.next = crNode;
            prevNode.next = newNode;
            newNode.prev = prevNode;
        }
        size++;
    }
    /**
     * Removes the first Node in the linked list, and returns the value stored within it
     * @return data within the Node
     */
    @Override
    public T removeFirst(){
        if (head == null){
            throw new Exception("There is no Node in the list");
        }
        T returnValue = head.data;
        if (head.next == null){
            head.data = null;
            head = null;
        }
        else {
            Node nextNode = head.next;
            head.next = null;
            head.data = null;
            head = nextNode;
            nextNode.prev = null;
        }
        size--;
        return returnValue;
    }

    /**
     * Removes the last node in the linked list, and returns the data stored within it
     * @return data stored within Node
     */
    @Override
    public T removeLast(){
        if (tail == null){
            throw new Exception("There is no Node in the list");
        }
        T returnValue = tail.data;
        if (tail.prev == null){
            tail.data = null;
            tail = null;
        }
        else {
            Node prevNode = tail.prev;
            tail.prev = null;
            tail.data = null;
            tail = prevNode;
            prevNode.next = null;
        }
        size--;
        return returnValue;
    }

    /**
     * Given a valid index, the function will remove the node at the index, and return the data stored within it
     * @param index
     * @return data within Node
     */
    @Override
    public T removeAt(int index){
        if (index == 0){
            return removeFirst();
        }
        if (index == size - 1){
            return removeLast();
        }
        Node crNode = head.next;
        for (int x = 0; x < index; x++){
            if (crNode == null){
                throw new Exception("Index out of bounds");
            }
            crNode = crNode.next;
        }
        crNode.prev.next = crNode.next;
        crNode.next.prev = crNode.prev;
        crNode.prev = null;
        crNode.next = null;
        T returnValue = crNode.data;
        crNode.data = null;
        crNode = null;
        size--;
        return returnValue;
    }

    /**
     * returns the data within the first node
     * @return Head Node Data
     */
    @Override
    public T getFirst(){
        if (head == null){
            throw new Exception("There is no head node");
        }
        else {
            return head.data;
        }
    }

    /**
     * Returns the data within the tail Node
     * @return Tail Node Data
     */
    @Override
    public T getLast(){
        if (tail == null){
            throw new Exception("There is no tail node");
        }
        else {
            return tail.data;
        }
    }

    /**
     * given a valid index, the function will return the data stored within the Node at the index
     * @param index valid Index
     * @return Node[index] Data
     */
    @Override
    public T getAt(int index){
        if (index >= size){
            throw new Exception("Index out of bounds");
        }
        else if (index == size - 1){
            return tail.data;
        }
        else {
            Node crNode = head;
            for (int x = 0; x < index; x++){
                crNode = crNode.next;
            }
            return crNode.data;
        }
    }

    /**
     * checks the size value and the value of head and tail node to see if linked list is empty
     * @return True or False
     */
    @Override
    public boolean isEmpty(){
        if (size == 0 && head == null && tail == null){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Goes over each node in the linked list and clears its value to set it up for garbage collection
     */
    @Override
    public void clear(){
        Node crNode = head;
        while (crNode != null){
            Node nextNode = crNode.next;
            crNode.prev = null;
            crNode.next = null;
            crNode.data = null;
            crNode = nextNode;
        }
        head = null;
        tail = null;
        size = 0;
    }
    
    /**
     * Iterates through the linked list to see if a Node containing data exists, and if so returns true or false
     * @param data check if data is in the linked list
     * @return True or False
     */
    @Override
    public boolean contains(T data){
        Node crNode = head;
        while (crNode != null){
            if (crNode.data == data){
                return true;
            }
            crNode = crNode.next;
        }
        return false;
    }

    /**
     * iterates through the linked list, and if a node contains data, return the index of that node
     * @param data check if data is in linked list
     * @return index of Node that contains data
     */
    @Override
    public int indexOf(T data){
        Node crNode = head;
        for (int x = 0; x < size; x++){
            if (crNode.data == data){
                return x;
            }
            crNode = crNode.next;
        }
        return -1;
    }
}

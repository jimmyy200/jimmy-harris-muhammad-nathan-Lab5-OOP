package com.university.bookstore.Lab5Implementations;

import com.university.bookstore.api.DoublyLinkedList;

public class MaterialLinkedList implements DoublyLinkedList{
    private Node<T> head;
    private Node<T> tail;
    private int size;

    int size(){
        return size;
    }
    
    public MaterialLinkedList(){
        this.head = null;
        this.tail = null;
        size = 0;
    }

    public MaterialLinkedList(Node first){
        this.head = first;
        this.tail = first;
        size = 1;
    }

    public MaterialLinkedList(T data){ 
        Node newNode = new Node(data);
        this.head = newNode;
        this.tail = newNode;
        size = 1;
    }

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

    public void insertAt(int index, T data){
        Node<T> newNode = new Node<>(data);
        if (size > index){
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
            prevNode.next = crNode;
            newNode.prev = prevNode;
        }
        size++;
    }

    public void insertAt(int index, Node newNode){
        if (size > index){
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
            prevNode.next = crNode;
            newNode.prev = prevNode;
        }
        size++;
    }

    public T removeFirst(){
        if (head == null){
            throw new Exception("There is no Node in the list");
        }
        Node nextNode = head.next;
        head.next = null;
        T returnValue = head.data;
        head.data = null;
        head = nextNode;
        nextNode.prev = null;
        size--;
        return returnValue;
    }

    public T removeLast(){
        if (tail == null){
            throw new Exception("There is no Node in the list");
        }
        Node prevNode = tail.prev;
        tail.prev = null;
        T returnValue = tail.data;
        tail.data = null;
        tail = prevNode;
        prevNode.next = null;
        size--;
        return returnValue;
    }

    public T removeAt(int index){
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

    public T getFirst(){
        if (head == null){
            throw new Exception("There is no head node");
        }
        else {
            return head.data;
        }
    }

    public T getLast(){
        if (tail == null){
            throw new Exception("There is no tail node");
        }
        else {
            return tail.data;
        }
    }

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

    public boolean isEmpty(){
        if (size == 0 && head == null && tail == null){
            return true;
        }
        else {
            return false;
        }
    }

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

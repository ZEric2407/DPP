package com.example.dpp;

import java.util.ArrayList;

public class AccountList {
    private int size;
    private AccountNode head;
    private AccountNode tail;
    private static int sizeLimit = -1;
    private class AccountNode {
        AccountNode next;
        Account elmt;
        AccountNode prev;

        public AccountNode(Account acnt){
            elmt = acnt;
        }
    }
    public static void setSizeLim(int newSize){
        sizeLimit = newSize;
    }

    public AccountList() {
        size = 0;
    }

    public int getSize(){
        return size;
    }

    public Account findAccount(String name){
        AccountNode currentNode = head;
        for (int i = 0; i < size; i++){
            if (currentNode.elmt.name.equals(name)){
                return currentNode.elmt;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    public boolean accExists(String name){
        return findAccount(name) != null;
    }

    public ArrayList<Account> retrieveAll(){
        ArrayList<Account> lst = new ArrayList<>();
        AccountNode currNode = head;
        for (int i = 0; i < getSize(); i++){
            lst.add(currNode.elmt);
            currNode = currNode.next;
        }
        return lst;
    }
    public boolean addNode(Account acnt){
        if (size > sizeLimit && sizeLimit != -1){
            return false;
        }
        if (size == 0){
            head = new AccountNode(acnt);
            tail = head;
        } else {
            AccountNode newNode = new AccountNode(acnt);
            tail.next = newNode;
            newNode.prev = tail;
            tail = tail.next;
        }
        size++;
        return true;
    }

    public Account deleteNode(String name){
        AccountNode currentNode = head;
        for (int i = 0; i < size; i++){
            if (currentNode.elmt.name.equals(name)){
                if (size == 1){
                    head = null;
                    tail = null;
                } else if (currentNode.equals(head)){
                    head = head.next;
                    head.prev = null;
                } else if (currentNode.equals(tail)){
                    tail = tail.prev;
                    tail.next = null;
                }
                size--;
                return currentNode.elmt;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    public boolean clearList(){
        if (size == 0){
            return false;
        }
        AccountNode currentNode = head;
        while (!currentNode.equals(tail)){
            currentNode.prev = null;
            currentNode.elmt = null;
            currentNode = currentNode.next;
            currentNode.prev.next = null;
        }

        currentNode.elmt = null;
        head = null;
        tail = null;
        size = 0;

        return true;
    }

    public Account retrieveLast(){
        return tail.elmt;
    }

}

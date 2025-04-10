package com.example.lab3;


public class SLList {
    private static class TransactionNode {
        public Transaction item;
        public TransactionNode next;


        public TransactionNode(Transaction f, TransactionNode r) {
            this.item = f;
            this.next = r;
        }
    }

    // first item if exist is at sentinel.next
    private final TransactionNode sentinel;
    private int size; // tracks size of linked list

    public SLList(Transaction x) {
        sentinel = new TransactionNode(null, null); // sentinel is an invariant and just for reference
        sentinel.next = new TransactionNode(x, null);
        size = 1;
    }

    // add to first
    public void addFirst(Transaction x) {
        sentinel.next = new TransactionNode(x, sentinel.next);
        size += 1;
    }


    // fet N transactions
    public Transaction[] getLastNItems(int n) {
        if (n <= 0 || sentinel.next == null) {
            return new Transaction[0];
        }

        int transactionsToReturn = Math.min(n, size);
        Transaction[] result = new Transaction[transactionsToReturn];

        TransactionNode p = sentinel;

        for (int i = 0; i < transactionsToReturn && p.next != null; i++) {
            result[i] = p.next.item;
            p = p.next;
        }

        return result;
    }

}

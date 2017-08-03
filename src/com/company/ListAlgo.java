package com.company;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by tianqiliu on 2017-07-09.
 */
public class ListAlgo {

    /**
     * Given the starting nodes of two linked lists representing integers,
     * compute their sum and return it as a linked list.
     * <p>
     * Assumption: inputs are not empty
     *
     * @param l1 starting node of number1
     * @param l2 starting node of number2
     * @return the list representation of the sum
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        int firstDigit = l1.val + l2.val;
        int flag = 0;
        if (firstDigit > 9) {
            firstDigit -= 10;
            flag = 1;
        }
        ListNode start = new ListNode(firstDigit);
        ListNode end = start;
        l1 = l1.next;
        l2 = l2.next;
        while (l1 != null && l2 != null) {
            int digit = l1.val + l2.val + flag;
            boolean overflow = digit > 9;
            flag = overflow ? 1 : 0;
            digit = overflow ? digit - 10 : digit;
            end.next = new ListNode(digit);
            end = end.next;
            l1 = l1.next;
            l2 = l2.next;
        }

        ListNode l = l1 == null ? l2 : l1;
        while (l != null) {
            int digit = l.val + flag;
            boolean overflow = digit > 9;
            flag = overflow ? 1 : 0;
            digit = overflow ? digit - 10 : digit;
            end.next = new ListNode(digit);
            end = end.next;
            l = l.next;
        }

        if (flag == 1) {
            end.next = new ListNode(1);
        }

        return start;
    }

    /**
     * Delete the nth node from the end of the given linked list using just 1 pass.
     *
     * @param head
     * @param n    starting from 1
     * @return
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {

        // idea: to do this in one pass,
        // when one pointer reaches null,
        // there must be a pointer n-1 nodes behind it
        // corner case: delete the very first node
        ListNode fast = head;
        for (int i = 0; i < n; ++i) {
            fast = fast.next;
        }

        ListNode mid = head;
        ListNode slow = null;
        while (fast != null) {
            fast = fast.next;
            slow = mid;
            mid = mid.next;
        }

        if (slow == null) {
            head = head.next;
        } else {
            slow.next = mid.next;
        }
        return head;
    }

    /**
     * Merged two sorted linked lists.
     *
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {

        if (l1 == null) {
            return l2 == null ? null : l2;
        }

        if (l2 == null) {
            return l1;
        }

        //idea: find the next smaller chunk of list and let the tail pointer point to it
        ListNode l = l1.val < l2.val ? l1 : l2;
        while (l1 != null && l2 != null) {
            ListNode tail = l1;
            while (l1 != null && l1.val < l2.val) {
                tail = l1;
                l1 = l1.next;
            }

            tail.next = tail != l1 ? l2 : tail.next;
            tail = l2;
            if (l1 == null) {
                break;
            }
            while (l2 != null && l2.val < l1.val) {
                tail = l2;
                l2 = l2.next;
            }

            tail.next = l1;
        }
        return l;

    }

    /**
     * Merged two sorted linked lists (version 2).
     *
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        // idea: start with a dummy linked list which has only one node and has both a head and tail pointer
        // now expand the linked list by letting the tail pointing to the next smaller node
        ListNode h = new ListNode(0); //dummy head; h.next is the actual head
        ListNode t = h; //tail
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                t.next = l1;
                l1 = l1.next;
            } else {
                t.next = l2;
                l2 = l2.next;
            }
            t = t.next;
        }

        t.next = l1 == null ? l2 : l1;
        return h.next;
    }

    /**
     * Determine if the given linked-list has a cycle.
     *
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }

        ListNode tortoise = head;
        ListNode rabbit = head.next;
        while (rabbit != null && rabbit.next != null) {
            if (tortoise == rabbit) {
                return true;
            }
            tortoise = tortoise.next;
            rabbit = rabbit.next.next;
        }
        return false;
    }

    /**
     * Merge array of sorted lists as one lists using recursion (faster)
     *
     * @param lists
     * @return
     */
    public ListNode mergeKLists2(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }

        return mergeKLists(lists, 0, lists.length - 1);
    }
    private ListNode mergeKLists(ListNode[] lists, int start, int end) {
        if (start == end) {
            return lists[start];
        }
        int mid = (start + end) >> 1;
        ListNode l1 = mergeKLists(lists, start, mid);
        ListNode l2 = mergeKLists(lists, mid + 1, end);
        return mergeTwoLists2(l1, l2);
    }

    /**
     * Merge array of sorted lists as one lists using priority queue
     *
     * @param lists
     * @return
     */
    public ListNode mergeKLists(ListNode[] lists) {

        // idea: first add the first node of each node to priority queue
        // pop a node from the queue and add the node to linked-list
        // if the popped node has a child, add its child to the queue
        // repeat until queue is empty
        ListNode head = new ListNode(0);
        ListNode tail = head;
        PriorityQueue<ListNode> queue = new PriorityQueue<>(new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.val < o2.val ? -1 : (o1.val == o2.val ? 0 : 1);
            }
        });
        for (ListNode l : lists) {
            if (l != null) {
                queue.add(l);
            }
        }

        while (!queue.isEmpty()) {
            tail.next = queue.poll();
            tail = tail.next;
            if (tail.next != null) {
                queue.add(tail.next);
            }
        }

        return head.next;


    }

    /**
     * Given a linked list, swap every two adjacent nodes and return its head
     * using recursion (faster).
     * e.g. 1->2 -> 3->4 to 2->1 -> 4->3
     * @param head
     * @return
     */
    public ListNode swapPairs2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode n1 = head, n2 = head.next;
        n1.next = n2.next;
        n2.next = n1;
        head = n2;
        n1.next= swapPairs2(n1.next);
        return head;

    }

    /**
     * Given a linked list, swap every two adjacent nodes and return its head.
     * e.g. 1->2 -> 3->4 to 2->1 -> 4->3
     * @param head
     * @return
     */
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode n1 = head, n2 = head.next, n0;
        n1.next = n2.next;
        n2.next = n1;
        head = n2;
        n0 = n1;
        n1 = n1.next;
        while (n1 != null) {
            n2 = n1.next;
            if (n2 == null) {
                return head;
            }
            n1.next = n2.next;
            n2.next = n1;
            n0.next = n2;
            n0 = n1;
            n1 = n1.next;
        }

        return head;
    }


    /**
     * Given a list, rotate the list to the right by k places, where k is non-negative.
     * i.e. put the last node to the head of the list, and
     * do so k times
     * @param head
     * @param k
     * @return
     */
    public ListNode rotateRight(ListNode head, int k) {

        //idea: assume list is of length n
        // we found that the last k nodes are now the first k nodes of the list
        // in the original order
        // e.g. 1->2->3->4->5; k=2
        // => 1->2->3 -> 4->5
        // => 4->5->1->2->3
        if (head == null || k == 0) {
            return head;
        }

        ListNode fast = head;
        int len = 1;
        for (int i = 0; i < k && fast.next != null; ++i) {
            fast = fast.next;
            ++len;
        }
        if (fast.next == null) {
            fast = head;
            len = k % len;

            for (int i = 0; i < len; ++i) {
                fast = fast.next;
            }

        }

        ListNode slow = head;
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }

        fast.next = head;
        head = slow.next;
        slow.next = null;

        return head;

    }

    /**
     * A linked list is given such that each node contains an additional random pointer
     * which could point to any node in the list or null.
     * @param head
     * @return a deep copy of the list.
     */
    public RandomListNode copyRandomList(RandomListNode head) {
        // key: pointer to an original node
        // value: pointer to a copy containing/copying only the label of the original node
        // i.e. the values are pointers pointing to copy-nodes that will later form the new list
        HashMap<RandomListNode, RandomListNode> hashMap = new HashMap<>();
        RandomListNode itr = head;
        while (itr != null) {
            hashMap.put(itr, new RandomListNode(itr.label));
            itr = itr.next;
        }

        itr = head;
        RandomListNode copy;
        // link the copy-nodes together to form the new list
        while (itr != null) {
            copy = hashMap.get(itr); // get the copy of itr
            copy.next = hashMap.get(itr.next); // find the copy of itr's "next" and let copy's "next" point to it
            copy.random = hashMap.get(itr.random); // find the copy of itr's "random" and let copy's "random" point to it
            itr = itr.next;
        }
        return hashMap.get(head);
    }


}

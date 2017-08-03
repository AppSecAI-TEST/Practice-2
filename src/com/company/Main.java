package com.company;


public class Main {

    public static void main(String[] args) {
        ListNode l1 = new ListNode(2);
        ListNode l2 = new ListNode(1);
        ListAlgo listAlgo = new ListAlgo();
        listAlgo.mergeTwoLists(l1, l2);
        ListNode head = new ListNode(1);
        ListNode node = head;
        for (int i = 2; i < 5; ++i) {
            node.next = new ListNode(i);
            node = node.next;
        }
        listAlgo.swapPairs2(head);
        // write your code here
        testLengthOfLongestSubstring();
        SimpleAlgo algo = new SimpleAlgo();
        algo.trapRainWater(new int[]{2, 8, 5, 5, 6, 1, 7, 4, 5});
    }

    private static void testLengthOfLongestSubstring() {

        TreeAlgo treeAlgo = new TreeAlgo();
        treeAlgo.generateTrees(3);
        treeAlgo.isValidBST(new TreeNode(2147483647));

        String[] strings = new String[3];
        strings[0] = "abcabcbb";
        strings[1] = "bbbbb";
        strings[2] = "pwwkew";

        SimpleAlgo simpleAlgo = new SimpleAlgo();
        for (String s : strings) {
            int len = simpleAlgo.lengthOfLongestSubstring(s);
            System.out.println(len);
        }

        String s1 = "tmmzuxt";
        int len = simpleAlgo.lengthOfLongestSubstring(s1);
        System.out.println(len);

        int[] nums1 = {1, 3};
        int[] nums2 = {2, 4, 5};
        System.out.println(simpleAlgo.findMedianSortedArrays(nums1, nums2));

        String s = "abb";
        System.out.println(simpleAlgo.longestPalindrome2(s));

        s = "ABCDEF";
        System.out.println(simpleAlgo.convert(s, 3));

        System.out.println(simpleAlgo.reverse(1534236469));

        System.out.println(simpleAlgo.myAtoi("1"));

        simpleAlgo.isPalindrome(0);
        System.out.println(simpleAlgo.maxArea(new int[]{2, 3, 4, 5, 18, 17, 6}));

        simpleAlgo.threeSum(new int[]{0, 0, 0, 0});
        simpleAlgo.letterCombinations("999");
    }
}

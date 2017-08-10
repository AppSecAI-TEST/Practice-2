package com.company;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by tianqiliu on 2017-05-31.
 */
public class SimpleAlgo {

    /**
     * @param s The string to be parsed
     * @return the length of the longest substring containing non-repeating char in s
     */
    public int lengthOfLongestSubstring(String s) {

        if (s == null || s.equals("")) {
            return 0;
        }
        int sLen = s.length();
        int longLen = 0;
        int tempLen = 0;
        int recentRepeat = 0;
        HashMap<Character, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < sLen; ++i) {
            Character c = s.charAt(i);
            Integer pos = hashMap.get(c);
            if (pos == null) {
                hashMap.put(c, i);
                ++tempLen;
            } else {
                recentRepeat = pos > recentRepeat ? pos : recentRepeat;
                tempLen = i - recentRepeat;
                hashMap.replace(c, i);

            }
            longLen = tempLen > longLen ? tempLen : longLen;
        }
        return longLen;
    }

    /**
     * Given an array of integers and a sum,
     * find the indices of two numbers in that array which sum to the given sum.
     * <p>
     * Assumption: array is not null and may contain duplicates
     *
     * @param nums   array of numbers which may repeat
     * @param target the sum to which two elements in the array sum
     * @return the indices of the two numbers that sum to target
     */
    public int[] twoSum(int[] nums, int target) {

        /*
         * Key idea:
         * Suppose target = 9, and there are 2 & 7 in the array.
         * During the initialization of the hash-table,
         * if we encounter 2 first, at the time we encounter 2, we cannot find 7 in the hash-table.
         * But later, when we encounter 7, we will find 2 in the hash-table because it is already stored.
         */

        //keys are number in the array
        //values are their first occurrence
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        int len = nums.length;
        for (int i = 0; i < len; ++i) {
            int diff = target - nums[i];
            Integer index = hashMap.get(diff);

            if (index != null) {
                return new int[]{index, i};
            }

            hashMap.put(nums[i], i);
        }

        return null;

    }


    /**
     * Find the median of the all the numbers in both given arrays
     *
     * @param nums1 sorted array
     * @param nums2 sorted array
     * @return the median of the {nums1[:], nums2[;]}
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        // idea: let i denote the position where we cut nums1 into two halves
        //      and let j denote the position where we cut nums2 into two halves
        // i.e.
        // nums1[0...i] | nums1[i+1...]
        // nums2[0...j] | nums2[j+1...]
        // we want to make sure:
        // 1. max(nums1[0...i], nums2[0...j]) <= min(nums1[i+1...],nums2[j+1...])
        // 2. len(nums1[0...i]) + len(nums2[0...j]) = len(nums1[i+1...]) + len(nums2[j+1])

        if (nums1.length == 0) {
            return findMedianSortedArray(nums2);
        }

        if (nums2.length == 0) {
            return findMedianSortedArray(nums1);
        }

        if (nums1.length == 1 && nums2.length == 1) {
            //cur nums2
            return (nums1[0] + nums2[0]) / 2.0;
        }

        if (nums1.length == 1) {
            return findMedian(nums2, nums1[0]);
        }

        if (nums2.length == 1) {
            return findMedian(nums1, nums2[0]);
        }


        int[] a = nums1.length < nums2.length ? nums1 : nums2;
        int[] b = nums1.length < nums2.length ? nums2 : nums1;
        int lenA = a.length, lenB = b.length;

        if (a[0] >= b[lenB - 1]) { //entire a[] >= b[]
            int len = lenA + lenB;
            if ((len & 1) == 0) {
                if (lenA == lenB) {
                    return (b[lenB - 1] + a[0]) / 2.0;
                }
                return (b[len >> 1] + b[(len >> 1) - 1]) / 2.0;
            }
            return b[len >> 1];
        } else if (a[lenA - 1] <= b[0]) {
            int len = lenA + lenB;
            if ((len & 1) == 0) {
                if (lenA == lenB) {
                    return (a[lenA - 1] + b[0]) / 2.0;
                }
                return (b[(len >> 1) - lenA] + b[(len >> 1) - 1 - lenA]) / 2.0;
            }

            return b[(len >> 1) - lenA];
        }

        int i = 0, j = lenA - 1;
        int cutA = j >> 1, cutB = (lenA & 1) == 1 && (lenB & 1) == 1 ? ((lenB - 1) >> 1) - 1 : (lenB - 1) >> 1;

        while (i < j) {

            int leftMax = Math.max(a[cutA], b[cutB]);
            int rightMin = Math.min(a[cutA + 1], b[cutB + 1]);

            //base case
            if (leftMax <= rightMin) {

                if (((lenA + lenB) & 1) == 0) { //even length
                    return (leftMax + rightMin) / 2.0;
                }
                return leftMax;
            }

            if (a[cutA] < b[cutB]) {
                //cutA further right
                i = cutA + 1;
                cutA = (i + j) >> 1;
                cutA = cutA == i - 1 ? cutA + 1 : cutA;
                cutB -= cutA - i + 1;
            } else {
                //cutA futher left
                j = cutA;
                cutA = (i + j) >> 1;
                cutA = cutA == j ? cutA - 1 : cutA;
                cutB += j - cutA;
            }
        }

        if (i == lenA - 1) {
            return ((lenA + lenB) & 1) == 0 ? (Math.max(a[cutA], b[cutB]) + b[cutB + 1]) / 2.0 : Math.max(a[cutA], b[cutB]);
        }
        return ((lenA + lenB) & 1) == 0 ? (Math.min(a[cutA + 1], b[cutB + 1]) + b[cutB]) / 2.0 : Math.min(a[cutA + 1], b[cutB]);
    }

    private double findMedianSortedArray(int[] nums) {
        try {
            int mid = (nums.length - 1) >> 1;
            return (nums.length & 1) == 0 ? (nums[mid] + nums[mid + 1]) / 2.0 : nums[mid];
        } catch (Exception e) {
            return nums[0];
        }

    }

    private double findMedian(int[] nums, int num) {
        int start = 0, end = nums.length - 1;
        int mid = (start + end) >> 1;
        if ((nums.length & 1) == 0) { //odd length in total
            if (num <= nums[mid]) {
                return nums[mid];
            } else {
                return nums[mid + 1] < num ? nums[mid + 1] : num;
            }
        } else { //even length in total
            if (num <= nums[mid]) {
                return num >= nums[mid - 1] ? (num + nums[mid]) / 2.0 : (nums[mid - 1] + nums[mid]) / 2.0;
            } else {
                return num <= nums[mid + 1] ? (nums[mid] + num) / 2.0 : (nums[mid] + nums[mid + 1]) / 2.0;
            }
        }

    }

    /**
     * Find a longest palindrome in the given string s
     * using expand around centre approach
     *
     * @param s
     * @return a long palindrome
     */
    public String longestPalindrome(String s) {
//        if (s == null || s.isEmpty()) {
//            return "";
//        }
        char[] c = s.toCharArray();
        int[] window1 = expandAroundSingleCentre(c);
        if (s.length() > 1) {
            int[] window2 = expandAroundDoubleCentre(c);
            return (window1[1] - window1[0]) < (window2[1] - window2[0]) ?
                    s.substring(window2[0], window2[1]) :
                    s.substring(window1[0], window1[1]);
        }

        return s.substring(window1[0], window1[1]);

    }

    private int[] expandAroundSingleCentre(char[] c) {
        int sLen = c.length;
        int pLen = 0;
        int pStart = 0, pStop = 0;
        for (int i = 1; i < sLen; ++i) {
            for (int start = i - 1, stop = i + 1; start > -1 && stop < sLen; --start, ++stop) {
                if (c[start] == c[stop]) {
                    int thisLen = stop - start;
                    if (thisLen > pLen) {
                        pLen = thisLen;
                        pStart = start;
                        pStop = stop;
                    }
                } else {
                    break;
                }
            }
        }

//        return s.substring(pStart, pStop + 1);
        return new int[]{pStart, pStop + 1};
    }

    private int[] expandAroundDoubleCentre(char[] c) {
        int sLen = c.length;
        int pLen = 0;
        int pStart = 0, pStop = 0;
        int dStart = 0, dStop = 0;
        for (int i = 0, j = 1; j < sLen; ++i, ++j) {
            if (c[i] == c[j]) {
                dStart = i;
                dStop = j;
                for (int start = i - 1, stop = j + 1; start > -1 && stop < sLen; --start, ++stop) {
                    if (c[start] == c[stop]) {
                        int thisLen = stop - start;
                        if (thisLen > pLen) {
                            pLen = thisLen;
                            pStart = start;
                            pStop = stop;
                        }
                    } else {
                        break;
                    }
                }
            }

        }
//        return pStop > 0 ? s.substring(pStart, pStop + 1) : s.substring(dStart, dStop + 1);
        return pStop > 0 ? new int[]{pStart, pStop + 1} : new int[]{dStart, dStop + 1};

    }

    /**
     * Find the longest palindrome in the given string s
     * using dynamic programming
     *
     * @param s
     * @return
     */
    public String longestPalindromeDP(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }


        //idea:
        //s[i...j] is palindrome if
        //s[i+1...j-1] is palindrome and s[i] == s[j]

        int sLen = s.length();
        int sLen_1 = sLen - 1;
        boolean[][] t = new boolean[sLen][sLen];

        //initialization
        for (int i = 0; i < sLen_1; ++i) {
            t[i][i] = true;
            t[i][i + 1] = s.charAt(i) == s.charAt(i + 1);
        }
        t[sLen_1][sLen_1] = true;

        //main loop
        int sLen_2 = sLen - 2;
        for (int col = 2; col < sLen_1; col += 2) {
            int iStop = sLen - col - 1;
            int i = 0, j = col;
            while (i < iStop) {
                t[i][j] = t[i + 1][j - 1] && (s.charAt(i) == s.charAt(j));
                t[i][j + 1] = t[i + 1][j] && (s.charAt(i) == s.charAt(j + 1));
                ++i;
                ++j;
            }
            t[i][sLen_1] = t[i + 1][sLen_2] && (s.charAt(i) == s.charAt(sLen_1));
        }

        if ((sLen & 1) != 0 && sLen > 1) { // corner case for odd length
            t[0][sLen_1] = t[1][sLen_2] && (s.charAt(0) == s.charAt(sLen_1));

        }
        return traceBack(t, s);
    }

    private String traceBack(boolean[][] t, String s) {
        int pLen = 0, pStart = 0, pStop = 0, sLen = t.length;
        for (int i = 0; i < sLen; ++i) {
            if ((sLen - i - 1) > pLen) {
                for (int j = sLen - 1; j > -1; --j) {
                    if (t[i][j]) {
                        if ((j - i) > pLen) {
                            pLen = j - i;
                            pStart = i;
                            pStop = j;
                        }
                        break;
                    }
                }
            }

        }

        return s.substring(pStart, pStop + 1);
    }

    /**
     * Find the longest palindrome in given string s
     * using shrinking window approach
     *
     * @param s
     * @return
     */
    public String longestPalindrome2(String s) {
        int len = s.length();
//        if (len < 2) {
//            return s;
//        }
        char[] c = s.toCharArray();
        // basic idea:
        // starts with whole string,
        // and recursively shrink window and sweep through window
        // to see if the current window is a palindrome
        for (int window_1 = len - 1; window_1 > -1; --window_1) {
            for (int i = 0; i + window_1 < len; ++i) {
                if (isPalindrome(c, i, i + window_1)) {
                    return s.substring(i, i + window_1 + 1);
                }
            }
        }
        return s;
    }

    private boolean isPalindrome(char[] c, int startIndex, int uptoIndex) {
        while (startIndex < uptoIndex) {
            if (c[startIndex] != c[uptoIndex]) {
                return false;
            }
            ++startIndex;
            --uptoIndex;
        }
        return true;
    }


    /**
     * Find the given string s in zig-zag order
     *
     * @param s
     * @param numRows number of rows zig-zag representation will have
     * @return s in zig-zag order
     */
    public String convert(String s, int numRows) {
        int len = s.length();
        if (len <= numRows || numRows < 2) {
            return s;
        }

        char[] c = s.toCharArray();
        StringBuilder[] sb = new StringBuilder[numRows];
        for (int i = 0; i < numRows; ++i) {
            sb[i] = new StringBuilder();
        }

        int offC = 0;
        while (offC < len) {
            //vertical
            int i = 0;
            while (i < numRows && offC < len) {
                sb[i++].append(c[offC++]);
            }

            //oblique
            i -= 2;
            while (i > 0 && offC < len) {
                sb[i--].append(c[offC++]);
            }
        }

        //return
        int i = 1;
        while (i < numRows) {
            sb[0].append(sb[i++]);
        }

        return sb[0].toString();
    }


    /**
     * Given a integer x, reverse it.
     * If the reverse overflows, return 0
     *
     * @param x
     * @return the reverse of x
     */
    public int reverse(int x) {

        //remove trailing 0
//        int remainder = x % 10;
//        while (remainder == 0) {
//            x /= 10;
//            remainder = x % 10;
//        }

        int y = 0;
        int yy = 0;
        while (x != 0) {
            yy = y;
            y = y * 10 + x % 10;
            x /= 10;
        }

        return y / 10 == yy ? y : 0;
    }

    /**
     * Implementation of atoi
     *
     * @param str
     * @return
     */
    public int myAtoi(String str) {
        if (str.isEmpty()) {
            return 0;
        }
        str = str.trim();
        char[] c = str.toCharArray();

        boolean negative = c[0] == '-';
        int i = negative || c[0] == '+' ? 1 : 0;
        int num = 0, prevNum = 0;
        while (i < c.length && prevNum == num / 10) {
            prevNum = num;
            if (c[i] >= '0' && c[i] <= '9') {
                num = num * 10 + (c[i] - '0');
            } else if (i == 0) { //NaN
                return 0;
            } else {
                break;
            }
            ++i;
        }

        return negative ?
                (prevNum == num / 10 || prevNum == num ? -num : Integer.MIN_VALUE) :
                (prevNum == num / 10 || prevNum == num ? num : Integer.MAX_VALUE);
    }

    /**
     * Determine if the given integer x is palindrome
     *
     * @param x
     * @return
     */
    public boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0)) {
            return x == 0;
        }
        int y = 0;
        while (x > y) {
            y = y * 10 + x % 10;
            x /= 10;
        }

        return (x == y) || (x == y / 10);
    }


    /**
     * Determine if the string s matches the regex p
     * which only supports "*" and "."
     *
     * @param s string to match
     * @param p regex to match against
     * @return
     */
    public boolean isMatch(String s, String p) {
        String[] haha = p.split("\\*");
        return false;
    }


    /**
     * Given n non-negative integers a1, a2, ..., an, where each represents a point at coordinate (i, ai).
     * n vertical lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0).
     * Find two lines, which together with x-axis forms a container, such that the container contains the most water.
     *
     * @param height
     * @return the maximum water contained
     */
    public int maxArea(int[] height) {
        int len = height.length;
        int i = 0, j = len - 1, maxArea = 0;

        while (i < j) {
            boolean leftLower = height[i] < height[j];
            int area = (j - i) * (leftLower ? height[i] : height[j]);
            maxArea = maxArea < area ? area : maxArea;
            i = leftLower ? i + 1 : i;
            j = leftLower ? j : j - 1;
        }

        return maxArea;
    }

    public int maxArea2(int[] height) {
        int len = height.length;
        int i = 0, j = len - 1, maxArea = 0;

        while (i < j) {
            int minHeight = height[i] < height[j] ? height[i] : height[j];
            int area = (j - i) * minHeight;
            maxArea = maxArea < area ? area : maxArea;
            while (height[i] <= minHeight && i < j) {
                ++i;
            }
            while (height[j] < minHeight) {
                ++j;
            }

        }

        return maxArea;
    }

    /**
     * Find the longest common prefix among strings
     * in strs[]
     *
     * @param strs
     * @return the longest common prefix
     */
    public String longestCommonPrefix(String[] strs) {

        char[] prefix;
        try {
            prefix = strs[0].toCharArray();
        } catch (Exception e) {
            return "";
        }

        int upto = prefix.length;

        for (int i = 1; i < strs.length && upto > 0; ++i) {


            int j = 0;
            int tempLen = 0; //records how long are the two strings under comparsion in common
            int len = Math.min(upto, strs[i].length());
            char[] str = strs[i].toCharArray();

            while (j < len) {
                if (prefix[j] == str[j]) {
                    ++tempLen;
                } else {
                    break;
                }
                ++j;
            }

            upto = upto < tempLen ? upto : tempLen;

        }

        return new String(prefix, 0, upto);
    }

    /**
     * Find a list of 3 numbers that sum to 0 in the given array
     * with each list of 3 must be unique
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {

        List<List<Integer>> lists = new ArrayList<>();

        Arrays.sort(nums);
        int iStop = nums.length - 2;
        for (int i = 0; i < iStop; ++i) {
            while (i > 0 && nums[i] == nums[i - 1] && i < iStop) { //escape duplicates
                ++i;
            }


            int target = -nums[i];
            int j = i + 1, k = nums.length - 1;
            while (j < k) {

                int residue = nums[j] + nums[k];
                if (target == residue) {
                    lists.add(Arrays.asList(nums[i], nums[j], nums[k]));

                    while (j < k && nums[j] == nums[j + 1]) { //escape duplicates
                        ++j;
                    }

                    while (j < k && nums[k] == nums[k - 1]) { //escape duplicates
                        --k;
                    }
                    ++j;
                    --k;
                } else {
                    j = target < residue ? j : j + 1;
                    k = target < residue ? k - 1 : k;
                }


            }
        }
        return lists;
    }

    /**
     * Given a string digits, find all combinations of alphabets
     * can be represented by these digits.
     * '0' -> ''
     * '1' -> ''
     * '2' -> 'a', 'b', 'c'
     * ... like keypad on old cellphone
     *
     * @param digits
     * @return
     */
    public List<String> letterCombinations(String digits) {
        if (digits.isEmpty()) {
            return new ArrayList<>();
        }
        char[][] letters = {
                {'a', 'b', 'c'},
                {'d', 'e', 'f'},
                {'g', 'h', 'i'},
                {'j', 'k', 'l'},
                {'m', 'n', 'o'},
                {'p', 'q', 'r', 's'},
                {'t', 'u', 'v'},
                {'w', 'x', 'y', 'z'}
        };

        char[] nums = digits.toCharArray();

        int i = 0;
        while ((nums[i] == '0' || nums[i] == '1') && i < nums.length) {
            ++i;
        }


        char[] seed = letters[nums[i] - '2'];
        String[] combo = new String[seed.length];

        for (int j = 0; j < seed.length; ++j) {
            combo[j] = String.valueOf(seed[j]);
        }

        while (++i < nums.length) {
            if (nums[i] != '1' && nums[i] != '0') {
                char[] newletters = letters[nums[i] - '2'];
                String[] newCombo = new String[combo.length * newletters.length];
                for (int j = 0; j < combo.length; ++j) {
                    for (int k = 0; k < newletters.length; ++k) {
                        newCombo[newletters.length * j + k] = combo[j] + String.valueOf(newletters[k]);
                    }
                }

                combo = newCombo;
            }
        }

        return Arrays.asList(combo);


    }

    /**
     * Given a string containing just the characters '(', ')', '{', '}', '[' and ']',
     * determine if the input string is closed properly.
     *
     * @param s
     * @return
     */
    public boolean isValid(String s) {
        if (s.isEmpty()) {
            return true;
        }

        if ((s.length() & 1) != 0) {
            return false;
        }

        char first = s.charAt(0);
        char last = s.charAt(s.length() - 1);
        if (first == '}' || first == ')' || first == ']' || last == '{' || last == '(' || last == '[') {
            return false;
        }

        char[] charArray = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < charArray.length; ++i) {
            char c = charArray[i];
            switch (c) {
                case ')':
                    if (stack.pop() != '(') {
                        return false;
                    }
                    continue;
                case '}':
                    if (stack.pop() != '{') {
                        return false;
                    }
                    continue;
                case ']':
                    if (stack.pop() != '[') {
                        return false;
                    }
                    continue;
                default:
                    stack.push(c);
            }
        }
        return true;
    }

    /**
     * Given n non-negative integers representing an elevation map
     * where the width of each bar is 1,
     * compute how much water it is able to trap after raining using DP
     *
     * @param height
     * @return
     */
    public int trapRainWaterDP(int[] height) {
        if (height.length < 2) {
            return 0;
        }

        int water = 0;
        int len = height.length;
        int[] maxToLeft = new int[len]; // [i] is the maximum height to the left of bar_i
        maxToLeft[0] = height[0];
        for (int i = 1; i < len; ++i) {
            maxToLeft[i] = Math.max(height[i - 1], maxToLeft[i - 1]);
        }
        int[] maxToRight = new int[height.length]; //[i] is the maximum height to the right of bar_i
        maxToRight[len - 1] = height[len - 1];
        for (int i = len - 2; i > -1; --i) {
            maxToRight[i] = Math.max(height[i + 1], maxToRight[i + 1]);
        }

        //adding the water that bar_i can contribute
        for (int i = 1; i < len; ++i) {
            int temp = Math.min(maxToLeft[i], maxToRight[i]) - height[i];
            water += temp > 0 ? temp : 0;
        }
        return water;
    }

    /**
     * Given n non-negative integers representing an elevation map
     * where the width of each bar is 1,
     * compute how much water it is able to trap after raining.
     *
     * @param height
     * @return
     */
    public int trapRainWater(int[] height) {
        if (height.length < 2) {
            return 0;
        }
        int water = 0, leftMax = height[0], rightMax = height[height.length - 1];
        int i = 1, j = height.length - 2;
        while (i <= j) {

            // idea:
            // the water that the currently visited bar (i.e. either [i] or [j])
            // can contribute depends on the minimum of the (leftMax, rightMax)
            if (leftMax < rightMax) {
                if (leftMax < height[i - 1]) {
                    leftMax = height[i - 1];
                } else {

                    int temp = leftMax - height[i];
                    water += temp > 0 ? temp : 0;
                    ++i;
                }

            } else {
                if (rightMax < height[j + 1]) {
                    rightMax = height[j + 1];
                } else {
                    int temp = rightMax - height[j];
                    water += temp > 0 ? temp : 0;
                    --j;
                }

            }
        }
        return water;
    }

    /**
     * Rotate the image by 90 degrees (clockwise).
     *
     * @param matrix n by n matrix
     */
    public void rotateImage90(int[][] matrix) {

        // idea:
        // draw a 5 by 5 matrix before and after rotation
        // observe that (i,j) -> (j, n-i-1)
        // and to do the rotation in place
        // 4 pixels are circularly swapped at a time
        int len = matrix.length;
        int iStop = len >> 1, jStop = len - 1;
        for (int i = 0; i < iStop; --jStop, ++i) {
            for (int j = i; j < jStop; ++j) {
                int b = matrix[j][len - i - 1];
                matrix[j][len - i - 1] = matrix[i][j];
                int c = matrix[len - i - 1][len - j - 1];
                matrix[len - i - 1][len - j - 1] = b;
                int d = matrix[len - j - 1][i];
                matrix[len - j - 1][i] = c;
                matrix[i][j] = d;
            }
        }
    }

    /**
     * Rotate the image by 90 degrees (clockwise).
     *
     * @param matrix n by n matrix
     */
    public void rotate(int[][] matrix) {

        // idea:
        // 1. reflect about x-axis
        // 2. transpose
        // 1 2 3    7 8 9    7 4 1
        // 4 5 6 => 4 5 6 => 8 5 2
        // 7 8 9    1 2 3    9 6 3
        int len = matrix.length;
        for (int i = 0, j = len - 1; i < j; ++i, --j) {
            int[] top = matrix[i];
            matrix[i] = matrix[j];
            matrix[j] = top;
        }
        for (int i = 0; i < len; ++i) {
            for (int j = i + 1; j < len; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }

    /**
     * Group all anagrams
     * For example, given: ["eat", "tea", "tan", "ate", "nat", "bat"],
     * return
     * [["ate", "eat","tea"],["nat","tan"],["bat"]]
     *
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        HashMap<String, List<String>> hashMap = new HashMap<>();
        for (String s : strs) {
            char[] c = s.toCharArray();
            Arrays.sort(c);
            String temp = new String(c);
            List<String> l = hashMap.get(temp);
            if (l == null) {
                l = new ArrayList<>();
                l.add(s);
                hashMap.put(temp, l);
            } else {
                l.add(s);
            }
        }

        return new ArrayList<>(hashMap.values());
    }

    /**
     * check if two char arrays are anagram
     *
     * @param c1
     * @param c2
     * @return
     */
    public boolean isAnagram(char[] c1, char[] c2) {
        if (c1.length != c2.length) {
            return false;
        }

        HashMap<Character, Integer> hashMap = new HashMap<>();
        for (char c : c1) {
            Integer temp = hashMap.get(c);
            if (temp == null) {
                hashMap.put(c, 1);
            } else {
                hashMap.replace(c, temp.intValue() + 1);
            }
        }

        for (char c : c2) {
            Integer temp = hashMap.get(c);
            if (temp == null || temp.intValue() == 0) {
                return false;
            } else {
                hashMap.replace(c, temp.intValue() - 1);
            }
        }
        return true;
    }

    /**
     * Given a m x n matrix, if an element is 0, set its entire row and column to 0.
     * Do it in place. Only 0s in the original matrix count.
     *
     * @param matrix
     */
    public void setZeroes(int[][] matrix) {
        //denote if the first row/column should be zeros
        boolean firstRowZero = false, firstColZero = false;
        int rows = matrix.length, cols = matrix[0].length;

        //check if the first row should be zeros
        for (int j = 0; j < cols; ++j) {
            if (matrix[0][j] == 0) {
                firstRowZero = true;
                break;
            }
        }

        //check if the first column should be zeros
        for (int i = 0; i < rows; ++i) {
            if (matrix[i][0] == 0) {
                firstColZero = true;
                break;
            }
        }

        // check the remaining entries of the matrices
        // if m[i,j] is 0, row i and column j should be zeros
        // we mark this down in the first entry of row i
        // and the first entry of column j
        for (int i = 1; i < rows; ++i) {
            int[] row = matrix[i];
            for (int j = 1; j < cols; ++j) {
                if (row[j] == 0) {
                    row[0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        // set rows to zeros, but do not start with row 0
        // since doing so affects our 0-marks on columns
        for (int i = 1; i < rows; ++i) {
            if (matrix[i][0] == 0) {
                Arrays.fill(matrix[i], 0);
            }
        }

        // set columns to zeros, but do not start with column 0
        // since doing so affects our 0-marks on rows
        for (int j = 1; j < cols; ++j) {
            if (matrix[0][j] == 0) {
                for (int i = 1; i < rows; ++i) {
                    matrix[i][j] = 0;
                }
            }
        }

        // finish off the first row and column
        if (firstRowZero) {
            Arrays.fill(matrix[0], 0);
        }

        if (firstColZero) {
            for (int i = 0; i < rows; ++i) {
                matrix[i][0] = 0;
            }
        }
    }

    /**
     * Find all the subsets (including empty set) of a given set
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> lists = new ArrayList<>();
        backTrack(lists, new ArrayList<>(), nums, 0);
        return lists;
    }

    private void backTrack(List<List<Integer>> lists, List<Integer> list, int[] nums, int start) {
        lists.add(list);
        for (int i = start; i < nums.length; ++i) {
            list.add(nums[i]);
            backTrack(lists, list, nums, i + 1);
            list.remove(list.size() - 1);
        }
    }

    /**
     * get the ith row of pascal's triangle
     * where i starts at 0
     *
     * @param rowIndex
     * @return
     */
    public List<Integer> getRowOfPascal(int rowIndex) {
        int bound = rowIndex + 2;
        int[] nums = {1};
        for (int row = 2; row < bound; ++row) {
            int[] temp = new int[row];
            temp[0] = 1;
            temp[row - 1] = 1;
            int secondLast = row - 1;
            for (int i = 1; i < secondLast; ++i) {
                temp[i] = nums[i - 1] + nums[i];
            }
            nums = temp;
        }
        List<Integer> list = new ArrayList<>(nums.length);
        for (int i = 0; i < nums.length; ++i) {
            list.add(nums[i]);
        }
        return list;
    }

    public int maxProfit(int[] prices) {
        int maxProfit = 0, profitSoFar = 0;
        for (int i = 1; i < prices.length; ++i) {
            int profit = prices[i] - prices[i - 1] + profitSoFar;
            profitSoFar = profit > 0 ? profit : 0;
            maxProfit = profitSoFar > maxProfit ? profitSoFar : maxProfit;
        }
        return maxProfit;
    }

    /**
     * Given an array of prices of a stock where [i] is the price on day i
     * Find the maximum profit can be obtained by buying one stock and sell it later.
     * Dynamic Programming is used.
     *
     * @param prices
     * @return
     */
    public int maxProfitDP(int[] prices) {

        int maxProfit = 0, minBuyIn = Integer.MAX_VALUE;
        for (int i = 0; i < prices.length; ++i) {
            minBuyIn = minBuyIn < prices[i] ? minBuyIn : prices[i];
            int profit = prices[i] - minBuyIn;
            maxProfit = profit > maxProfit ? profit : maxProfit;
        }
        return maxProfit;
//        if (prices.length < 2) {
//            return 0;
//        }
//        int len = prices.length;
//        int[] maxProfitToRight = new int[len];
//        maxProfitToRight[len - 1] = prices[len - 1];
//        for (int i = len - 2; i > -1; --i) {
//            int right = maxProfitToRight[i + 1];
//            int temp = prices[i];
//            maxProfitToRight[i] = right > temp ? right : temp;
//        }
//
//        int maxProfit = 0;
//        for (int i = 0; i < len; ++i) {
//            int profit = maxProfitToRight[i] - prices[i];
//            maxProfit = profit > maxProfit ? profit : maxProfit;
//        }
//
//        return maxProfit;
    }

//    public boolean wordBreak(String s, List<String> wordDict) {
//        HashSet<String> set = new HashSet<>();
//        for (String word : wordDict) {
//            if (s.indexOf(word) > -1) {
//                set.add(word);
//            }
//        }
//        if (set.size() == 0) {
//            return false;
//        }
//        return wordBreak(s, set);
//
//    }
//
//    private boolean wordBreak(String s, HashSet<String> set) {
//        for (String word : set) {
//            int index = s.indexOf(word);
//            if (index > -1) {
//                String s1 = s.substring(0, index);
//                String s2 = s.substring(index + word.length());
//                if ((s1.length() == 0 || wordBreak(s1, set)) && (s2.length() == 0 || wordBreak(s2, set))) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    /**
     * Determine if "s" can be tokenized into strings in "wordDict".
     * Note: a token in "wordDict" can be used repeatedly
     *
     * @param s
     * @param wordDict
     * @return
     */
    public boolean wordBreak(String s, List<String> wordDict) {
        HashSet<String> hashSet = new HashSet<>(wordDict);
        int len = s.length();
        boolean[] t = new boolean[len + 1]; // t[i] indicates if s[0...i-1] can be broken into tokens
        t[0] = true; // s[-1] is true
        for (int i = 1; i < t.length; ++i) {
            for (int j = 0; j < i; ++j) {
                if (t[j] && hashSet.contains(s.substring(j, i))) {
                    t[i] = true;
                    break;
                }
            }
        }

        return t[len];

//        boolean[] t = new boolean[len]; // records if s[0...i] can be broken into dictionary
//        t[0] = hashSet.contains(s.substring(0, 1));
//        for (int i = 1; i < len; ++i) {
//            int ii = i + 1;
//            if (hashSet.contains(s.substring(0, ii))) { //corner case: s[-1...j-1 && j...i]
//                t[i] = true; //s[0...i] is good
//            } else {
//                for (int j = 1; j < ii; ++j) {
//                    //is s[0...j-1] good and s[j...i] also good?
//                    if (t[j -1] && hashSet.contains(s.substring(j, ii))) {
//                        t[i] = true;
//                        break;
//                    }
//                }
//            }
//
//        }
//
//        return t[len - 1];

    }

    /**
     * Given an array of integers that is already sorted in ascending order,
     * find non-zero based positions of two numbers that add up to a specific target number.
     * @param numbers
     * @param target
     * @return
     */
    public int[] twoSumSorted(int[] numbers, int target) {
        int i = 0, j = numbers.length - 1;
        while (i < j) {
            int sum = numbers[i] + numbers[j];
            if (sum == target) {
                return new int[]{i + 1, j + 1};
            }
            if (sum < target) {
                ++i;
            } else {
                --j;
            }
        }
        return null;
    }

    /**
     * Given a 2D grid of '1's and '0's, where '1' represents land and '0' represents water,
     * find the number of islands in the grid.
     * An island is horizontally and vertically connected lands.
     *
     * @param grid a 2D grid of land and water
     * @return number of isalnds
     */

    private int gGridN, gGridM;
    private char[][] gGrid;
    public int numIslands(char[][] grid) {

        // idea:
        // if found a land, search around it until meet a water,
        // and mark all the land connected to it as water

        if (grid.length == 0) {
            return 0;
        }
        int count = 0;
        gGridN = grid.length;
        gGridM = grid[0].length;
        gGrid = grid;
        for (int i = 0; i < gGridN; ++i) {
            for (int j = 0; j < gGridM; ++j) {
                if (grid[i][j] == '1') {
                    fadeIsland(i, j);
                    ++count;
                }
            }
        }
        return count;
    }

    private void fadeIsland(int i, int j) {
        if (i < 0 || j < 0 || i >= gGridN || j >= gGridM || gGrid[i][j] != '1') {
            return;
        }
        gGrid[i][j] = '0';

        fadeIsland(i, j - 1);
        fadeIsland(i, j + 1);
        fadeIsland(i - 1, j);
        fadeIsland(i + 1, j);

    }

    public int countPrimes(int n) {
        // ideas:
        // Do not stop at sqrt of the number i being tested.
        // Use this number to find all other numbers = i * j where i * j < n
        // i * j is clearly not a prime, so mark it and no need to test it
        boolean[] notPrimes = new boolean[n];
        int count = 0;
        int product;
        int j;
        for (int i = 2; i < n; ++i) {
            if (notPrimes[i]) {
                continue;
            }
            ++count;

            product = i << 1; //i * 2
            j = 2;
            while (product < n) {
                notPrimes[product] = true;
                product =  i * (++j);
            }
        }

        return count;
    }

}

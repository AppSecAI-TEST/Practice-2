package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by tianqiliu on 2017-07-03.
 */

public class TreeAlgo {




    /**
     * Given a binary tree, return the inorder traversal of its nodes' values
     * without using recursion.
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        ArrayList<Integer> list = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();

        TreeNode node = root;
        while (!stack.isEmpty() || node != null) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }

            TreeNode temp = stack.pop();
            list.add(temp.val);
            if (temp.right != null) {
                node = temp.right;
            }
        }

        return list;
    }

    /**
     * Given an integer n, generate all structurally unique BST's (binary search trees)
     * that store values 1...n.
     * @param n
     * @return
     */
    public List<TreeNode> generateTrees(int n) {
        if (n < 1) {
            return new ArrayList<>();
        }
        return generateTrees(1, n+1);
    }

    private List<TreeNode> generateTrees(int start, int upperBound) {

        if (start == upperBound) {
            List<TreeNode> list = new ArrayList<>();
            list.add(null);
            return list;
        }

        List<TreeNode> list = new ArrayList<>();

        for (int i = start; i < upperBound; ++i) {

            List<TreeNode> leftSubTree = generateTrees(start, i);
            List<TreeNode> rightSubTree = generateTrees(i + 1, upperBound);

            for (TreeNode left : leftSubTree) {
                for (TreeNode right : rightSubTree) {
                    TreeNode root = new TreeNode(i);
                    root.left = left;
                    root.right = right;
                    list.add(root);
                }
            }


        }
        return list;
    }

    /**
     * Given sequence 1...n each of which is a value of a node in a BST
     * find the number of all unique BSTs formed by this sequence
     * @param n
     * @return
     */
    public int numTrees(int n) {

        // [i] holds number of unique trees for 1...i
        // discard [0]
        int[] numOfTrees = new int[n+1];
        numOfTrees[1] = 1;
        numOfTrees[2] = 2;
        for (int i = 3; i < numOfTrees.length; ++i) { //[i]
            for (int j = 1; j <= i; ++j) {
                numOfTrees[i] += numOfTrees[j - 1] * numOfTrees[j-i];
            }
        }

        return numOfTrees[n];
    }

    /**
     * Given a binary tree, determine if it is a valid binary search tree (BST).
     * @param root
     * @return
     */


    private int lastVal = Integer.MIN_VALUE;
    private int comparedCount = 0;

    /**
     * Given a binary tree, determine if it is a valid binary search tree (BST).
     * @param root
     * @return
     */
    public boolean isValidBST(TreeNode root) {

        //idea: inorder traverse and should get a sorted sequence
        if (root == null) {
            return true;
        }

        boolean leftGood = isValidBST(root.left);
        if (!leftGood) {
            return false;
        }
        if (root.val <= lastVal && comparedCount != 0) {
            return false;
        }
        lastVal = root.val;
        comparedCount = 1;
        return isValidBST(root.right);
    }



    private TreeNode first = null;
    private TreeNode second = null;
    private TreeNode prevNode = null;

    /**
     * Given a binary search tree whose two nodes are swapped by mistake.
     * Recover the tree using O(1) memory.
     * @param root
     */
    public void recoverTree(TreeNode root) {
        //idea:
        //visualizing each node.val as a vertical bar
        //inorder traversal gives:
        //     |
        //   | |
        // | | |
        // if the bars are not in ascending order, then one of the bar (node) was swapped
        findSwappedNodes(root);
        int temp = first.val;
        first.val = second.val;
        second.val = temp;
    }

    private void findSwappedNodes(TreeNode node) {
        if (node == null) {
            return;
        }

        findSwappedNodes(node.left);
        if (prevNode != null && prevNode.val > node.val) {
            if (first == null) { // first time encounter a bad node
                first = prevNode;
                second = node;
            } else { // second time encounter a bad node
                second = node;
                return;
            }
        }
        prevNode = node;
        findSwappedNodes(node.right);
    }

    /**
     * Determine if p and q represent the same tree
     * @param p
     * @param q
     * @return
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null) {
            return q == null;
        }

        if (q == null) {
            return false;
        }

        return p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);

    }

    /**
     * Determine if the given tree is symmetric (mirror reflection)
     * @param root
     * @return
     */
    public boolean isSymmetric(TreeNode root) {
        return isSymmetric(root, root);
    }

    private boolean isSymmetric(TreeNode p, TreeNode q) {
        if (p == null) {
            return q == null;
        }

        if (q == null) {
            return false;
        }

        return p.val == q.val && isSymmetric(p.left, q.right) && isSymmetric(p.right, q.left);
    }

    /**
     * Find the maximum depth of the given tree.
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    /**
     * Given a binary tree, return the level order traversal of its nodes' values.
     * (ie, from left to right, level by level).
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }

        List<List<Integer>> lists = new ArrayList<>();
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.addLast(root);

        while (!queue.isEmpty()) {
            ArrayList<Integer> list = new ArrayList<>();
            int len = queue.size();
            // each iteration will clear the entire queue
            // which holds the level that is being visited
            // and refill the queue with the children of the level
            for (int i = 0; i < len; ++i) {
                TreeNode node = queue.pollFirst();
                list.add(node.val);
                if (node.left != null) {
                    queue.addLast(node.left);
                }
                if (node.right != null) {
                    queue.addLast(node.right);
                }
            }
            lists.add(list);

        }
        return lists;

    }

    /**
     * Given a binary tree,
     * imagine yourself standing on the right side of it,
     * @param root
     * @return the values of the nodes you can see ordered from top to bottom.
     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        rightSideView(root, list, 1);
        return list;
    }

    private void rightSideView(TreeNode root, List<Integer> list, int depth) {
        if (root == null) {
            return;
        }
        if (depth > list.size()) {
            list.add(root.val);
        }
        rightSideView(root.right, list, depth + 1);
        rightSideView(root.left, list, depth + 1);

    }

}

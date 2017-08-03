package com.company;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tliu4 on 8/3/2017.
 */
public class GraphAlgo {

    /**
     * Given two words (beginWord and endWord), and a dictionary's word list,
     * find the length of shortest transformation sequence from beginWord to endWord, such that:
     * 1. Only one letter can be changed at a time.
     * 2. Each transformed word must exist in the word list. Note that beginWord is not a transformed word.
     * e.g.
     * beginWord = "hit"
     * endWord = "cog"
     * wordList = ["hot","dot","dog","lot","log","cog"]
     * return: 5 ("hit" -> "hot" -> "dot" -> "dog" -> "cog")
     * @param beginWord
     * @param endWord
     * @param wordList
     * @return
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        HashMap<String, HashSet<String>> adjacencyMap = new HashMap<>();

        //initialize the adjacency matrix
        findNeighbourFor(beginWord, -1, wordList, adjacencyMap);
        int len = wordList.size();
        for (int i = 0; i < len; ++i) {
            findNeighbourFor(wordList.get(i), i, wordList, adjacencyMap);
        }

        if (adjacencyMap.get(endWord) == null) {
            return 0;
        }
        //run BFS to find "endWord"
        HashSet<String> visitedWords = new HashSet<>(len);
        ArrayDeque<Node> queue = new ArrayDeque<>(len);
        queue.addFirst(new Node(beginWord, 1));
        while (queue.size() > 0) {
            Node node = queue.removeLast();

            if (visitedWords.contains(node.word)) {
                continue;
            }
            if (node.word.equals(endWord)) {
                return node.distance;
            }
            visitedWords.add(node.word);
            int dist = node.distance + 1;
            HashSet<String> neighbours = adjacencyMap.get(node.word);
            for (String neighbour : neighbours) {
                queue.addFirst(new Node(neighbour, dist));
            }
        }
        return 0;

    }

    /**
     * Find all the neighbours for "word" from "wordList".
     * This function assumes the the words (and its neighbours) having lower index than "wordIndex"
     * are already taken care of.
     *
     * @param word         the word for which we find neighbours
     * @param wordIndex    the index of "word" in wordList, -1 if not in the list
     * @param wordList     a list of word
     * @param adjacencyMap graph structure
     */
    private void findNeighbourFor(String word, int wordIndex, List<String> wordList, HashMap<String, HashSet<String>> adjacencyMap) {
        HashSet<String> neighours = adjacencyMap.get(word);
        neighours = neighours == null ? new HashSet<>() : neighours;
        int len = wordList.size();
        int wordLen = word.length();

        for (int i = wordIndex + 1; i < len; ++i) {
            String word2 = wordList.get(i); //potential neighbour
            int diff = 0; //hold letter difference between "word" and potential neighbour
            for (int j = 0; j < wordLen; ++j) {
                diff = word.charAt(j) != word2.charAt(j) ? diff + 1 : diff;
            }
            if (diff == 1) {
                neighours.add(word2);
                HashSet<String> word2Neighbours = adjacencyMap.get(word2); //mutual inclusion
                if (word2Neighbours != null) {
                    word2Neighbours.add(word);
                } else {
                    word2Neighbours = new HashSet<>();
                    word2Neighbours.add(word);
                    adjacencyMap.put(word2, word2Neighbours);
                }
            }
        }

        adjacencyMap.put(word, neighours);
    }

    private class Node implements Comparable<Node> {
        String word;
        int distance;

        public Node(String word, int distance) {
            this.word = word;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.distance, o.distance);
        }
    }
}

package controllers;

import java.io.File;
import java.util.*;

/**
 * List of localization keys found in the sources.
 */
public class SourceMessageKeys {

    private Map<String, KeySourceList> keys = new HashMap<String, KeySourceList>();

    public SourceMessageKeys() {
    }

    public void addKey(String foundKey, File file, String snippet, int lineNo) {
        KeySourceList key = keys.get(foundKey);
        if (key == null) {
            key = new KeySourceList(foundKey);
            keys.put(foundKey, key);
        }
        key.addSource(file, snippet, lineNo);
    }

    public KeySourceList getKeySourceList(String key) {
        KeySourceList list = keys.get(key);
        if (list == null) {
            list = new KeySourceList(key);
        }
        return list;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        TreeMap<String, KeySourceList> map = new TreeMap<String, KeySourceList>(keys);
        for (String key : map.keySet()) {
            builder.append(map.get(key));
            builder.append("\n");
        }
        return builder.toString();
    }

    public Set<String> keySet() {
        return keys.keySet();        
    }

    /**
     * List of sourceFiles for localization key.
     */
    public static class KeySourceList {

        public HashMap<String, SourceFile> sourceFiles = new HashMap<String, SourceFile>();

        public String foundKey;

        public KeySourceList(String foundKey) {
            this.foundKey = foundKey;
        }

        public List<SourceFile> listSourceFiles() {
            ArrayList<SourceFile> list = new ArrayList<SourceFile>(sourceFiles.values());
            Collections.sort(list);
            return list;
        }

        public void addSource(File file, String snippet, int lineNo) {
            SourceFile sourceFile = sourceFiles.get(file.getPath());
            if (sourceFile == null) {
                sourceFile = new SourceFile(file.getPath());
                sourceFiles.put(file.getPath(), sourceFile);
            }
            sourceFile.addSnippet(snippet, lineNo);
        }

        /**
         * Single source file.
         */
        public static class SourceFile implements Comparable<SourceFile> {
            public String path;

            public List<Snippet> snippets = new ArrayList<Snippet>();

            public SourceFile(String path) {
                this.path = path;
            }

            public void addSnippet(String snippet, int lineNo) {
                snippets.add(new Snippet(snippet,lineNo));
            }

            public List<Snippet> listSnippets() {
                Collections.sort(snippets);
                return snippets;
            }

            public int compareTo(SourceFile o) {
                return path.compareTo(o.path);
            }

            /**
             * Code snippet in the source file.
             */
            public static class Snippet implements Comparable<Snippet> {
                public String snippet;
                public int lineNo;

                public Snippet(String snippet, int lineNo) {
                    this.snippet = snippet;
                    this.lineNo = lineNo;
                }

                public int compareTo(Snippet o) {
                    return new Integer(lineNo).compareTo(o.lineNo);
                }
            }
        }
    }
}

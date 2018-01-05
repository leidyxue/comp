package com.baifendian.comp.api.service.util;

import com.baifendian.comp.api.dto.error.PreFailedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.support.DefaultMessageSourceResolvable;

public class FolderHelper {

  public static void checkFolderCycle(List<Pair<String, String>> all, Pair<String, String> curFolder, String name) {
    if (CollectionUtils.isEmpty(all) || curFolder == null || StringUtils
        .isEmpty(curFolder.getLeft())) {
      return;
    }

      Tree root = Tree.buildTree(all);

      Tree child = root.getChild(curFolder.getRight());
      if (child == null){
        throw new PreFailedException("com.bfd.bi.api.project.parent.same.error",
            new DefaultMessageSourceResolvable(name));
      }

      if (null != child.getChild(curFolder.getLeft())) {
        throw new PreFailedException("com.bfd.bi.api.project.parent.same.error",
            new DefaultMessageSourceResolvable(name));
      }
  }

}

class Tree {

  private Pair<String, String> value;
  private List<Tree> childTrees = new ArrayList<>();

  private Tree() {
  }

  private Tree(Pair<String, String> val) {
    this.value = val;
  }

  static Tree buildTree(List<Pair<String, String>> ids) {
    Tree rootTree = new Tree();
    Map<String, Tree> treeMap = new HashMap<>();

    for (Pair<String, String> pair : ids) {
      Tree tree = new Tree(pair);
      if (StringUtils.isEmpty(pair.getLeft())) {
        rootTree.childTrees.add(tree);
      } else {
        Tree parent = treeMap.get(pair.getLeft());
        if (parent != null) {
          parent.childTrees.add(tree);
        }
      }
      treeMap.put(pair.getRight(), tree);
    }

    for (String key : treeMap.keySet()) {
      Tree tree = treeMap.get(key);
      if (tree.value.getLeft() != null) {
        Tree parent = treeMap.get(tree.value.getLeft());
        if (parent != null) {
          parent.childTrees.add(tree);
        }
      }
    }

    return rootTree;
  }

  Tree getChild(String id) {
    if (value != null) {
      if (StringUtils.equals(id, value.getRight())) {
        return this;
      }
    }

    for (Tree tree : childTrees) {
      Tree child = tree.getChild(id);
      if (child != null) {
        return child;
      }
    }

    return null;
  }
}

package com.mona.common.container.tree;

public class BTTree {
    public static class BTNode<DATA> {
        BTNode<DATA> parent, child_left, child_middle, child_right;
        DATA first, second;
    }
}

package me.jiesoul.cflat.compiler;

import me.jiesoul.cflat.ast.ExprNode;
import me.jiesoul.cflat.ast.StmtNode;

/**
 * Created by zhangyunjie on 2017/3/31.
 */
public class DerefenceChecker {

    private void check(StmtNode node) {
        node.accept(this);
    }

    private void check(ExprNode node) {
        node.accept(this);
    }
}

package me.jiesoul.cflat.ast;
import java.io.PrintStream

/**
 * Created by zhangyunjie on 2017/3/31.
 */
public abstract class Node implements  Dumpalbe {
    public Node() {

    }

    public abstract Location Location();

    public void dump() {
        dump(System.out);
    }

    public void dump(PrintStream s) {
        dump(new Dumper(s));
    }

    public void dump(Dumper d) {
        d.printClass(this, location());
        _dump(d);
    }

    protected abstract void _dump(Dumper d);
}

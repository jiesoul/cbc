package me.jiesoul.cflat.entity;

/**
 * Created by zhangyunjie on 2017/4/1.
 */
public class LocalScope {
    public Entity get(String name) throws SemanticException {
        DefinedVariable var = variables.get(name);
        if (var != null) {
            return var;
        } else {
            return parent.get(name);
        }
    }
}

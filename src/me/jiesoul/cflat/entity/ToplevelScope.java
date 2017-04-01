package me.jiesoul.cflat.entity;

/**
 * Created by zhangyunjie on 2017/4/1.
 */
public class ToplevelScope {

    public Entity get(String name) throws SemanticException {
        Entity ent = entities.get(name);
        if (ent == null) {
            throw new SemanticException("unrefsolved reference: " + name);
        }
        return ent;
    }
}

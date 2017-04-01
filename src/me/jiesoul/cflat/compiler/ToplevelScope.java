package me.jiesoul.cflat.compiler;

/**
 * Created by zhangyunjie on 2017/4/1.
 */
public class ToplevelScope {

    public void declareEntity(Entity entity) {
        Entity e = new entities.get(entity.name());
        if (e != null) {
            throw new SemanticException("duplicated declaration: " + entity.name() + ":" + e.location() + " and " + entity.location());
        }
        entities.put(entity.name(), entity);
    }
}

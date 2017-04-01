package me.jiesoul.cflat.ast;

import me.jiesoul.cflat.type.StructType;
import me.jiesoul.cflat.type.Type;

/**
 * Created by zhangyunjie on 2017/4/1.
 */
public class StructNode extends CompositeTypeDefinition {

    public Type definingType() {
        return new StructType(name(), members(), location());
    }
}

package me.jiesoul.cflat.compiler;

import me.jiesoul.cflat.ast.TypeDefinition;
import me.jiesoul.cflat.ast.TypeNode;
import me.jiesoul.cflat.type.Type;

import java.util.List;

/**
 * Created by zhangyunjie on 2017/3/31.
 */
public class TypeResolver {
    private final TypeTable typeTable;
    private final ErrorHandler errorHandler;

    public TypeResolver(TypeTable typeTable, ErrorHandler errorHandler) {
        this.typeTable = typeTable;
        this.errorHandler = errorHandler;
    }

    public void resolve(AST ast) {
        defineTypes(ast.types());
        for (TypeDefinition t : ast.types()) {
            t.accept(this);
        }
        for (Entity e : ast.entities()) {
            e.accept(this);
        }
    }

    private void defineTypes(List<TypeDefinition> definitions) {
        for (TypeDefinition def : definitions) {
            if (typeTable.isDefined(def.typeRef())) {
                error(def, "duplicated type definition: " + def.typeRef());
            } else {
                typeTable.put(def.typeRef(), def.definingType());
            }
        }
    }

    public Void visit(DefinedVariable var) {
        bindType(var.typeNode());
        if (var.hasInitializer()) {
            visitExpr(var.initializer());
        }
        return null;
    }

    private void bindType(TypeNode n) {
        if (n.isResolved()) return ;
        n.setType(typeTable.get(n.typeRef()));
    }

    public Void visit(DefinedFunction func) {
        resolveFunctionHeader(func);
        visitStmt(func.body());
        return null;
    }

    private void resolveFunctionHeader(Function func) {
        bindType(func.typeNode());
        for (Parameter param : func.parameters()) {
            Type t = typeTable.getParamType(param.typeNode().typeRef());
            param.typeNode().setType(t);
        }
    }
}

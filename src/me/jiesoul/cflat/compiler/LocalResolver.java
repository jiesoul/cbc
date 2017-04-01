package me.jiesoul.cflat.compiler;

import java.util.LinkedList;

/**
 * Created by zhangyunjie on 2017/4/1.
 */
public class LocalResolver {
    private final LinkedList<Scope> scopeStack;
    private final ConstantTable constantTable;
    private final ErrorHandler errorHandler;

    public LocalResolver(ErrorHandler h) {

        this.errorHandler = h;
        this.scopeStack = new LinkedList<Scope>();
        this.constantTable = new ConstantTable();
    }

    public void resolve(AST ast) throws SemanticException {
        ToplevelScope toplevel = new ToplevelScope();
        scopeStack.add(toplevel);

        for (Entity decl : ast.declarations()) {
            toplevel.declareEntity(decl);
        }

        for (Entity ent : ast.definitions()) {
            toplevel.defineEntity(ent);
        }

        resolveGvarInitiallizers(ast.definedVariables());
        resolveConstantValues(ast.constants());
        resolveFunctions(ast.definedFunctions());
        toplevel.checkReferences(errorHandler);
        if (errorHandler.errorOccured()) {
            throw new SemanticException("compile failed");
        }

        ast.setScope(toplevel);
        ast.setConstantTable(constantTable);

    }

    private void resolveFunctions(List<DefinedFunction> funcs) {
        for (DefinedFunction func : funcs) {
            pushScope(func.parameters());
            resolve(func.body());
            func.setScope(popScope());
        }
    }

    private void pushScope(List<? extends DefinedVariable> vars) {
        LocalScope scope = new LocalScope(currentScope());
        for (DefinedVariable var : vars) {
            if (scope.isDefinedLocally(var.name)) {
                error(var.location(), "duplicated variable in scope: " + var.name());
            } else {
                scope.defineVariable(var);
            }
        }
        scopeStack.addLast(scope);
    }

    private Scope currentScope() {
        return scopeStack.getLast();
    }

    private LocalScope popScope() {
        return (LocalScope)scopeStack.removeLast();
    }

    public Void visit(BlockNode node) {
        pushScope(node.variables());
        super.visit(node);
        node.setScope(popScope());
        return null;
    }

    public Void visit(variableNode node) {
        try {
            Entity ent = currentScope().get(node.getName());
            ent.refered();
            node.setEntity(ent);
        } catch (SemanticException ex) {
            error(node, ex.getMessage());
        }
        return null;
    }
}

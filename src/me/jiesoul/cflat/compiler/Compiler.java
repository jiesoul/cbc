package me.jiesoul.cflat.compiler;

/**
 * Created by zhangyunjie on 2017/3/31.
 */
public class Compiler {

    static final public String ProgramName = "cbc";
    static final public String Version = "1.0.0";

    public static void main(String[] args) {
        new Compiler(ProgramName).commandMain(args);
    }

    private final ErrorHandler errorHandler;

    public Compiler(String programName) {
        this.errorHandler = new ErrorHandler(programName);
    }

    public void commandMain(String[] args) {
        Options opts = Options.parse(args);
        List<SourceFile> srcs = opts.sourceFile();
        build(srcs, opts);
    }

    public void build(List<SourceFile> srcs, Options opts) throws CompilerException {
        for (SourceFile src : srcs) {
            compiler(src.path(), opts.asmFileNameOf(src), opts);
            assemble(src.path(), opts.objFileNameOf(src), opts);
        }
        link(opts);
    }

    public void compile(String srcPath, String destPath,
                        Options opts) throws CompilerException {
        AST ast = parseFile(srcPath, opts); //生成抽象语法树
        TypeTable types = opts.typeTable(); //类型表
        AST sem = semanticAnalyze(ast, types, opts); //语义分析
        IR ir = new IRGenerator(errorHandler).generate(sem, types); //中间代码生成
        //编译器的前端处理完成

        String asm = genrateAssembly(ir, opts); //生成汇编代码
        writeFile(destPath, asm);   //输出文件
    }
}

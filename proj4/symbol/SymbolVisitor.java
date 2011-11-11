/* ***************************
* Student: Benjamin Carr
* carrbs@cs.pdx.edu
*
* proj4 (SymbolVisitor.java)
*
* ***************************/
package symbol;
import ast.*;
import java.util.Hashtable;
import java.util.Vector;

public class SymbolVisitor implements TypeVI
{
    public Table symTable;          // the top-scope symbol table
    private ClassRec currClass;     // the current class scope
    private MethodRec currMethod;   // the current method scope
    private boolean hasMain;        // whether "main" method is defined

    public SymbolVisitor() { ... }

    public void visit(Program n) throws Exception
    {
        n.cl.accept(this);
        if (!hasMain)
            throw new SymbolException("Method main is missing");
        setupClassHierarchy(n.cl);  // establish class hierarchy
    }

    public void visit(ClassDecl n) throws Exception
    {
        // add a ClassRec to symTable 
        // recursively process vl list and ml list
    }

    public void visit(MethodDecl n) throws Exception
    {
        // add a MethodRec to the current ClassRec
        // recursively process fl list and vl list
        // if the method is ’main’, check for violations
        //  of main method’s rules
    }

    public void visit(VarDecl n) throws Exception
    {
        // decide whether the var is a local var or a class var
        // (hint: use env variables currClass and currMethod)
        // add a VarRec to the proper ClassRec of MethodRec
    }

    public void visit(Formal n) throws Exception
    {
        // add a VarRec to the current MethodRec’s param list
    }

    //
    // LISTS --- use default traversal
    //
    public void visit(List n) throws Exception {}
    public void visit(ClassDeclList n) throws Exception
    {
        for (int i = 0; i < n.size(); i++)
            n.elementAt(i).accept(this);
    }

    public void visit(VarDeclList n) throws Exception
    {
        for (int i = 0; i < n.size(); i++)
            in.elementAt(i).accept(this);
    }

    //
    // TYPES --- return the nodes themselves
    //
    public Type visit(IntType n) { return n; }
    public Type visit(BoolType n) { return n; }
    public Type visit(ObjType n) throws Exception { return n; }

    //
    // STMTS and EXPRS
    //
    public void visit(StmtList n) throws Exception {}
    public void visit(Block n) throws Exception {}
    public void visit(Assign n) throws Exception {}

    public void visit(ExpList n) throws Exception {}
    public Type visit(Binop n) throws Exception { return null; }
    public Type visit(Relop n) throws Exception { return null; }
}

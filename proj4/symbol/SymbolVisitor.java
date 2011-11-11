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

    public SymbolVisitor()
    {
        symTable = new Table();
        currClass = null;
        currMethod = null;
    }

    public void visit(Program n) throws Exception
    {
        n.cl.accept(this);
        if (!hasMain)
            throw new SymbolException("Method main is missing");
        setupClassHierarchy(n.cl);  // establish class hierarchy
    }



    public void visit(VarDecl n) throws Exception
    {
        // decide whether the var is a local var or a class var
        // (hint: use env variables currClass and currMethod)
        // add a VarRec to the proper ClassRec of MethodRec
        if (currMethod)
            currMethod.addLocal(n.var, n.t);
        else
            currClass.addClassVar(n.var, n.t, n.e);
    }

    public void visit(Formal n) throws Exception
    {
        // add a VarRec to the current MethodRec’s param list

    }

    //
    // LISTS --- use default traversal
    //
    public void visit(AstList n) throws Exception {}

    public void visit(ClassDeclList n) throws Exception
    {
        for (int i = 0; i < n.size(); i++)
            n.elementAt(i).accept(this);
    }

    public void visit(ClassDecl n) throws Exception
    {
        symTable.addClass(n.cid); // add a ClassRec to symTable 
        currClass = symTable.getClass(n.cid);
        // recursively process vl list and ml list
        for (int i = 0; i < n.vl.size(); i++)
            n.vl.elementAt(i).accept(this);
        for (int i = 0; i < n.ml.size(); i++)
            n.ml.elementAt(i).accept(this);
        currClass = null;
    }

    public void visit(VarDeclList n) throws Exception
    {
        for (int i = 0; i < n.size(); i++)
            in.elementAt(i).accept(this);
    }

    public void visit(MethodDecl n) throws Exception
    {
        // add a MethodRec to the current ClassRec
        currClass.addMethod(n.mid, n.t);
        currMethod = currClass.getMethod(n.mid);
        // recursively process fl list and vl list
        for (int i = 0; i < n.fl.size(); i++)
            n.fl.elementAt(i).accept(this);
        for (int i = 0; i < n.vl.size(); i++)
            n.vl.elementAt(i).accept(this);
        // if the method is ’main’, check for violations
        //  of main method’s rules
        if (n.id.s == "main")
        {
            if(!hasMain)
                hasMain = true;
            else 
            throw new SymbolException("Every program must have one" +
                " (and only one) main method.");
        }
        currMethod = null;
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

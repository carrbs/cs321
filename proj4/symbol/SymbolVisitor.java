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

    private void setupClassHierarchy(ClassDeclList cl) throws Exception 
    {
        Vector<ClassDecl> work = new Vector<ClassDecl>();
        Vector<String> done = new Vector<String>();
        for (int i=0; i<cl.size(); i++)
            work.add(cl.elementAt(i));
        while (work.size() > 0)
        {
            for (int i=0; i<work.size(); i++)
            {
                ClassDecl cd = (ClassDecl) work.elementAt(i);
                if (cd.pid != null) 
                {
                    if (!done.contains(cd.pid.s)) continue;
                    ClassRec cr = symTable.getClass(cd.cid);
                    ClassRec pr = symTable.getClass(cd.pid);
                    cr.linkParent(pr);
                }
                done.add(cd.cid.s);
                work.remove(cd);
            }
        }
    }

    public void visit(Program n) throws Exception
    {
        n.cl.accept(this);
        if (!hasMain)
            throw new SymbolException("Method main is missing");
        setupClassHierarchy(n.cl);  // establish class hierarchy
    }

    //
    // LISTS --- use default traversal
    //
    public void visit(VarDeclList n) throws Exception
    {
        for (int i = 0; i < n.size(); i++)
            n.elementAt(i).accept(this);
    }

    public void visit(VarDecl n) throws Exception
    {
        // decide whether the var is a local var or a class var
        // (hint: use env variables currClass and currMethod)
        // add a VarRec to the proper ClassRec of MethodRec
        if (currMethod != null)
        {
            if (currMethod.getParam(n.var) != null)
                throw new SymbolException(n.var.s +" already defined as a param");
            currMethod.addLocal(n.var, n.t);
        }
        else
            currClass.addClassVar(n.var, n.t, n.e);
    }

    public void visit(FormalList n) throws Exception
    {
        for (int i = 0; i < n.size(); i++)
            n.elementAt(i).accept(this);
    }

   public void visit(Formal n) throws Exception
    {
        // add a VarRec to the current MethodRec’s param list
        currMethod.addParam(n.id, n.t);
    }

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


    public void visit(MethodDeclList n) throws Exception
    {
        for (int i = 0; i < n.size(); i++)
            n.elementAt(i).accept(this);
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
        if (n.mid.s.equals("main"))
        {
            if (currClass.varCnt() > 0)
                throw new SymbolException("main class are not allowed" +
                    " to have data fields");
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
    public Type visit(BasicType n) { return n; }
    public Type visit(ArrayType n) { return n; }
    public Type visit(ObjType n) throws Exception { return n; }

    //
    // STATEMENTS
    //
    public void visit(StmtList n) throws Exception {}
    public void visit(Block n) throws Exception {}
    public void visit(Assign n) throws Exception {}
    public void visit(CallStmt n) throws Exception {}
    public void visit(If n) throws Exception {}
    public void visit(While n) throws Exception {}
    public void visit(Print n) throws Exception {}
    public void visit(Return n) throws Exception {}

    //
    // Expressions
    //
    public void visit(ExpList n) throws Exception {}
    public Type visit(Binop n) throws Exception {return null;}
    public Type visit(Relop n) throws Exception {return null;}
    public Type visit(Unop n) throws Exception {return null;}
    public Type visit(ArrayElm n) throws Exception {return null;}
    public Type visit(ArrayLen n) throws Exception {return null;}
    public Type visit(Field n) throws Exception {return null;}
    public Type visit(Call n) throws Exception {return null;}
    public Type visit(NewArray n) throws Exception {return null;}
    public Type visit(NewObj n) throws Exception {return null;}
    public Type visit(Id n) throws Exception {return null;}
    public Type visit(This n) throws Exception {return null;}

    //
    // Base values
    //
    public Type visit(IntVal n) {return null;}
    public Type visit(FloatVal n) {return null;}
    public Type visit(BoolVal n) {return null;}
    public Type visit(StrVal n) {return null;}
    }

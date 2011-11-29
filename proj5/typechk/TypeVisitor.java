/* ***************************
* Student: Benjamin Carr
* carrbs@cs.pdx.edu
*
* proj5 (TypeVisitor.java)
*
* ***************************/
package typechk;
import ast.*;
import symbol.*;

public class TypeVisitor implements TypeVI {
  private Table symTable;
  private ClassRec currClass;
  private MethodRec currMethod;
  private boolean hasReturn;

  private BasicType IntType = new BasicType(BasicType.Int);
  private BasicType BoolType = new BasicType(BasicType.Bool);
  private BasicType FloatType = new BasicType(BasicType.Float);
  
  public TypeVisitor(Table symtab)
  {
    symTable = symtab;
    currClass = null;
    currMethod = null;
    hasReturn = false;
  }

  public void visit(Program n) throws Exception
  {
    n.cl.accept(this);
  }

  // Lists
  public void visit(AstList n) throws Exception{}
  public void visit(ClassDeclList n) throws Exception
  {
    for (int i = 0; i < n.size(); i++) 
        n.elementAt(i).accept(this);
  }
  public void visit(MethodDeclList n) throws Exception
  {
    for (int i = 0; i < n.size(); i++) 
        n.elementAt(i).accept(this);
  }
  public void visit(VarDeclList n) throws Exception
  {
    for (int i = 0; i < n.size(); i++) 
        n.elementAt(i).accept(this);
  }
  public void visit(FormalList n) throws Exception
  {
    for (int i = 0; i < n.size(); i++) 
        n.elementAt(i).accept(this);
  }
  public void visit(StmtList n) throws Exception
  {
    for (int i = 0; i < n.size(); i++) 
        n.elementAt(i).accept(this);
  }
  public void visit(ExpList n) throws Exception
  {
    for (int i = 0; i < n.size(); i++) 
        n.elementAt(i).accept(this);
  }

  // Declarations
  public void visit(ClassDecl n) throws Exception
  {
    currClass = symTable.getClass(n.cid);
    if (n.pid != null)
        symTable.getClass(n.pid);
    n.vl.accept(this);
    n.ml.accept(this);
    currClass = null;
  }
  public void visit(MethodDecl n) throws Exception
  {
    currMethod = currClass.getMethod(n.mid);
    if (n.t != null)
        n.t.accept(this);
    n.fl.accept(this);
    n.vl.accept(this);
    n.sl.accept(this);
     if (n.t != null && hasReturn == false)
        throw new TypeException("Method "+ n.mid.s
            + " is missing a Return statment");
    currMethod = null;
  }
  public void visit(VarDecl n) throws Exception
  {
    n.t.accept(this);
    if (n.e != null) 
    {
        Type t = n.e.accept(this);
    }
  }
  public void visit(Formal n) throws Exception
  {
    if (n.t != null)
        n.t.accept(this);
  }

  // Types
  public Type visit(BasicType n) { return n; }
  public Type visit(ObjType n) throws Exception
  {
    symTable.getClass(n.cid);
    return n;
  }
  public Type visit(ArrayType n) { return n.et; }

  private boolean compatible(Type t1, Type t2) throws Exception 
  {
    if (t1==t2)
        return true;
    else if (t1 instanceof BasicType && t2 instanceof BasicType)
    {
        BasicType a = (BasicType) t1;
        BasicType b = (BasicType) t2;
        if (a.typ == b.typ)
            return true;
    }
    else if (t1 instanceof ObjType && t2 instanceof ObjType)
    {
        ObjType a = (ObjType) t1;
        ObjType b = (ObjType) t2;
        if (a.cid.s.equals(b.cid.s))
            return true;
        else 
        {
            ClassRec foo = symTable.getClass(b.cid).parent();
            if (foo != null)
            {
                return compatible(t1, new ObjType(foo.id()));
            }
        }
    }
    else if (t1 instanceof ArrayType && t2 instanceof ArrayType)
    {
        ArrayType a = (ArrayType) t1;
        ArrayType b = (ArrayType) t2;
        return compatible(a.et, b.et);
    }
    return false;
  }
  // Statements
  public void visit(Block n) throws Exception
  {
    n.sl.accept(this);
  }
  public void visit(Assign n) throws Exception
  {
    Type t1 = n.lhs.accept(this);
    Type t2 = n.rhs.accept(this);
    if(compatible(t1, t2) == false)
        throw new TypeException("Incompatible types in Assign: "
            + t1.toString() + " <- " + t2.toString());
  }
  public void visit(CallStmt n) throws Exception
  {
    Type t = n.obj.accept(this);
    ObjType a = (ObjType) t;
    MethodRec mr = symTable.getClass(a.cid).getMethod(n.mid);
    if (mr == null)
        throw new SymbolException("Method " + n.mid.s + " not defined");
    int cnt = mr.paramCnt();
    int argscnt;
    if (n.args == null)
        argscnt = 0;
    else
        argscnt = n.args.size();
    if (cnt != argscnt)
        throw new TypeException("Sizes don't match");
    if (n.args != null)
    {
        for (int i = 0; i < n.args.size(); i++) 
        {
            Type arg = n.args.elementAt(i).accept(this);
            Type par = mr.getParamAt(i).type();
            if (!compatible(arg, par))
                throw new TypeException("Formal's and actual's types not "
                    + "compatible: " + arg + " vs. " + par);
        }
    }
  }
  public boolean isBoolean(Type t)
  {
    if (t instanceof BasicType)
    {
            BasicType a = (BasicType) t;
            if(a.typ == BasicType.Bool)
                return true;
    }
    return false;
  }
  public void visit(If n) throws Exception
  {
    Type t = n.e.accept(this);
    if(isBoolean(t))
    {
        n.s1.accept(this);
        if(n.s2 != null)
            n.s2.accept(this);
        return;
    }
    throw new TypeException("Test of If is not of boolean type: "
        + t.toString());
  }
  public void visit(While n) throws Exception
  {
        n.e.accept(this);
        n.s.accept(this);
  }
  public void visit(Print n) throws Exception
  {
    if (n.e != null)
    {
        Type t = n.e.accept(this);
        if (!(t instanceof BasicType))
            throw new TypeException("Argument to Print must be of a basic "
                + "type or a string literal: " + t.toString());
    }
  }
  public void visit(Return n) throws Exception
  {
    hasReturn = true;
    Type rt = currMethod.rtype();
    if (n.e != null)
    {
        Type t = n.e.accept(this);
        if(!compatible(rt, t))
            throw new TypeException("Wrong return type for method "
                + currMethod.id().s + ": " + t.toString());
    }
    else if (rt != null)
    throw new TypeException("Return is missing an expr of type "
        + rt.toString());
  }
  public Type isIntOrFloat(BasicType t1, BasicType t2)
  {
    if (t1.typ == BasicType.Int && t2.typ == BasicType.Int)
        return IntType;
    else if (t1.typ == BasicType.Int && t2.typ == BasicType.Float
                || t1.typ == BasicType.Float && t2.typ == BasicType.Int
                || t1.typ == BasicType.Float && t2.typ == BasicType.Float)
        return FloatType;
    return null;
  }
  // Expressions
  public Type visit(Binop n) throws Exception
  {
    Type typ1 = n.e1.accept(this);
    Type typ2 = n.e2.accept(this);
    if (typ1 instanceof BasicType && typ2 instanceof BasicType)
    {
        BasicType t1 = (BasicType) typ1;
        BasicType t2 = (BasicType) typ2;
        switch (n.op)
        {
        case Binop.ADD:
        case Binop.SUB:
        case Binop.MUL:
        case Binop.DIV:
            Type t = isIntOrFloat(t1, t2);
            if (t != null)
                return t;
            break;
        case Binop.AND:
        case Binop.OR:
            if (t1.typ == BasicType.Bool && t2.typ == BasicType.Bool)
                return BoolType;
        }
    }
    throw new TypeException("Binop operands' type mismatch: "
                + typ1.toString() + n.opName(n.op) + typ2.toString());
  }
  public Type visit(Relop n) throws Exception
  {
    Type typ1 = n.e1.accept(this);
    Type typ2 = n.e2.accept(this);
    switch (n.op) 
    {
    case Relop.EQ:
    case Relop.NE:
        if(compatible(typ1, typ2))
            return BoolType;
        break;
    case Relop.LT:
    case Relop.LE:
    case Relop.GT:
    case Relop.GE:
        if (typ1 instanceof BasicType && typ2 instanceof BasicType)
        {
            BasicType t1 = (BasicType) typ1;
            BasicType t2 = (BasicType) typ2;
            if (isIntOrFloat(t1, t2) != null)
                return BoolType;
        }
        break;
    }
    throw new TypeException("Incorrect operand types in Relop: "
        + typ1.toString() + n.opName(n.op) + typ2.toString());
  }
  public Type visit(Unop n) throws Exception
  {
    Type a = n.e.accept(this);
    BasicType t = (BasicType) a;
    switch (n.op) 
    {
    case Unop.NEG:
        if (t.typ == BasicType.Int || t.typ == BasicType.Float)
            return t;
        break;
    case Unop.NOT:
        if (t.typ == BasicType.Bool)
            return t;
    }
    throw new TypeException("Error in Unop");
  }
  public Type visit(ArrayElm n) throws Exception
  {
    Type array = n.array.accept(this);
    if (!(array instanceof ArrayType))
        throw new TypeException("ArrayElm object is not an array: "
            + array.toString());
    Type idx = n.idx.accept(this);
    if (idx instanceof BasicType)
    {
        BasicType t = (BasicType) idx;
        if(t.typ == BasicType.Int)
        {
            ArrayType a = (ArrayType) array;
            return a.et;
        }
    }
    throw new TypeException("used non-integer to index an array");
  }

  public Type visit(ArrayLen n) throws Exception
  {
    return IntType;
  }
  public Type visit(Field n) throws Exception
  {
    Type t = n.obj.accept(this);
    if (!(t instanceof ObjType))
        throw new TypeException("Object in Field is not ObjType: "
            + t.toString());
    ObjType obj = (ObjType) t;
    ClassRec cr = symTable.getClass(obj.cid);
    VarRec vr = cr.getClassVar(n.var);
    if (vr != null)
        return vr.type();
    else
    {
        vr = cr.parent().getClassVar(n.var);
        if (vr != null)
            return vr.type();
    }
    throw new TypeException("Var "+ n.var.s+" not found in class "+obj.cid.s);
  }
  public Type visit(Call n) throws Exception
  {
    Type a = n.obj.accept(this);
    ObjType t = (ObjType) a;
    ClassRec cl = symTable.getClass(t.cid);
    MethodRec mr = cl.getMethod(n.mid);
    if (mr == null)
    {
        ClassRec parent = cl.parent();
        if (parent != null)
            mr = parent.getMethod(n.mid);
    }
    if (mr == null)
    {
        throw new TypeException("Couldn't find "+ n.mid.s +" in "+ t.cid.s +" class.");
    }
    if (n.args != null)
    {
        int args = n.args.size();
        int params = mr.paramCnt();
        if (args != params)
            throw new TypeException("Formals' and actuals' counts don't match: "
                + params +" vs. "+ args);
        for (int i = 0; i < mr.paramCnt(); i++)
        {
            Type param = mr.getParamAt(i).type();
            Type varType = n.args.elementAt(i).accept(this);
            if (!compatible(param, varType))
                throw new TypeException("Formal's and actual's types not compatible: "
                    + param +" vs. "+ varType);
        }
    }
    return mr.rtype();
  }
  public Type visit(NewArray n) throws Exception
  {
    return new ArrayType(n.et);
  }
  public Type visit(NewObj n) throws Exception
  {
    symTable.getClass(n.cid);
    return new ObjType(n.cid);
  }
  public Type visit(Id n) throws Exception
  {
    VarRec v = null;
    if (currMethod != null)
    {
        v = currMethod.getParam(n);
        if (v == null)
            v = currMethod.getLocal(n);
    }
    if (v == null)
        v = currClass.getClassVar(n);
    if (v == null)
        throw new SymbolException("Var "+n.s+" not defined");
    else
        return v.type();
  }
  public Type visit(This n) throws Exception
  {
    return new ObjType(currClass.id());
  }
  // Base values
  public Type visit(IntVal n) { return IntType; }
  public Type visit(FloatVal n) { return FloatType; }
  public Type visit(BoolVal n) { return BoolType; }
  public Type visit(StrVal n) { return IntType; }
}

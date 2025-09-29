namespace AstRewriter.Core;

public abstract record Expr;
public sealed record Id(string Name) : Expr;
public sealed record Num(string Text) : Expr;
public sealed record TupleLiteral(IReadOnlyList<Expr> Elements) : Expr;
public sealed record NewExpr(string TypeName, IReadOnlyList<Expr> Args) : Expr;

public abstract record Stmt;
public sealed record VarDecl(string Name, Expr Init) : Stmt;
public sealed record Return(Expr Value) : Stmt;
public sealed record Block(IReadOnlyList<Stmt> Statements) : Stmt;

public class TupleLiteralRewriter
{
    public static Block Rewrite(Block root, string typeName)
    {
        return (Block)RewriteStmt(root, typeName);
    }

    private static Stmt RewriteStmt(Stmt stmt, string typeName)
    {
        return stmt switch
        {
            VarDecl varDecl => new VarDecl(varDecl.Name, RewriteExpr(varDecl.Init, typeName)),
            Return ret => new Return(RewriteExpr(ret.Value, typeName)),
            Block block => new Block(block.Statements.Select(s => RewriteStmt(s, typeName)).ToList()),
            _ => throw new NotSupportedException($"Statement type {stmt.GetType().Name} is not supported."),
        };
    }

    private static Expr RewriteExpr(Expr expr, string typeName)
    {
        return expr switch
        {
            Id id => id,
            Num num => num,
            TupleLiteral tuple => new NewExpr(typeName, tuple.Elements.Select(e => RewriteExpr(e, typeName)).ToList()),
            NewExpr newExpr => new NewExpr(newExpr.TypeName, newExpr.Args.Select(e => RewriteExpr(e, typeName)).ToList()),
            _ => throw new NotSupportedException($"Expression type {expr.GetType().Name} is not supported."),
        };
    }
}
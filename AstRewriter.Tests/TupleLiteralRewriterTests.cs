using AstRewriter.Core;

namespace AstRewriter.Tests;

[TestFixture]
public class TupleLiteralRewriterTests
{
    [Test]
    public void Rewrite_SimpleVarDecl_RewritesTuple()
    {
        var ast = new Block(new[]
        {
            new VarDecl("x", new TupleLiteral(new Expr[] { new Num("1"), new Id("a") }))
        });

        var rewritten = TupleLiteralRewriter.Rewrite(ast, "MyTuple");

        var expected = new Block(new[]
        {
            new VarDecl("x", new NewExpr("MyTuple", new Expr[] { new Num("1"), new Id("a") }))
        });

        Assert.That(rewritten, Is.EqualTo(expected));
    }

    [Test]
    public void Rewrite_NestedTupleLiteral_RewritesRecursively()
    {
        var ast = new Block(new[]
        {
            new VarDecl("x", new TupleLiteral(new Expr[]
            {
                new Num("1"),
                new TupleLiteral(new Expr[] { new Num("2"), new Num("3") })
            }))
        });

        var rewritten = TupleLiteralRewriter.Rewrite(ast, "MyTuple");

        var expected = new Block(new[]
        {
            new VarDecl("x", new NewExpr("MyTuple", new Expr[]
            {
                new Num("1"),
                new NewExpr("MyTuple", new Expr[] { new Num("2"), new Num("3") })
            }))
        });

        Assert.That(rewritten, Is.EqualTo(expected));
    }

    [Test]
    public void Rewrite_ReturnStatement_RewritesTuple()
    {
        var ast = new Block(new[]
        {
            new Return(new TupleLiteral(new Expr[] { new Id("x"), new Id("y") }))
        });

        var rewritten = TupleLiteralRewriter.Rewrite(ast, "ResultTuple");

        var expected = new Block(new[]
        {
            new Return(new NewExpr("ResultTuple", new Expr[] { new Id("x"), new Id("y") }))
        });

        Assert.That(rewritten, Is.EqualTo(expected));
    }

    [Test]
    public void Rewrite_TupleInNewExpression_RewritesInnerTuple()
    {
        var ast = new Block(new[]
        {
            new VarDecl("data", new NewExpr("OtherType", new[]
            {
                new TupleLiteral(new Expr[] { new Num("1"), new Num("2") })
            }))
        });

        var rewritten = TupleLiteralRewriter.Rewrite(ast, "MyTuple");

        var expected = new Block(new[]
        {
            new VarDecl("data", new NewExpr("OtherType", new[]
            {
                new NewExpr("MyTuple", new Expr[] { new Num("1"), new Num("2") })
            }))
        });

        Assert.That(rewritten, Is.EqualTo(expected));
    }

    [Test]
    public void Rewrite_NoTuples_ReturnsSameTree()
    {
        var ast = new Block(new Stmt[]
        {
            new VarDecl("x", new Num("1")),
            new Return(new Id("x"))
        });

        var rewritten = TupleLiteralRewriter.Rewrite(ast, "MyTuple");

        Assert.That(rewritten, Is.EqualTo(ast));
    }

    [Test]
    public void Rewrite_ComplexBlock_RewritesAllTuples()
    {
        var ast = new Block(new Stmt[]
        {
            new VarDecl("a", new TupleLiteral(new Expr[] { new Num("1"), new Num("2") })),
            new VarDecl("b", new NewExpr("SomeType", new []
            {
                new TupleLiteral(new Expr[] { new Num("3"), new Id("a") })
            })),
            new Return(new TupleLiteral(new Expr[] { new Id("a"), new Id("b") }))
        });

        var rewritten = TupleLiteralRewriter.Rewrite(ast, "Data");

        var expected = new Block(new Stmt[]
        {
            new VarDecl("a", new NewExpr("Data", new Expr[] { new Num("1"), new Num("2") })),
            new VarDecl("b", new NewExpr("SomeType", new []
            {
                new NewExpr("Data", new Expr[] { new Num("3"), new Id("a") })
            })),
            new Return(new NewExpr("Data", new Expr[] { new Id("a"), new Id("b") }))
        });

        Assert.That(rewritten, Is.EqualTo(expected));
    }
}
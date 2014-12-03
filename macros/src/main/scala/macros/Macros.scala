package scalaParser.macros
import language.experimental.macros
import org.parboiled2.{Rule0, RuleDSLCombinators, Rule}
import org.parboiled2.support._
import shapeless.HList

import scala.collection.immutable
import scala.language.existentials

object Macros {

  import scala.reflect.macros._
  import Compat210.whitebox.Context

  def opt_impl[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
              (c: Context)
              (r: c.Expr[Rule[I, O]])
              (o: c.Expr[Lifter[Option, I, O]])
              = {//: c.Expr[Rule[o.value.In, o.value.Out]] = {
    new ParserMacros[c.type](c).optional[I,O](r)(o)
  }

  def rep_impl[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
          (c: Context)
          (r: c.Expr[Rule[I, O]])
          (s: c.Expr[Lifter[immutable.Seq, I, O]])
           =  {//: c.Expr[Rule[s.value.In, s.value.Out]] = {
    new ParserMacros[c.type](c).rep0[I,O](r)(s)
  }

  def repSep_impl[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
              (c: Context)
              (r: c.Expr[Rule[I, O]], sep: c.Expr[Rule0])
              (s: c.Expr[Lifter[immutable.Seq, I, O]])
              = { //: c.Expr[Rule[s.value.In, s.value.Out]] = {
    new ParserMacros[c.type](c).repSep0[I,O](r, sep)(s)
  }

  def rep1_impl[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
           (c: Context)
           (r: c.Expr[Rule[I, O]])
           (s: c.Expr[Lifter[immutable.Seq, I, O]])
           = {// : c.Expr[Rule[s.value.In, s.value.Out]] = {
    new ParserMacros[c.type](c).rep10[I,O](r)(s)
  }

  def rep1Sep_impl[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
              (c: Context)
              (r: c.Expr[Rule[I, O]], sep: c.Expr[Rule0])
              (s: c.Expr[Lifter[immutable.Seq, I, O]])
              = {//: c.Expr[Rule[s.value.In, s.value.Out]] = {
    new ParserMacros[c.type](c).rep1Sep0[I,O](r, sep)(s)
  }

  class ParserMacros[C <: Context](val c: C) extends Internal210 {
    import c.universe._

    def optional[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
                (r: c.Expr[Rule[I, O]])
                (o: c.Expr[Lifter[Option, I, O]])
                : c.Expr[Any] = {//c.Expr[Rule[o.value.In, o.value.Out]] = {
      c.Expr(q"optional($r)")
    }

    def rep0[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
            (r: c.Expr[Rule[I, O]])
            (s: c.Expr[Lifter[immutable.Seq, I, O]])
            : c.Expr[Any] = {//: c.Expr[Rule[s.value.In, s.value.Out]] = {
      c.Expr(q"zeroOrMore($r)")
    }

    def repSep0[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
                (r: c.Expr[Rule[I, O]], sep: c.Expr[Rule0])
                (s: c.Expr[Lifter[immutable.Seq, I, O]])
                : c.Expr[Any] = {//: c.Expr[Rule[s.value.In, s.value.Out]] = {
      c.Expr(q"zeroOrMore($r).separatedBy($sep)")
    }

    def rep10[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
             (r: c.Expr[Rule[I, O]])
             (s: c.Expr[Lifter[immutable.Seq, I, O]])
             : c.Expr[Any] = {//: c.Expr[Rule[s.value.In, s.value.Out]] = {
      c.Expr(q"oneOrMore($r)")
    }

    def rep1Sep0[I <: HList: c.WeakTypeTag, O <: HList: c.WeakTypeTag]
                (r: c.Expr[Rule[I, O]], sep: c.Expr[Rule0])
                (s: c.Expr[Lifter[immutable.Seq, I, O]])
                : c.Expr[Any] = {//: c.Expr[Rule[s.value.In, s.value.Out]] = {
      c.Expr(q"oneOrMore($r).separatedBy($sep)")
    }
  }
}

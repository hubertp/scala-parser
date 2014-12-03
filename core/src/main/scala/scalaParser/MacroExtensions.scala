package scalaParser

import org.parboiled2.{Rule0, RuleDSLCombinators, Rule}
import org.parboiled2.support._
import shapeless.HList

import scala.reflect._

import scala.collection.immutable

trait MacroExtensions {

  def opt[I <: HList, O <: HList]
       (r: Rule[I, O])
       (implicit o: Lifter[Option, I, O])
       //: Rule[o.In, o.Out] = macro scalaParser.macros.Macros.opt_impl[I, O]
       = macro scalaParser.macros.Macros.opt_impl[I, O]

  def rep[I <: HList, O <: HList]
         (r: Rule[I, O])
         (implicit s: Lifter[immutable.Seq, I, O])
         //: Rule[s.In, s.Out] = macro scalaParser.macros.Macros.rep_impl[I, O]
      = macro scalaParser.macros.Macros.rep_impl[I, O]

  def repSep[I <: HList, O <: HList]
            (r: Rule[I, O], sep: Rule0)
            (implicit s: Lifter[immutable.Seq, I, O])
            //: Rule[s.In, s.Out] = macro scalaParser.macros.Macros.repSep_impl[I, O]
            = macro scalaParser.macros.Macros.repSep_impl[I, O]

  def rep1[I <: HList, O <: HList]
          (r: Rule[I, O])
          (implicit s: Lifter[immutable.Seq, I, O])
          //: Rule[s.In, s.Out] = macro scalaParser.macros.Macros.rep1_impl[I, O]
          = macro scalaParser.macros.Macros.rep1_impl[I, O]

  def rep1Sep[I <: HList, O <: HList]
            (r: Rule[I, O], sep: Rule0)
            (implicit s: Lifter[immutable.Seq, I, O])
            //: Rule[s.In, s.Out] = macro scalaParser.macros.Macros.rep1Sep_impl[I, O]
            = macro scalaParser.macros.Macros.rep1Sep_impl[I, O]

}

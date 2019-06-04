package Dominio;

import Nomes.Names;

abstract class IsFirst {

   protected static boolean eof(Names next) {
      return (next == Names.EOF);
   }

   protected static boolean classDecl(Names next) {
      return (next == Names.CLASS);
   }

   protected static boolean classBody(Names next) {
      return (next == Names.LCBR);
   }

   protected static boolean type(Names next) {
      return (next == Names.INT || next == Names.STRING || next == Names.ID);
   }

   protected static boolean varDeclOpt(Names next) {
      return (next == Names.COMMA);
   }

   protected static boolean constructor(Names next) {
      return (next == Names.CONSTRUCTOR);
   }

   protected static boolean methodBody(Names next) {
      return (next == Names.LPAREN);
   }

   protected static boolean statement(Names next) {
      return (next == Names.INT || next == Names.STRING || next == Names.ID
            || next == Names.PRINT || next == Names.READ || next == Names.RETURN
            || next == Names.SUPER || next == Names.IF || next == Names.FOR
            || next == Names.BREAK || next == Names.SEMICOLON);
   }

   protected static boolean atribStat(Names next) {
      return (next == Names.ID);
   }

   protected static boolean print(Names next) {
      return (next == Names.PRINT);
   }

   protected static boolean read(Names next) {
      return (next == Names.READ);
   }

   protected static boolean returnStat(Names next) {
      return (next == Names.RETURN);
   }

   protected static boolean superStat(Names next) {
      return (next == Names.SUPER);
   }

   protected static boolean ifStat(Names next) {
      return (next == Names.IF);
   }

   protected static boolean forStat(Names next) {
      return (next == Names.FOR);
   }

   protected static boolean breakStat(Names next) {
      return (next == Names.BREAK);
   }

   protected static boolean unaryExpression(Names next) {
      return (next == Names.MAIS || next == Names.MENOS);
   }

   protected static boolean allocExpression(Names next) {
      return (next == Names.NEW || type(next));
   }
}

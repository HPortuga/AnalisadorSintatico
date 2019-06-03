package Dominio;

import Nomes.Names;

abstract class IsFirst {

   static boolean eof(Names next) {
      return (next == Names.EOF);
   }

   static boolean classDecl(Names next) {
      return (next == Names.CLASS);
   }

   static boolean classBody(Names next) {
      return (next == Names.LCBR);
   }

   static boolean type(Names next) {
      return (next == Names.INT || next == Names.STRING || next == Names.ID);
   }

   static boolean varDeclOpt(Names next) {
      return (next == Names.COMMA);
   }

   static boolean constructor(Names next) {
      return (next == Names.CONSTRUCTOR);
   }

   static boolean methodBody(Names next) {
      return (next == Names.LPAREN);
   }

   static boolean statement(Names next) {
      return (next == Names.INT || next == Names.STRING || next == Names.ID
            || next == Names.PRINT || next == Names.READ || next == Names.RETURN
            || next == Names.SUPER || next == Names.IF || next == Names.FOR
            || next == Names.BREAK || next == Names.SEMICOLON);
   }

   static boolean atribStat(Names next) {
      return (next == Names.ID);
   }

   static boolean print(Names next) {
      return (next == Names.PRINT);
   }

   static boolean read(Names next) {
      return (next == Names.READ);
   }

   static boolean returnStat(Names next) {
      return (next == Names.RETURN);
   }

   static boolean superStat(Names next) {
      return (next == Names.SUPER);
   }

   static boolean ifStat(Names next) {
      return (next == Names.IF);
   }

   static boolean forStat(Names next) {
      return (next == Names.FOR);
   }

   static boolean breakStat(Names next) {
      return (next == Names.BREAK);
   }

   static boolean unaryExpression(Names next) {
      return (next == Names.MAIS || next == Names.MENOS);
   }

   static boolean allocExpression(Names next) {
      return (next == Names.NEW || type(next));
   }
}

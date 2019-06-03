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

}

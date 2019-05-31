package Dominio;

import Exceptions.*;
import Nomes.Names;

public class Parser {
   private Token lToken;
   private Scanner scanner;

   public Parser(String input) {
      this.scanner = new Scanner(input);
      advance();
   }

   private void advance() {
      this.lToken = this.scanner.nextToken();
   }

   private void match(Names name) {
      if (this.lToken.getName() == name) {
         advance();
      }
      else throw new CompilerException("Dominio.Token inesperado: " + this.lToken.getName());
   }

   public String program() throws CompilerException {
      classList();
      return "Analise sintatica concluida";
   }

   private void classList() {
      classDecl();
      if (this.lToken.getName() != Names.EOF)
         classList();
   }

   private void classDecl() {
      if (this.lToken.getName() == Names.CLASS) {
         advance();
         if (this.lToken.getName() == Names.ID) {
            advance();
            classDecl_line();
            classBody();
         } else
            throw new BadClassException(Names.ID, this.lToken.getName(), this.scanner.getLinha());
      } else
         throw new BadClassException(Names.CLASS, this.lToken.getName(), this.scanner.getLinha());
   }

   private void classDecl_line() {
      if (this.lToken.getName() == Names.EXTENDS) {
         advance();
         if (this.lToken.getName() == Names.ID) {
            advance();
         } else
            throw new BadClassException(Names.ID, this.lToken.getName(), this.scanner.getLinha());
      }
   }

   private void classBody() {
      if (this.lToken.getName() == Names.LCBR) {
         advance();

         varDeclListOpt();
         constructDeclListOpt();
         methodDeclListOpt();

         if (this.lToken.getName() == Names.RCBR)
            advance();
         else throw new BadClassBodyException(Names.RCBR, this.lToken.getName(), this.scanner.getLinha());
      } else throw new BadClassBodyException(Names.RCBR, this.lToken.getName(), this.scanner.getLinha());

   }

   private void varDeclListOpt() {
      if (this.lToken.getName() == Names.INT
      || this.lToken.getName() == Names.STRING
      || this.lToken.getName() == Names.ID)
         varDeclList();
   }

   private void varDeclList() {
      varDecl();

      if (this.lToken.getName() == Names.INT
      || this.lToken.getName() == Names.STRING
      || this.lToken.getName() == Names.ID)
         varDeclList();
   }

   private void varDecl() {
      type();

      if (this.lToken.getName() == Names.LSBR) {
         advance();

         if (this.lToken.getName() == Names.RSBR)
            advance();
         else throw new BadVariableException(Names.RSBR, this.lToken.getName(), this.scanner.getLinha());
      }

      if (this.lToken.getName() == Names.ID)
         advance();
      else throw new BadVariableException(Names.ID, this.lToken.getName(), this.scanner.getLinha());

      if (this.lToken.getName() == Names.COMMA)
         varDeclOpt();
      else if (this.lToken.getName() == Names.LPAREN) {
         methodBody();
         return;
      }

      if (this.lToken.getName() == Names.SEMICOLON)
         advance();
      else throw new BadVariableException(Names.ID, this.lToken.getName(), this.scanner.getLinha());
   }

   private void varDeclOpt() {
      if (this.lToken.getName() == Names.COMMA) {
         advance();
         if (this.lToken.getName() == Names.ID) {
            advance();
            varDeclOpt();
         }
         else throw new BadVariableException(Names.ID, this.lToken.getName(), this.scanner.getLinha());
      }
   }

   private void type() {
      if (this.lToken.getName() == Names.INT
      || this.lToken.getName() == Names.STRING
      || this.lToken.getName() == Names.ID)
         advance();
   }

   private void constructDeclListOpt() {
      if (this.lToken.getName() == Names.CONSTRUCTOR)
         constructDeclList();
   }

   private void constructDeclList() {
      constructDecl();
      if (this.lToken.getName() == Names.CONSTRUCTOR)
         constructDeclList();
   }

   private void constructDecl() {
      if (this.lToken.getName() == Names.CONSTRUCTOR) {
         advance();
         if (this.lToken.getName() == Names.LPAREN)
            methodBody();
         else throw new BadConstructorException(Names.LPAREN, this.lToken.getName(), this.scanner.getLinha());
      }
   }

   private void methodDeclListOpt() {
      if (this.lToken.getName() == Names.INT
      || this.lToken.getName() == Names.STRING
      || this.lToken.getName() == Names.ID)
         methodDeclList();
   }

   private void methodDeclList() {
      methodDecl();

      if (this.lToken.getName() == Names.INT
      || this.lToken.getName() == Names.STRING
      || this.lToken.getName() == Names.ID)
         methodDeclList();
   }

   private void methodDecl() {
      type();

      if (this.lToken.getName() == Names.LSBR) {
         advance();

         if (this.lToken.getName() == Names.RSBR)
            advance();
         else throw new BadMethodException(Names.RSBR, this.lToken.getName(), this.scanner.getLinha());
      }

      if (this.lToken.getName() == Names.ID)
         advance();
      else throw new BadMethodException(Names.ID, this.lToken.getName(), this.scanner.getLinha());

      if (this.lToken.getName() == Names.LPAREN)
         methodBody();
      else throw new BadMethodException(Names.LPAREN, this.lToken.getName(), this.scanner.getLinha());
   }

   private void methodBody() {
      if (this.lToken.getName() == Names.LPAREN) {
         advance();

         paramListOpt();

         if (this.lToken.getName() == Names.RPAREN)
            advance();
         else throw new BadMethodBodyException(Names.RPAREN, this.lToken.getName(), this.scanner.getLinha());

         if (this.lToken.getName() == Names.LCBR) {
            advance();

            statementsOpt();

            if (this.lToken.getName() == Names.RCBR)
               advance();
            else throw new BadMethodBodyException(Names.RCBR, this.lToken.getName(), this.scanner.getLinha());
         } else throw new BadMethodBodyException(Names.LCBR, this.lToken.getName(), this.scanner.getLinha());
      }
   }

   private void paramListOpt() {
      if (this.lToken.getName() == Names.INT
      || this.lToken.getName() == Names.STRING
      || this.lToken.getName() == Names.ID)
         paramList();
   }

   private void paramList() {
      param();

      if (this.lToken.getName() == Names.COMMA) {
         advance();

         if (this.lToken.getName() == Names.INT
         || this.lToken.getName() == Names.STRING
         || this.lToken.getName() == Names.ID)
            paramList();
         else throw new BadParamException(null, this.lToken.getName(), this.scanner.getLinha());
      }
   }

   private void param() {
      type();

      if (this.lToken.getName() == Names.LSBR) {
         advance();

         if (this.lToken.getName() == Names.RSBR)
            advance();
         else throw new BadParamException(Names.RSBR, this.lToken.getName(), this.scanner.getLinha());
      }

      if (this.lToken.getName() == Names.ID)
         advance();
      else throw new BadParamException(Names.ID, this.lToken.getName(), this.scanner.getLinha());
   }

   private void statementsOpt() {
      if (isFirstDeStatement())
         statements();
   }

   private void statements() {
      statement();

      if (isFirstDeStatement())
         statements();
   }

   private void statement() {
      if (this.lToken.getName() == Names.PRINT) {
         printStat();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());
      }

      if (this.lToken.getName() == Names.BREAK) {
         advance();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());
      }

      if (this.lToken.getName() == Names.SEMICOLON)
         advance();
   }

   private void printStat() {
      if (this.lToken.getName() == Names.PRINT) {
         advance();
         expression();
      }
   }

   private void readStat() {
      if (this.lToken.getName() == Names.READ) {
         advance();
//         expression();
      }
   }

   private void returnStat() {
      if (this.lToken.getName() == Names.RETURN) {
         advance();
//         expression();
      }
   }

   private void superStat() {
      if (this.lToken.getName() == Names.SUPER) {
         advance();
         if (this.lToken.getName() == Names.LPAREN) {
            advance();
//            argListOpt();
            match(Names.RPAREN);
         }
      }
   }

   private void expression() {
      numExpression();

      if (this.lToken.getName() == Names.RELOP)
         expression();
   }

   private void numExpression() {
      term();
   }

   private void term() {

   }

   private boolean isFirstDeStatement() {
      return (this.lToken.getName() == Names.INT
            || this.lToken.getName() == Names.STRING
            || this.lToken.getName() == Names.ID
            || this.lToken.getName() == Names.PRINT
            || this.lToken.getName() == Names.READ
            || this.lToken.getName() == Names.RETURN
            || this.lToken.getName() == Names.SUPER
            || this.lToken.getName() == Names.IF
            || this.lToken.getName() == Names.FOR
            || this.lToken.getName() == Names.BREAK
            || this.lToken.getName() == Names.SEMICOLON);
   }

}

package Dominio;

import Exceptions.*;
import Nomes.Names;

public class Parser {
   private Token lToken;
   private Scanner scanner;
   private boolean encontrouAmbiguidade = false;

   public Parser(String input) {
      this.scanner = new Scanner(input);
      advance();
   }

   private void advance() {
      this.lToken = this.scanner.nextToken();
   }

   public String program() throws CompilerException {
      if (IsFirst.classDecl(this.getLookahead()))
         classList();
      else if (!IsFirst.classDecl(this.getLookahead()) && !IsFirst.eof(this.getLookahead()))
         throw new BadProgramException(Names.CLASS, this.getLookahead(), this.scanner.getLinha());

      return "Analise sintatica concluida";
   }

   private void classList() {
      classDecl();

      if (!IsFirst.eof(this.getLookahead()))
         classList();
   }

   private void classDecl() {
      if (IsFirst.classDecl(this.getLookahead())) {
         advance();

         if (this.getLookahead() == Names.ID) {
            advance();

            classDecl_line();
            classBody();

         } else
            throw new BadClassException(Names.ID, this.getLookahead(), this.scanner.getLinha());

      } else
         throw new BadClassException(Names.CLASS, this.getLookahead(), this.scanner.getLinha());
   }

   private void classDecl_line() {
      if (this.lToken.getName() == Names.EXTENDS) {
         advance();

         if (this.lToken.getName() == Names.ID) {
            advance();
         } else
            throw new BadClassException(Names.ID, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private void classBody() {
      if (IsFirst.classBody(this.getLookahead())) {
         advance();

         varDeclListOpt();

         if (this.encontrouAmbiguidade) {
            this.toggleEncontrouAmbiguidade();
            methodBody();
            methodDeclListOpt();

            if (this.getLookahead() == Names.RCBR)
               advance();
            else throw new BadClassBodyException(Names.RCBR, this.getLookahead(), this.scanner.getLinha());

            return;
         }

         constructDeclListOpt();
         methodDeclListOpt();

         if (this.getLookahead() == Names.RCBR)
            advance();
         else throw new BadClassBodyException(Names.RCBR, this.getLookahead(), this.scanner.getLinha());

      } else throw new BadClassBodyException(Names.RCBR, this.getLookahead(), this.scanner.getLinha());

   }

   private void varDeclListOpt() {
      if (IsFirst.type(this.getLookahead()))
         varDeclList();
   }

   private void varDeclList() {
      varDecl();

      if (IsFirst.type(this.getLookahead()))
         varDeclList();
   }

   private void varDecl() {
      type();

      if (this.getLookahead() == Names.LSBR) {
         advance();

         if (this.getLookahead() == Names.RSBR)
            advance();
         else throw new BadVariableException(Names.RSBR, this.getLookahead(), this.scanner.getLinha());
      }

      varDecl_line();
   }

   private void varDecl_line() {
      if (this.getLookahead() == Names.ID)
         advance();
      else throw new BadVariableException(Names.ID, this.getLookahead(), this.scanner.getLinha());

      if (IsFirst.varDeclOpt(this.getLookahead()))
         varDeclOpt();
      else if (this.getLookahead() == Names.LPAREN) {
         this.toggleEncontrouAmbiguidade();
         return;
      }

      if (this.getLookahead() == Names.SEMICOLON)
         advance();
      else throw new BadVariableException(Names.SEMICOLON, this.getLookahead(), this.scanner.getLinha());
   }

   private void varDeclOpt() {
      if (IsFirst.varDeclOpt(this.getLookahead())) {
         advance();

         if (this.getLookahead() == Names.ID) {
            advance();
            varDeclOpt();
         } else throw new BadVariableException(Names.ID, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private void type() {
      if (IsFirst.type(this.getLookahead()))
         advance();
   }

   private void constructDeclListOpt() {
      if (IsFirst.constructor(this.getLookahead()))
         constructDeclList();
   }

   private void constructDeclList() {
      constructDecl();

      if (IsFirst.constructor(this.getLookahead()))
         constructDeclList();
   }

   private void constructDecl() {
      if (IsFirst.constructor(this.getLookahead())) {
         advance();

         if (IsFirst.methodBody(this.getLookahead()))
            methodBody();
         else throw new BadConstructorException(Names.LPAREN, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private void methodDeclListOpt() {
      if (IsFirst.type(this.getLookahead()))
         methodDeclList();
   }

   private void methodDeclList() {
      methodDecl();

      if (IsFirst.type(this.getLookahead()))
         methodDeclList();
   }

   private void methodDecl() {
      type();

      if (this.getLookahead() == Names.LSBR) {
         advance();

         if (this.getLookahead() == Names.RSBR)
            advance();
         else throw new BadMethodException(Names.RSBR, this.getLookahead(), this.scanner.getLinha());
      }

      if (this.getLookahead() == Names.ID)
         advance();
      else throw new BadMethodException(Names.ID, this.getLookahead(), this.scanner.getLinha());

      if (IsFirst.methodBody(this.getLookahead()))
         methodBody();
      else throw new BadMethodException(Names.LPAREN, this.getLookahead(), this.scanner.getLinha());
   }

   private void methodBody() {
      if (IsFirst.methodBody(this.getLookahead())) {
         advance();

         paramListOpt();

         if (this.getLookahead() == Names.RPAREN)
            advance();
         else throw new BadMethodBodyException(Names.RPAREN, this.getLookahead(), this.scanner.getLinha());

         if (this.getLookahead() == Names.LCBR) {
            advance();

            statementsOpt();

            if (this.getLookahead() == Names.RCBR)
               advance();
            else throw new BadMethodBodyException(Names.RCBR, this.getLookahead(), this.scanner.getLinha());

         } else throw new BadMethodBodyException(Names.LCBR, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private void paramListOpt() {
      if (IsFirst.type(this.getLookahead()))
         paramList();
   }

   private void paramList() {
      param();

      if (this.getLookahead() == Names.COMMA) {
         advance();

         if (IsFirst.type(this.getLookahead()))
            paramList();
         else throw new BadParamException(null, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private void param() {
      type();

      if (this.getLookahead() == Names.LSBR) {
         advance();

         if (this.getLookahead() == Names.RSBR)
            advance();
         else throw new BadParamException(Names.RSBR, this.getLookahead(), this.scanner.getLinha());
      }

      if (this.getLookahead() == Names.ID)
         advance();
      else throw new BadParamException(Names.ID, this.getLookahead(), this.scanner.getLinha());
   }

   private void statementsOpt() {
      if (IsFirst.statement(this.getLookahead()))
         statements();
   }

   private void statements() {
      statement();

      if (IsFirst.statement(this.getLookahead()))
         statements();
   }

   private void statement() {
      if (IsFirst.atribStat(this.getLookahead())) {
         atribStat();

         if (this.encontrouAmbiguidade) {
            toggleEncontrouAmbiguidade();
            varDecl_line();
         }
      }

      else if (IsFirst.type(this.getLookahead()))
         varDeclList();

      else if (IsFirst.print(this.getLookahead())) {
         printStat();

         if (this.getLookahead() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.getLookahead(), this.scanner.getLinha());
      }

      else if (IsFirst.read(this.getLookahead())) {
         readStat();

         if (this.getLookahead() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.getLookahead(), this.scanner.getLinha());
      }

      else if (IsFirst.returnStat(this.getLookahead())) {
         returnStat();

         if (this.getLookahead() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.getLookahead(), this.scanner.getLinha());
      }

      else if (IsFirst.superStat(this.getLookahead())) {
         superStat();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.getLookahead(), this.scanner.getLinha());
      }

      else if (IsFirst.ifStat(this.getLookahead()))
         ifStat();

      else if (IsFirst.forStat(this.getLookahead()))
         forStat();

      else if (IsFirst.breakStat(this.getLookahead())) {
         advance();

         if (this.getLookahead() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.getLookahead(), this.scanner.getLinha());
      }

      else if (this.getLookahead() == Names.SEMICOLON)
         advance();

      else throw new BadStatementException(Names.SEMICOLON, this.getLookahead(), this.scanner.getLinha());
   }

   private void printStat() {
      if (IsFirst.print(this.getLookahead())) {
         advance();
         expression();
      }
   }

   private void readStat() {
      if (IsFirst.read(this.getLookahead())) {
         advance();
         expression();
      }
   }

   private void returnStat() {
      if (IsFirst.returnStat(this.getLookahead())) {
         advance();
         expression();
      }
   }

   private void superStat() {
      if (IsFirst.superStat(this.getLookahead())) {
         advance();

         if (this.getLookahead() == Names.LPAREN) {
            advance();

            argListOpt();

            if (this.getLookahead() == Names.RPAREN)
               advance();
            else throw new BadSuperStatException(Names.RPAREN, this.getLookahead(), this.scanner.getLinha());

         } else throw new BadSuperStatException(Names.LPAREN, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private void ifStat() {
      if (IsFirst.ifStat(this.getLookahead())) {
         advance();

         if (this.getLookahead() == Names.LPAREN)
            advance();
         else throw new BadIfException(Names.LPAREN, this.getLookahead(), this.scanner.getLinha());

         expression();

         if (this.getLookahead() == Names.RPAREN)
            advance();
         else throw new BadIfException(Names.RPAREN, this.getLookahead(), this.scanner.getLinha());

         if (this.getLookahead() == Names.LCBR)
            advance();
         else throw new BadIfException(Names.LCBR, this.getLookahead(), this.scanner.getLinha());

         statements();

         if (this.getLookahead() == Names.RCBR)
            advance();
         else throw new BadIfException(Names.RCBR, this.getLookahead(), this.scanner.getLinha());

         if (this.getLookahead() == Names.ELSE)
            ifState_line();
      }
   }

   private void ifState_line() {
      if (this.getLookahead() == Names.ELSE) {
         advance();

         if (this.getLookahead() == Names.LCBR) {
            advance();

            statements();

            if (this.getLookahead() == Names.RCBR)
               advance();
            else throw new BadIfException(Names.RCBR, this.getLookahead(), this.scanner.getLinha());

         } else throw new BadIfException(Names.RCBR, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private void forStat() {
      if (IsFirst.forStat(this.getLookahead())) {
         advance();

         if (this.getLookahead() == Names.LPAREN)
            advance();
         else throw new BadForException(Names.LPAREN, this.getLookahead(), this.scanner.getLinha());

         atribStatOpt();

         if (this.getLookahead() == Names.SEMICOLON)
            advance();
         else throw new BadForException(Names.SEMICOLON, this.getLookahead(), this.scanner.getLinha());

         expressionOpt();

         if (this.getLookahead() == Names.SEMICOLON)
            advance();
         else throw new BadForException(Names.SEMICOLON, this.getLookahead(), this.scanner.getLinha());

         atribStatOpt();

         if (this.getLookahead() == Names.RPAREN)
            advance();
         else throw new BadForException(Names.RPAREN, this.getLookahead(), this.scanner.getLinha());

         if (this.getLookahead() == Names.LCBR)
            advance();
         else throw new BadForException(Names.LCBR, this.getLookahead(), this.scanner.getLinha());

         statements();

         if (this.getLookahead() == Names.RCBR)
            advance();
         else throw new BadForException(Names.RCBR, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private void atribStatOpt() {
      if (IsFirst.atribStat(this.getLookahead()))
         atribStat();
   }

   private void atribStat() {
      lValue();

      if (this.encontrouAmbiguidade)
         return;

      if (this.getLookahead() == Names.ATTR)
         advance();
      else throw new BadAtribStatException(Names.ATTR, this.getLookahead(), this.scanner.getLinha());

      if (IsFirst.unaryExpression(this.getLookahead()))
         expression();
      else if (IsFirst.allocExpression(this.getLookahead()))
         allocExpression();
      else throw new BadAtribStatException(Names.MAIS, this.getLookahead(), this.scanner.getLinha());

   }

   private void allocExpression() {
      if (this.getLookahead() == Names.NEW) {
         advance();

         if (this.getLookahead() == Names.ID) {
            advance();

            if (this.getLookahead() == Names.LPAREN) {
               advance();

               argListOpt();

               if (this.getLookahead() == Names.RPAREN)
                  advance();
               else throw new BadAllocExpressionException(Names.RPAREN, this.getLookahead(), this.scanner.getLinha());

            } else throw new BadAllocExpressionException(Names.LPAREN, this.getLookahead(), this.scanner.getLinha());

         } else throw new BadAllocExpressionException(Names.ID, this.getLookahead(), this.scanner.getLinha());

      } else if (IsFirst.type(this.getLookahead())) {
         advance();

         if (this.getLookahead() == Names.LSBR) {
            advance();

            expression();

            if (this.getLookahead() == Names.RSBR)
               advance();
            else throw new BadAllocExpressionException(Names.RSBR, this.getLookahead(), this.scanner.getLinha());

         } else throw new BadAllocExpressionException(Names.LSBR, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private void lValue() {
      if (this.getLookahead() == Names.ID) {
         advance();

         if (this.getLookahead() == Names.ID) {
            this.toggleEncontrouAmbiguidade();
            return;
         }

         if (this.getLookahead() == Names.LSBR) {
            advance();

            if (this.getLookahead() == Names.RSBR) {
               this.toggleEncontrouAmbiguidade();
               return;
            }

            expression();

            if (this.getLookahead() == Names.RSBR)
               advance();
            else throw new BadLeftValueException(Names.RSBR, this.getLookahead(), this.scanner.getLinha());
         }

         if (this.getLookahead() == Names.DOT) {
            advance();

            if (this.getLookahead() == Names.ID)
               lValue();
            else throw new BadLeftValueException(Names.RSBR, this.getLookahead(), this.scanner.getLinha());
         }
      }
   }

   private void argListOpt() {
      if (IsFirst.unaryExpression(this.getLookahead()))
         argList();
   }

   private void argList() {
      expression();

      if (this.getLookahead() == Names.COMMA) {
         advance();
         argList();
      }
   }

   private void expressionOpt() {
      if (IsFirst.unaryExpression(this.getLookahead()))
         expression();
   }

   private void expression() {
      numExpression();

      if (this.getLookahead() == Names.RELOP)
         expression();
   }

   private void numExpression() {
      term();

      if (IsFirst.unaryExpression(this.getLookahead())) {
         advance();
         term();
      }
   }

   private void term() {
      unaryExpression();

      if (this.getLookahead() == Names.VEZES
            || this.getLookahead() == Names.DIVIDIDO
            || this.getLookahead() == Names.MOD) {
         advance();
         unaryExpression();
      }
   }

   private void unaryExpression() {
      if (IsFirst.unaryExpression(this.getLookahead())) {
         advance();
         factor();
      } else throw new BadUnaryExpressionException(null, this.getLookahead(), this.scanner.getLinha());
   }

   private void factor() {
      if (this.getLookahead() == Names.INTEGER_LITERAL
            || this.getLookahead() == Names.STRING_LITERAL)
         advance();

      else if (this.getLookahead() == Names.ID)
         lValue();

      else if (this.getLookahead() == Names.LPAREN) {
         advance();
         expression();

         if (this.getLookahead() == Names.RPAREN)
            advance();
         else throw new BadFactorException(Names.RPAREN, this.getLookahead(), this.scanner.getLinha());
      }
   }

   private Names getLookahead() {
      return this.lToken.getName();
   }

   private void toggleEncontrouAmbiguidade() {
      this.encontrouAmbiguidade = !this.encontrouAmbiguidade;
   }
}

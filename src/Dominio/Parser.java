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

      if (this.lToken.getName() == Names.ID)
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
      if (this.getLookahead() == Names.ID) {
         atribStat();

         if (this.encontrouAmbiguidade) {
            toggleEncontrouAmbiguidade();
            varDecl_line();
         }
      }
      else if (IsFirst.type(this.getLookahead()))
         varDeclList();

      else if (this.lToken.getName() == Names.PRINT) {
         printStat();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());
      }

      else if (this.lToken.getName() == Names.READ) {
         readStat();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());
      }

      else if (this.lToken.getName() == Names.RETURN) {
         returnStat();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());
      }

      else if (this.lToken.getName() == Names.SUPER) {
         superStat();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());
      }

      else if (this.lToken.getName() == Names.IF)
         ifStat();

      else if (this.lToken.getName() == Names.FOR)
         forStat();

      else if (this.lToken.getName() == Names.BREAK) {
         advance();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadStatementException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());
      }

      else if (this.lToken.getName() == Names.SEMICOLON)
         advance();

      else throw new BadStatementException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());
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
         expression();
      }
   }

   private void returnStat() {
      if (this.lToken.getName() == Names.RETURN) {
         advance();
         expression();
      }
   }

   private void superStat() {
      if (this.lToken.getName() == Names.SUPER) {
         advance();

         if (this.lToken.getName() == Names.LPAREN) {
            advance();

            argListOpt();

            if (this.lToken.getName() == Names.RPAREN)
               advance();
            else throw new BadSuperStatException(Names.RPAREN, this.lToken.getName(), this.scanner.getLinha());
         } else throw new BadSuperStatException(Names.LPAREN, this.lToken.getName(), this.scanner.getLinha());
      }
   }

   private void ifStat() {
      if (this.lToken.getName() == Names.IF) {
         advance();

         if (this.lToken.getName() == Names.LPAREN)
            advance();
         else throw new BadIfException(Names.LPAREN, this.lToken.getName(), this.scanner.getLinha());

         expression();

         if (this.lToken.getName() == Names.RPAREN)
            advance();
         else throw new BadIfException(Names.RPAREN, this.lToken.getName(), this.scanner.getLinha());

         if (this.lToken.getName() == Names.LCBR)
            advance();
         else throw new BadIfException(Names.LCBR, this.lToken.getName(), this.scanner.getLinha());

         statements();

         if (this.lToken.getName() == Names.RCBR)
            advance();
         else throw new BadIfException(Names.RCBR, this.lToken.getName(), this.scanner.getLinha());

         if (this.lToken.getName() == Names.ELSE)
            ifState_line();
      }
   }

   private void ifState_line() {
      if (this.lToken.getName() == Names.ELSE) {
         advance();

         if (this.lToken.getName() == Names.LCBR) {
            advance();

            statements();

            if (this.lToken.getName() == Names.RCBR)
               advance();
            else throw new BadIfException(Names.RCBR, this.lToken.getName(), this.scanner.getLinha());
         } else throw new BadIfException(Names.RCBR, this.lToken.getName(), this.scanner.getLinha());
      }
   }

   private void forStat() {
      if (this.lToken.getName() == Names.FOR) {
         advance();

         if (this.lToken.getName() == Names.LPAREN)
            advance();
         else throw new BadForException(Names.LPAREN, this.lToken.getName(), this.scanner.getLinha());

         atribStatOpt();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadForException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());

         expressionOpt();

         if (this.lToken.getName() == Names.SEMICOLON)
            advance();
         else throw new BadForException(Names.SEMICOLON, this.lToken.getName(), this.scanner.getLinha());

         atribStatOpt();

         if (this.lToken.getName() == Names.RPAREN)
            advance();
         else throw new BadForException(Names.RPAREN, this.lToken.getName(), this.scanner.getLinha());

         if (this.lToken.getName() == Names.LCBR)
            advance();
         else throw new BadForException(Names.LCBR, this.lToken.getName(), this.scanner.getLinha());

         statements();

         if (this.lToken.getName() == Names.RCBR)
            advance();
         else throw new BadForException(Names.RCBR, this.lToken.getName(), this.scanner.getLinha());

      }
   }

   private void atribStatOpt() {
      if (this.lToken.getName() == Names.ID)
         atribStat();
   }

   private void atribStat() {
      lValue();

      if (this.encontrouAmbiguidade)
         return;

      if (this.lToken.getName() == Names.ATTR)
         advance();
      else throw new BadAtribStatException(Names.ATTR, this.lToken.getName(), this.scanner.getLinha());

      if (this.lToken.getName() == Names.MAIS
            || this.lToken.getName() == Names.MENOS)
         expression();
      else if (this.lToken.getName() == Names.NEW
            || this.lToken.getName() == Names.INT
            || this.lToken.getName() == Names.STRING_LITERAL
            || this.lToken.getName() == Names.ID)
         allocExpression();
      else throw new BadAtribStatException(Names.MAIS, this.lToken.getName(), this.scanner.getLinha());

   }

   private void allocExpression() {
      if (this.lToken.getName() == Names.NEW) {
         advance();

         if (this.lToken.getName() == Names.ID) {
            advance();

            if (this.lToken.getName() == Names.LPAREN) {
               advance();

               argListOpt();

               if (this.lToken.getName() == Names.RPAREN)
                  advance();
               else throw new BadAllocExpressionException(Names.RPAREN, this.lToken.getName(), this.scanner.getLinha());

            } else throw new BadAllocExpressionException(Names.LPAREN, this.lToken.getName(), this.scanner.getLinha());

         } else throw new BadAllocExpressionException(Names.ID, this.lToken.getName(), this.scanner.getLinha());

      } else if (this.lToken.getName() == Names.INT
            || this.lToken.getName() == Names.STRING_LITERAL
            || this.lToken.getName() == Names.ID) {
         advance();

         if (this.lToken.getName() == Names.LSBR) {
            advance();

            expression();

            if (this.lToken.getName() == Names.RSBR)
               advance();
            else throw new BadAllocExpressionException(Names.RSBR, this.lToken.getName(), this.scanner.getLinha());

         } else throw new BadAllocExpressionException(Names.LSBR, this.lToken.getName(), this.scanner.getLinha());
      }
   }

   private void lValue() {
      if (this.lToken.getName() == Names.ID) {
         advance();

         if (this.lToken.getName() == Names.ID) {
            this.toggleEncontrouAmbiguidade();
            return;
         }

         if (this.lToken.getName() == Names.LSBR) {
            advance();

            if (this.getLookahead() == Names.RSBR) {
               this.toggleEncontrouAmbiguidade();
               return;
            }

            expression();

            if (this.lToken.getName() == Names.RSBR)
               advance();
            else throw new BadLeftValueException(Names.RSBR, this.lToken.getName(), this.scanner.getLinha());
         }

         if (this.lToken.getName() == Names.DOT) {
            advance();

            if (this.lToken.getName() == Names.ID)
               lValue();
            else throw new BadLeftValueException(Names.RSBR, this.lToken.getName(), this.scanner.getLinha());
         }
      }
   }

   private void argListOpt() {
      if (this.lToken.getName() == Names.MAIS
            || this.lToken.getName() == Names.MENOS)
         argList();
   }

   private void argList() {
      expression();

      if (this.lToken.getName() == Names.COMMA) {
         advance();
         argList();
      }
   }

   private void expressionOpt() {
      if (this.lToken.getName() == Names.MAIS
            || this.lToken.getName() == Names.MENOS)
         expression();
   }

   private void expression() {
      numExpression();

      if (this.lToken.getName() == Names.RELOP)
         expression();
   }

   private void numExpression() {
      term();

      if (this.lToken.getName() == Names.MAIS
            || this.lToken.getName() == Names.MENOS) {
         advance();
         term();
      }
   }

   private void term() {
      unaryExpression();

      if (this.lToken.getName() == Names.VEZES
            || this.lToken.getName() == Names.DIVIDIDO
            || this.lToken.getName() == Names.MOD) {
         advance();
         unaryExpression();
      }
   }

   private void unaryExpression() {
      if (this.lToken.getName() == Names.MAIS
            || this.lToken.getName() == Names.MENOS) {
         advance();
         factor();
      } else throw new BadUnaryExpressionException(null, this.lToken.getName(), this.scanner.getLinha());
   }

   // falta LValue
   private void factor() {
      if (this.lToken.getName() == Names.INTEGER_LITERAL
            || this.lToken.getName() == Names.STRING_LITERAL)
         advance();

      else if (this.lToken.getName() == Names.LPAREN) {
         advance();
         expression();

         if (this.lToken.getName() == Names.RPAREN)
            advance();
         else throw new BadFactorException(Names.RPAREN, this.lToken.getName(), this.scanner.getLinha());
      }
   }

   private Names getLookahead() {
      return this.lToken.getName();
   }

   public boolean isEncontrouAmbiguidade() {
      return encontrouAmbiguidade;
   }

   public void toggleEncontrouAmbiguidade() {
      this.encontrouAmbiguidade = !this.encontrouAmbiguidade;
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

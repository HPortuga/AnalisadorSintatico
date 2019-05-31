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
         classList_line();
   }

   private void classList_line() {
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
         varDeclList_line();
   }

   private void varDeclList_line() {
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
         constructDeclList_line();
   }

   private void constructDeclList_line() {
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
      methodDeclList();
   }

   private void methodDeclList() {
      methodDecl();
      methodDeclList_line();
   }

   private void methodDeclList_line() {
      methodDeclList();
      methodDecl();
   }

   private void methodDecl() {
      type();
      methodDecl_line();
   }

   private void methodDecl_line() {
      if (this.lToken.getName() == Names.ID) {
         advance();
         methodBody();
      }
      else if (this.lToken.getName() == Names.LSBR) {
         advance();
         match(Names.RSBR);
         methodBody();
      }
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
//            statementsOpt();

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

      if (this.lToken.getName() == Names.COMMA)
         paramList_line();
   }

   private void paramList_line() {
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

   private void param_line() {
      if (this.lToken.getName() == Names.ID)
         advance();
      else if (this.lToken.getName() == Names.LSBR) {
         advance();
         match(Names.RSBR);
         if (this.lToken.getName() == Names.ID)
            advance();
      }
   }

   private void statementsOpt() {
      if (this.lToken.getName() != Names.RCBR)
         statements();
   }

   private void statements() {
      statement();
      statements_line();
   }

   private void statements_line() {
      statements();
      statement();
   }

   public void statement() {
      varDeclList();

      atribStat();
      if (this.lToken.getName() == Names.SEMICOLON)
         advance();

      printStat();
      if (this.lToken.getName() == Names.SEMICOLON)
         advance();

      readStat();
      if (this.lToken.getName() == Names.SEMICOLON)
         advance();

      returnStat();
      if (this.lToken.getName() == Names.SEMICOLON)
         advance();

      superStat();
      if (this.lToken.getName() == Names.SEMICOLON)
         advance();

      ifStat();
      forStat();

      if (this.lToken.getName() == Names.BREAK) {
         advance();
         match(Names.SEMICOLON);
      }

      if (this.lToken.getName() == Names.SEMICOLON)
         advance();
   }

   private void atribStat() {
      lValue();
      if (this.lToken.getName() == Names.ATTR) {
         advance();
         atribStat_line();
      }
   }

   private void atribStat_line() {
      expression();
      allocExpresion();
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
            match(Names.RPAREN);
         }
      }
   }

   private void ifStat() {
      if (this.lToken.getName() == Names.IF) {
         advance();
         if (this.lToken.getName() == Names.LPAREN) {
            advance();
            expression();
            match(Names.RPAREN);
            if (this.lToken.getName() == Names.LCBR) {
               advance();
               statements();
               match(Names.RCBR);
               ifStat_line();
            }
         }
      }
   }

   private void ifStat_line() {
      if (this.lToken.getName() == Names.ELSE) {
         advance();
         if (this.lToken.getName() == Names.LCBR) {
            advance();
            statements();
            match(Names.RCBR);
         }
      }
   }

   private void forStat() {
      if (this.lToken.getName() == Names.FOR) {
         advance();
         if (this.lToken.getName() == Names.LPAREN) {
            advance();
            atribStatOpt();
            if (this.lToken.getName() == Names.SEMICOLON) {
               advance();
               expressionOpt();
               if (this.lToken.getName() == Names.SEMICOLON) {
                  advance();
                  atribStatOpt();
                  match(Names.RPAREN);
                  if (this.lToken.getName() == Names.LCBR) {
                     advance();
                     statements();
                     match(Names.RCBR);
                  }
               }
            }
         }
      }
   }

   private void atribStatOpt() {
      atribStat();
   }

   private void expressionOpt() {
      expression();
   }

   private void lValue() {
      if (this.lToken.getName() == Names.ID) {
         advance();
         lValue_Line();
         lValueComp();
      }
   }

   private void lValue_Line() {
      if (this.lToken.getName() == Names.LSBR) {
         advance();
         expression();
         match(Names.RSBR);
         lValueComp();
      }
   }

   private void lValueComp() {
      if (this.lToken.getName() == Names.DOT) {
         advance();
         if (this.lToken.getName() == Names.ID) {
            advance();
            lValueComp();
            lValueComp_line();
         }
      }
   }

   private void lValueComp_line() {
      if (this.lToken.getName() == Names.LSBR) {
         advance();
         expression();
         match(Names.RSBR);
         lValueComp();
      }
   }

   private void expression() {
      numExpression();
      expression_line();
   }

   private void expression_line() {
      if (this.lToken.getName() == Names.RELOP) {
         advance();
         numExpression();
      }
   }

   private void allocExpresion() {
      if (this.lToken.getName() == Names.NEW) {
         advance();
         if (this.lToken.getName() == Names.ID) {
            advance();
            if (this.lToken.getName() == Names.LPAREN) {
               advance();
               argListOpt();
               match(Names.RPAREN);
            }
         }
      }

      allocExpresion_line();
   }

   private void allocExpresion_line() {
      type();
      if (this.lToken.getName() == Names.LSBR) {
         advance();
         expression();
         match(Names.RSBR  );
      }
   }

   private void numExpression() {
      term();
      numExpression_line();
   }

   private void numExpression_line() {
      if (this.lToken.getName() == Names.OPNUM) {
         advance();
         term();
         numExpression_line();
      }
   }

   private void term() {
      unaryExpression();
      term_line();
   }

   private void term_line() {
      if (this.lToken.getName() == Names.OPUN) {
         advance();
         unaryExpression();
         term_line();
      }
   }

   private void unaryExpression() {
      if (this.lToken.getName() == Names.OPNUM) {
         advance();
         factor();
      }
   }

   private void factor() {
      if (this.lToken.getName() == Names.INTEGER_LITERAL
            || this.lToken.getName() == Names.STRING) {
         advance();
      }
      else if (this.lToken.getName() == Names.LPAREN) {
         advance();
         expression();
         match(Names.RPAREN);
      }
      else if (this.lToken.getName() == Names.ID) {
         lValue();
      }
   }

   private void argListOpt() {
      argList();
   }

   private void argList() {
      expression();
      argList_line();
   }

   private void argList_line() {
      if (this.lToken.getName() == Names.COMMA) {
         advance();
         expression();
      }
   }

}

package Dominio;

import Exceptions.BadClassException;
import Exceptions.CompilerException;
import Nomes.Gramatica;
import Nomes.Names;

import java.util.Stack;

public class Parser {
   private Token lToken;      // Dominio.Token de lookahead
   private Scanner scanner;
   private Stack<Gramatica> pilha;

   public Parser(String input) {
      this.scanner = new Scanner(input);
      this.pilha = new Stack<>();
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

         if (this.lToken.getName() == Names.INT
         || this.lToken.getName() == Names.STRING
         || this.lToken.getName() == Names.ID)
            varDeclListOpt();

         if (this.lToken.getName() == Names.CONSTRUCTOR)
            constructDeclListOpt();

         if (this.lToken.getName() == Names.RCBR)
            advance();
         else throw new CompilerException("Corpo de classe mal formado; Esperado: }, apareceu: " + this.lToken.getName());
      } else
         throw new CompilerException("Corpo de classe mal formado; Esperado: {, apareceu: " + this.lToken.getName()
               + " na linha " + this.scanner.getLinha());
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
         match(Names.RSBR);
      }

      if (this.lToken.getName() == Names.ID)
         advance();

      if (this.lToken.getName() == Names.COMMA)
         varDeclOpt();

      match(Names.SEMICOLON);
   }

   private void varDeclOpt() {
      if (this.lToken.getName() == Names.COMMA) {
         advance();
         if (this.lToken.getName() == Names.ID) {
            advance();
            varDeclOpt();
         }
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
         methodBody();
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
         match(Names.RPAREN);
         if (this.lToken.getName() == Names.LCBR) {
            advance();
            statementsOpt();
            match(Names.RCBR);
         }
      }
      else
         throw new CompilerException("Corpo de metodo mal formado; Esperado: (, encontrado: " + this.lToken.getName()
         + " na linha " + this.scanner.getLinha());
   }

   private void paramListOpt() {
      if (this.lToken.getName() == Names.INT
      || this.lToken.getName() == Names.STRING
      || this.lToken.getName() == Names.ID)
         paramList();
   }

   private void paramList() {
      param();
      paramList_line();
   }

   private void paramList_line() {
      paramList();
      if (this.lToken.getName() == Names.COMMA)
         param();
   }

   private void param() {
      type();
      param_line();
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
         pilha.push(Gramatica.LVALUE);
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
            pilha.push(Gramatica.LVALUECOMP);
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
      pilha.push(Gramatica.EXPRESSION);
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
      pilha.push(Gramatica.NUM_EXPRESSION);
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
      pilha.push(Gramatica.TERM);
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
         pilha.push(Gramatica.UNARY_EXPRESSION);
      }
   }

   private void factor() {
      if (this.lToken.getName() == Names.INTEGER_LITERAL
            || this.lToken.getName() == Names.STRING) {
         advance();
         pilha.push(Gramatica.FACTOR);
      }
      else if (this.lToken.getName() == Names.LPAREN) {
         advance();
         expression();
         match(Names.RPAREN);
         pilha.push(Gramatica.FACTOR);
      }
      else if (this.lToken.getName() == Names.ID) {
         lValue();
         pilha.push(Gramatica.FACTOR);
      }
   }

   private void argListOpt() {
      argList();
      pilha.push(Gramatica.ARGLISTOPT);
   }

   private void argList() {
      expression();
      argList_line();
      pilha.push(Gramatica.ARGLIST);
   }

   private void argList_line() {
      if (this.lToken.getName() == Names.COMMA) {
         advance();
         expression();
      }
   }

   public Stack<Gramatica> getPilha() {
      return pilha;
   }
}

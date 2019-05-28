import java.util.Stack;

public class Parser {
   private Token lToken;      // Token de lookahead
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
      else throw new CompilerException("Token inesperado: " + this.lToken.getName());
   }

   public void program() {
      while (lToken.getName() != Names.EOF) {
         expression();
         advance();
      }
   }

   private void classList() {
      classDecl();
      classList_line();
   }

   private void classList_line() {
      classDecl();
      classList();
   }

   private void classDecl() {

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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

   @Test
   void deve_reconhecer_FACTOR_INTEGER_LITERAL() {
      Parser parser = new Parser("+123");
      Gramatica naoTerminalEsperado = Gramatica.FACTOR;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().firstElement();
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_FACTOR_STRING_LITERAL() {
      Parser parser = new Parser("-\"abc\"");
      Gramatica naoTerminalEsperado = Gramatica.FACTOR;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().firstElement();
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_FACTOR_EXPRESSION_com_parenteses() {
      Parser parser = new Parser("-(+4-+2)");
      Gramatica naoTerminalEsperado = Gramatica.FACTOR;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(8);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_UNARY_EXPRESSION_positiva() {
      Parser parser = new Parser("+123");
      Gramatica naoTerminalEsperado = Gramatica.UNARY_EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(1);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_UNARY_EXPRESSION_negativa() {
      Parser parser = new Parser("-123");
      Gramatica naoTerminalEsperado = Gramatica.UNARY_EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(1);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_TERM_simples() {
      Parser parser = new Parser("-123");
      Gramatica naoTerminalEsperado = Gramatica.TERM;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(2);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_TERM_multiplicacao() {
      Parser parser = new Parser("+123 * -123");
      Gramatica naoTerminalEsperado = Gramatica.TERM;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(4);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_TERM_divisao() {
      Parser parser = new Parser("+123 / +123");
      Gramatica naoTerminalEsperado = Gramatica.TERM;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(4);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_TERM_modulo() {
      Parser parser = new Parser("+123 % +123");
      Gramatica naoTerminalEsperado = Gramatica.TERM;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(4);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_NUM_EXPRESSION_simples() {
      Parser parser = new Parser("+123");
      Gramatica naoTerminalEsperado = Gramatica.NUM_EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(3);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_NUM_EXPRESSION_soma() {
      Parser parser = new Parser("+123 + +123");
      Gramatica naoTerminalEsperado = Gramatica.NUM_EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(6);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_NUM_EXPRESSION_subtracao() {
      Parser parser = new Parser("+123 - +123");
      Gramatica naoTerminalEsperado = Gramatica.NUM_EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(6);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_EXPRESSION_simples() {
      Parser parser = new Parser("+123");
      Gramatica naoTerminalEsperado = Gramatica.EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().lastElement();
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_EXPRESSION_com_TERM() {
      Parser parser = new Parser("+123 * +123");
      Gramatica naoTerminalEsperado = Gramatica.EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().lastElement();
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_EXPRESSION_com_NUM_EXPRESSION() {
      Parser parser = new Parser("+123 * -123");
      Gramatica naoTerminalEsperado = Gramatica.EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().lastElement();
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_EXPRESSION_com_TERM_e_NUM_EXPRESSION() {
      Parser parser = new Parser("+123 - +123 * -123 + -123");
      Gramatica naoTerminalEsperado = Gramatica.EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().lastElement();
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_EXPRESSION_com_RELOP() {
      Parser parser = new Parser("-5 >= +10");
      Gramatica naoTerminalEsperado = Gramatica.EXPRESSION;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().lastElement();
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_LVALUE_simples() {
      Parser parser = new Parser("+_abc");
      Gramatica naoTerminalEsperado = Gramatica.LVALUE;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().firstElement();
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_LValue_EXPRESSION() {
      Parser parser = new Parser("+id[+6]");
      Gramatica naoTerminalEsperado = Gramatica.LVALUE;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(5);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_LVALUECOMP_simples() {
      Parser parser = new Parser("+id.id");
      Gramatica naoTerminalEsperado = Gramatica.LVALUECOMP;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().firstElement();
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_LVALUECOMP_EXPRESSION() {
      Parser parser = new Parser("+id.id[+7]");
      Gramatica naoTerminalEsperado = Gramatica.LVALUECOMP;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(5);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }

   @Test
   void deve_reconhecer_LVALUE_EXPRESSION_LVALUECOMP_EXPRESSION_LVALUECOMP() {
      Parser parser = new Parser("+id[+0].abc[-\"s\"].i");
      Gramatica naoTerminalEsperado = Gramatica.LVALUE;

      parser.program();

      Gramatica naoTerminalRetornado = parser.getPilha().elementAt(12);
      assertEquals(naoTerminalEsperado, naoTerminalRetornado);
   }
}

















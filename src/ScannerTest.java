import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ScannerTest {
   private Token eof;

   @BeforeEach
   void inicializar() {
      eof = new Token(Names.EOF, null);
   }

   @Test
   void deve_retornar_EOF_caso_input_vazio() {
      Scanner scanner = new Scanner("");
      List<String> listaEsperada = Collections.singletonList(eof.toString());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_um_ID() {
      Scanner scanner = new Scanner("U");
      List<String> listaEsperada = Stream.of(
            new Token(Names.ID, "U"), eof)
            .map(Token::toString)
            .collect(Collectors.toList());
      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_ignorar_espacos() {
      Scanner scanner = new Scanner("        ");
      List<String> listaEsperada = Collections.singletonList(eof.toString());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_IDs() {
      Scanner scanner = new Scanner("Aba aba a8s _A _a _3123 o G _");
      List<String> listaEsperada = Stream.of(
            new Token(Names.ID, "Aba"),
            new Token(Names.ID, "aba"),
            new Token(Names.ID, "a8s"),
            new Token(Names.ID, "_A"),
            new Token(Names.ID, "_a"),
            new Token(Names.ID, "_3123"),
            new Token(Names.ID, "o"),
            new Token(Names.ID, "G"),
            new Token(Names.ID, "_"),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_um_INTEGER_LITERAL() {
      Scanner scanner = new Scanner("1");
      List<String> listaEsperada = Stream.of(
            new Token(Names.INTEGER_LITERAL, "1"), eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_INTEGER_LITERALs() {
      Scanner scanner = new Scanner("4589 8 5654879841 456");
      List<String> listaEsperada = Stream.of(
            new Token(Names.INTEGER_LITERAL, "4589"),
            new Token(Names.INTEGER_LITERAL, "8"),
            new Token(Names.INTEGER_LITERAL, "5654879841"),
            new Token(Names.INTEGER_LITERAL, "456"),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_RELOP_menor_e_maior() {
      Scanner scanner = new Scanner("<> > <");
      List<String> listaEsperada = Stream.of(
            new Token(Names.RELOP, "<"),
            new Token(Names.RELOP, ">"),
            new Token(Names.RELOP, ">"),
            new Token(Names.RELOP, "<"),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_RELOP_menorigual_e_maiorigual() {
      Scanner scanner = new Scanner("<=>= <= >=");
      List<String> listaEsperada = Stream.of(
            new Token(Names.RELOP, "<="),
            new Token(Names.RELOP, ">="),
            new Token(Names.RELOP, "<="),
            new Token(Names.RELOP, ">="),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_ATTR() {
      Scanner scanner = new Scanner("= <= >=");
      List<String> listaEsperada = Stream.of(
            new Token(Names.ATTR, "="),
            new Token(Names.RELOP, "<="),
            new Token(Names.RELOP, ">="),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_RELOP_igualdade() {
      Scanner scanner = new Scanner("== <= = >====<<=");
      List<String> listaEsperada = Stream.of(
            new Token(Names.RELOP, "=="),
            new Token(Names.RELOP, "<="),
            new Token(Names.ATTR, "="),
            new Token(Names.RELOP, ">="),
            new Token(Names.RELOP, "=="),
            new Token(Names.ATTR, "="),
            new Token(Names.RELOP, "<"),
            new Token(Names.RELOP, "<="),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_RELOP_desigualdade() {
      Scanner scanner = new Scanner("!= ==!= <= != >====!=");
      List<String> listaEsperada = Stream.of(
            new Token(Names.RELOP, "!="),
            new Token(Names.RELOP, "=="),
            new Token(Names.RELOP, "!="),
            new Token(Names.RELOP, "<="),
            new Token(Names.RELOP, "!="),
            new Token(Names.RELOP, ">="),
            new Token(Names.RELOP, "=="),
            new Token(Names.ATTR, "="),
            new Token(Names.RELOP, "!="),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_OPNUM() {
      Scanner scanner = new Scanner("4 + 5 - +-");
      List<String> listaEsperada = Stream.of(
            new Token(Names.INTEGER_LITERAL, "4"),
            new Token(Names.OPNUM, "+"),
            new Token(Names.INTEGER_LITERAL, "5"),
            new Token(Names.OPNUM, "-"),
            new Token(Names.OPNUM, "+"),
            new Token(Names.OPNUM, "-"),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_OPUN() {
      Scanner scanner = new Scanner("5*4 /% 433 %asd");
      List<String> listaEsperada = Stream.of(
            new Token(Names.INTEGER_LITERAL, "5"),
            new Token(Names.OPUN, "*"),
            new Token(Names.INTEGER_LITERAL, "4"),
            new Token(Names.OPUN, "/"),
            new Token(Names.OPUN, "%"),
            new Token(Names.INTEGER_LITERAL, "433"),
            new Token(Names.OPUN, "%"),
            new Token(Names.ID, "asd"),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   // Separadores: LPAREN RPAREN LSBR RSBR LCBR RCBR COMMA DOT SEMICOLON
   @Test
   void deve_reconhecer_separadores() {
      Scanner scanner = new Scanner("(){} [ . ] ;,");
      List<String> listaEsperada = Stream.of(
            new Token(Names.LPAREN, "("),
            new Token(Names.RPAREN, ")"),
            new Token(Names.LCBR, "{"),
            new Token(Names.RCBR, "}"),
            new Token(Names.LSBR, "["),
            new Token(Names.DOT, "."),
            new Token(Names.RSBR, "]"),
            new Token(Names.SEMICOLON, ";"),
            new Token(Names.COMMA, ","),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_STRING() {
      Scanner scanner = new Scanner("\"HAHAHAH 8 9 0 as % 4\" as 5/");
      List<String> listaEsperada = Stream.of(
            new Token(Names.STRING, "\"HAHAHAH 8 9 0 as % 4\""),
            new Token(Names.ID, "as"),
            new Token(Names.INTEGER_LITERAL, "5"),
            new Token(Names.OPUN, "/"),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_comentarios_em_linha() {
      Scanner scanner = new Scanner("aba 89 // a [ ] ;\ncas0");
      List<String> listaEsperada = Stream.of(
            new Token(Names.ID, "aba"),
            new Token(Names.INTEGER_LITERAL, "89"),
            new Token(Names.ID, "cas0"),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   @Test
   void deve_reconhecer_comentarios_em_bloco() {
      Scanner scanner = new Scanner("asd /*isto\neh\num\ncomentario*/isto\nnão");
      List<String> listaEsperada = Stream.of(
            new Token(Names.ID, "asd"),
            new Token(Names.ID, "isto"),
            new Token(Names.ID, "não"),
            eof)
            .map(Token::toString)
            .collect(Collectors.toList());

      List<String> listaRetornada = analisar(scanner, listaEsperada);

      assertIterableEquals(listaEsperada, listaRetornada);
   }

   private List<String> analisar(Scanner scanner, List<String> listaEsperada) {
      List<String> listaRetornada = new ArrayList<>();

      Token t;
      do {
         t = scanner.nextToken();
         listaRetornada.add(t.toString());
      } while (t.getName() != Names.EOF);

      return listaRetornada;
   }
}

package Testes;

import Exceptions.BadClassException;
import Exceptions.CompilerException;
import Dominio.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {
   private String msgAnaliseConcluida = "Analise sintatica concluida";

   @Test
   void deve_identificar_erro_quando_input_vazio() {
      Parser parser = new Parser("");

      assertThrows(BadClassException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_classe_mal_formada() {
      Parser parser = new Parser("123");

      assertThrows(BadClassException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_classe_sem_ID() {
      Parser parser = new Parser("class 123");

      assertThrows(BadClassException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_corpo_de_classe_sem_LCBR() {
      Parser parser = new Parser("class batata batata");

      assertThrows(CompilerException.class, parser::program);
   }

   @Test
   void deve_reconhecer_uma_classe_simples() {
      Parser parser = new Parser("class batata {}");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_uma_classe_extendida() {
      Parser parser = new Parser("class Fruta {} class Abacaxi extends Fruta {}") ;

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_de_classe_extendida_sem_ID() {
      Parser parser = new Parser("class batata extends 123");

      assertThrows(BadClassException.class, parser::program);
   }

   @Test
   void deve_reconhecer_duas_classes_simples() {
      Parser parser = new Parser("class batata {} class abacaxi{}");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_segunda_classe_mal_formada() {
      Parser parser = new Parser("class batata {} 123");

      assertThrows(BadClassException.class, parser::program);
   }

   @Test
   void deve_reconhecer_duas_classes_extendidas() {
      Parser parser = new Parser("class Fruta {} class Banana extends Fruta {} class DaTerra extends Banana {}");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_de_classe_mal_formada_em_lista_de_classes() {
      Parser parser = new Parser("class batata {} class cenoura {} class 123 {}");

      assertThrows(BadClassException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_classe_extendida_sem_ID_em_lista_de_classes() {
      Parser parser = new Parser("class batata extends fruta{} class abacaxi extends 123");

      assertThrows(BadClassException.class, parser::program);
   }

   @Test
   void deve_reconhecer_declaracao_de_variavel_simples() {
      String codigo =
            "class Fruta {" +
               "int tamanho;" +
            "}";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_declaracao_de_variavel_vetor() {
      String codigo =
            "class Fruta {" +
               "string[] caracteristicas;" +
            "}";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_declaracao_de_mais_de_uma_variavel() {
      String codigo =
            "class Fruta {" +
               "int tamanho;" +
               "string descricao;" +
               "Tipos[] tipos;" +
            "}";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_declaracao_de_multiplas_variaveis_na_mesma_linha() {
      String codigo =
            "class Fruta {" +
               "int tamanho, peso, idade;" +
            "}";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_declaracao_de_classe_somente_com_construtor() {
      String codigo =
            "class Fruta {" +
               "constructor () {}" +
            "}";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_quando_constructor_sem_LPAREN() {
      String codigo =
            "class Fruta {" +
               "constructor )( {}" +
            "}";
      Parser parser = new Parser(codigo);

      assertThrows(CompilerException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_corpo_de_classe_sem_RCBR() {
      Parser parser = new Parser("class batata {");

      assertThrows(CompilerException.class, parser::program);
   }

}
















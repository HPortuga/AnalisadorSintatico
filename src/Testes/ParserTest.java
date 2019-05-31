package Testes;

import Dominio.Parser;
import Exceptions.*;
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
   void deve_identificar_erro_de_corpo_de_classe_sem_LCBR() {
      Parser parser = new Parser("class batata batata");

      assertThrows(BadClassBodyException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_corpo_de_classe_sem_RCBR() {
      Parser parser = new Parser("class batata {");

      assertThrows(BadClassBodyException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_variavel_mal_formada_com_ID_errado() {
      String codigo = "class Fruta { int 123; }";
      Parser parser = new Parser(codigo);

      assertThrows(BadVariableException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_variavel_sem_ponto_e_virgula() {
      String codigo = "class Fruta { int aba }";
      Parser parser = new Parser(codigo);

      assertThrows(BadVariableException.class, parser::program);
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
   void deve_identificar_erro_de_variavel_vetor_sem_RSBR() {
      String codigo = "class Fruta { int tamanho[; }";
      Parser parser = new Parser(codigo);

      assertThrows(BadVariableException.class, parser::program);
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
   void deve_identificar_erro_multiplas_variaveis_sem_ID() {
      String codigo =
            "class Fruta {" +
               "int tamanho, 123, idade;" +
            "}";
      Parser parser = new Parser(codigo);

      assertThrows(BadVariableException.class, parser::program);
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
   void deve_identificar_erro_de_construtor_sem_LPAREN() {
      String codigo = "class Fruta { constructor )( {} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadConstructorException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_corpo_do_metodo_construtor_sem_RPAREN() {
      String codigo = "class Fruta { constructor (( {} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadMethodBodyException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_corpo_do_metodo_construtor_sem_LCBR() {
      String codigo = "class Fruta { constructor () }} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadMethodBodyException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_corpo_do_metodo_construtor_sem_RCBR() {
      String codigo = "class Fruta { constructor () {{ }";
      Parser parser = new Parser(codigo);

      assertThrows(BadMethodBodyException.class, parser::program);
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
   void deve_identificar_erro_de_parametro_vetor_do_metodo_construdor_sem_RSBR() {
      String codigo = "class Fruta { constructor (int [[) {} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadParamException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_parametro_vetor_do_metodo_construtor_sem_ID() {
      String codigo = "class Fruta { constructor (int [] ) {} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadParamException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_parametro_do_metodo_construtor_sem_ID() {
      String codigo = "class Fruta { constructor (int 123) {} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadParamException.class, parser::program);
   }

   @Test
   void deve_reconhecer_parametro_simples_no_metodo_construtor() {
      String codigo = "class Fruta { constructor (int batata) {} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_parametro_vetor_no_metodo_construtor() {
      String codigo = "class Fruta { constructor (int []batata) {} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_de_segundo_parametro_do_construtor_sem_Type() {
      String codigo = "class Fruta { constructor (int 123,) {} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadParamException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_segundo_parametro_do_construtor_sem_ID() {
      String codigo = "class Fruta { constructor (int 123, int) {} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadParamException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_segundo_parametro_vetor_do_construtor_sem_ID() {
      String codigo = "class Fruta { constructor (int 123, int []) {} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadParamException.class, parser::program);
   }

   @Test
   void deve_reconhecer_parametros_no_metodo_construtor() {
      String codigo = "class Fruta { constructor (int []batata, string sanduiche, NovaClasse classe) {} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }
}

















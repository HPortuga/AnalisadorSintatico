import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {
   private String msgAnaliseConcluida = "Analise sintatica concluida";

   @Test
   void deve_reconhecer_uma_classe_simples() {
      Parser parser = new Parser("class batata {}");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_duas_classes_simples() {
      Parser parser = new Parser("class batata {} class abacaxi{}");

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
   void deve_reconhecer_duas_classes_extendidas() {
      Parser parser = new Parser("class Fruta {} class Banana extends Fruta {} class DaTerra extends Banana {}");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
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
      String codigo = "" +
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

}

















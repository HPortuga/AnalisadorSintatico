import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {
   private String msgAnaliseConcluida = "Analise sintatica concluida";

   @Test
   void deve_reconhecer_CLASS_LIST_com_uma_classe_simples() {
      Parser parser = new Parser("class batata");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_CLASS_LIST_com_duas_classes_simples() {
      Parser parser = new Parser("class batata class abacaxi");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_CLASS_LIST_com_uma_classe_extendida() {
      Parser parser = new Parser("class Fruta class Abacaxi extends Fruta") ;

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_CLASS_LIST_com_duas_classes_extendidas() {
      Parser parser = new Parser("class Fruta class Banana extends Fruta class DaTerra extends Banana");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }


}

















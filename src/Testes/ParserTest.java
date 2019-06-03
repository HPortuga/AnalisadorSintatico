package Testes;

import Dominio.Parser;
import Exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {
   private String msgAnaliseConcluida = "Analise sintatica concluida";

   @Test
   void deve_identificar_erro_de_programa_mal_formado() {
      Parser parser = new Parser("batata");

      assertThrows(BadProgramException.class, parser::program);
   }

   @Test
   void deve_reconhecer_input_vazio() {
      Parser parser = new Parser("");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_de_classe_sem_ID() {
      Parser parser = new Parser("class 123");

      assertThrows(BadClassException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_classe_sem_LCBR() {
      Parser parser = new Parser("class classe }");

      assertThrows(BadClassBodyException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_classe_sem_RCBR() {
      Parser parser = new Parser("class classe {");

      assertThrows(BadClassBodyException.class, parser::program);
   }

   @Test
   void deve_reconhecer_uma_classe_simples() {
      Parser parser = new Parser("class batata {}");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_de_classe_extendida_sem_ID() {
      Parser parser = new Parser("class batata extends 123");

      assertThrows(BadClassException.class, parser::program);
   }

   @Test
   void deve_reconhecer_uma_classe_extendida() {
      Parser parser = new Parser("class Fruta {} class Abacaxi extends Fruta {}") ;

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_segunda_classe_mal_formada() {
      Parser parser = new Parser("class batata {} 123");

      assertThrows(BadClassException.class, parser::program);
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
   void deve_reconhecer_duas_classes_simples() {
      Parser parser = new Parser("class batata {} class abacaxi{}");

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
   void deve_identificar_erro_de_variavel_com_ID_errado() {
      Parser parser = new Parser("class Fruta { int 123; }");

      assertThrows(BadVariableException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_variavel_sem_ponto_e_virgula() {
      Parser parser = new Parser("class Fruta { int aba }");

      assertThrows(BadVariableException.class, parser::program);
   }

   @Test
   void deve_reconhecer_declaracao_de_variavel_simples() {
      Parser parser = new Parser("class Fruta { int tamanho; }");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_de_variavel_vetor_sem_RSBR() {
      Parser parser = new Parser("class Fruta { int[ tamanho; }");

      assertThrows(BadVariableException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_variavel_vetor_sem_ID() {
      Parser parser = new Parser("class Fruta { int[] ; }");

      assertThrows(BadVariableException.class, parser::program);
   }

   @Test
   void deve_reconhecer_declaracao_de_variavel_vetor() {
      Parser parser = new Parser("class Fruta { int[] tamanhos; }");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_de_lista_de_variavel_sem_ID() {
      Parser parser = new Parser("class Fruta { int tamanho, 123; }");

      assertThrows(BadVariableException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_lista_de_variavel_sem_ponto_e_virgula() {
      Parser parser = new Parser("class Fruta { int tamanho, peso }");

      assertThrows(BadVariableException.class, parser::program);
   }

   @Test
   void deve_reconhecer_declaracao_de_lista_de_variaveis() {
      Parser parser = new Parser("class Fruta { int tamanho, peso, cor; }");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_declaracao_de_mais_de_uma_variavel() {
      Parser parser = new Parser("class Fruta { int tamanho; string descricao; Tipos[] tipos; }");

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
      Parser parser = new Parser("class Fruta { constructor () {} }");

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_de_parametro_vetor_do_metodo_construtor_sem_RSBR() {
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

   @Test
   void deve_reconhecer_construtores_na_classe() {
      String codigo = "class Fruta { constructor(int batata){}" +
            "constructor(string batata){} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_de_metodo_da_classe_com_parametro_sem_tipo() {
      String codigo = "class Fruta { int fazer(123); }";
      Parser parser = new Parser(codigo);

      assertThrows(BadMethodBodyException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_metodo_da_classe_com_parametro_sem_ID() {
      String codigo = "class Fruta { int fazer(int 123); }";
      Parser parser = new Parser(codigo);

      assertThrows(BadParamException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_metodo_da_classe_com_parametro_vetor_sem_ID() {
      String codigo = "class Fruta { int fazer(int []123); }";
      Parser parser = new Parser(codigo);

      assertThrows(BadParamException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_metodo_da_classe_sem_RPAREN() {
      String codigo = "class Fruta { int fazer(int[] vetor; }";
      Parser parser = new Parser(codigo);

      assertThrows(BadMethodBodyException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_metodo_da_classe_sem_LCBR() {
      String codigo = "class Fruta { int fazer(int[] vetor); }";
      Parser parser = new Parser(codigo);

      assertThrows(BadMethodBodyException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_de_metodo_da_classe_sem_RCBR() {
      String codigo = "class Fruta { int fazer(int[] vetor){";
      Parser parser = new Parser(codigo);

      assertThrows(BadMethodBodyException.class, parser::program);
   }

   @Test
   void deve_reconhecer_classe_com_metodo_declarado() {
      String codigo = "class Fruta { int fazer(int[] vetor){} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_classe_com_metodos_declarados() {
      String codigo = "class Fruta { int metodo(string teste){}" +
            "string metodoComSring(string palavra){}" +
            "Classe metodoComClasse(Classe classe){} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_classe_com_variaveis_e_construtor() {
      String codigo = "class Fruta { int tamanho, peso;" +
            "constructor(int[] tamanho, int[] peso) {} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_classe_com_variaveis_e_construtor_e_metodos() {
      String codigo = "class Fruta {int tamanho;" +
            "constructor(int tamanho){}" +
            "int metodo(string teste){} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_classe_com_variaveis_e_metodos() {
      String codigo = "class Fruta {int tamanho;" +
            "int metodo(string teste){} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_classe_com_construtor_e_metodos() {
      String codigo = "class Fruta { constructor(int tamanho){}" +
            "int metodo(string teste){} }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_corpo_de_metodo_com_statement_ponto_e_virgula() {
      String codigo = "class Fruta { int metodo(string teste) { ; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_corpo_de_metodo_com_statement_break_sem_ponto_e_virgula() {
      String codigo = "class Fruta { int metodo(string teste) { break } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadStatementException.class, parser::program);
   }

   @Test
   void deve_reconhecer_corpo_de_metodo_com_statement_break() {
      String codigo = "class Fruta { int metodo(string teste) { break; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_corpo_de_metodo_com_statement_print_sem_ponto_e_virgula() {
      String codigo = "class Fruta { int metodo(string teste) { print +5 } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadStatementException.class, parser::program);
   }

   @Test
   void deve_reconhecer_corpo_de_metodo_com_statement_print() {
      String codigo = "class Fruta { int metodo(string teste) { print +5; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_corpo_de_metodo_com_statement_read_sem_ponto_e_virgula() {
      String codigo = "class Fruta { int metodo(string teste) { read +5} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadStatementException.class, parser::program);
   }

   @Test
   void deve_reconhecer_corpo_de_metodo_com_statement_read() {
      String codigo = "class Fruta { int metodo(string teste) { read +5; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_corpo_de_metodo_com_statement_return_sem_ponto_e_virgula() {
      String codigo = "class Fruta { int metodo(string teste) { return +5} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadStatementException.class, parser::program);
   }

   @Test
   void deve_reconhecer_corpo_de_metodo_com_statement_return() {
      String codigo = "class Fruta { int metodo(string teste) { return +5; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_corpo_de_metodo_com_statement_super_sem_LPAREN() {
      String codigo = "class Fruta { int metodo(string teste) { super } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadSuperStatException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_corpo_de_metodo_com_statement_super_sem_RPAREN() {
      String codigo = "class Fruta { int metodo(string teste) { super (} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadSuperStatException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_corpo_de_metodo_com_statement_super_sem_ponto_e_virgula() {
      String codigo = "class Fruta { int metodo(string teste) { super() } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadStatementException.class, parser::program);
   }

   @Test
   void deve_reconhecer_corpo_de_metodo_com_statement_super() {
      String codigo = "class Fruta { int metodo(string teste) { super(); } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_corpo_de_metodo_com_lista_de_variaveis() {
      String codigo = "class Frtuta { int metodo(string teste) {" +
            "int var, go, tam;" +
            "int[] size, color;" +
            "string descricao; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_lValue_sem_expression() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata[ = +5 } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadUnaryExpressionException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_lValue_sem_RSBR() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata[+5 = +5 } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadLeftValueException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_lValue_com_ponto_sem_ID() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata[+5]. = +5 } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadLeftValueException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_atrib_stat_sem_ATTR() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata[+5] +5 } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadAtribStatException.class, parser::program);
   }

   @Test
   void deve_reconhecer_statement_atrib_com_expression() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata[+5] = +5 } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_alloc_new_sem_ID() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata = new () } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadAllocExpressionException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_alloc_new_sem_LPAREN() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata = new batata) } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadAllocExpressionException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_alloc_new_sem_RPAREN() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata = new batata( } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadAllocExpressionException.class, parser::program);
   }

   @Test
   void deve_reconhecer_alloc_new() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata = new batata() } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_alloc_type_sem_LSBR() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata = int ] } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadAllocExpressionException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_alloc_type_sem_expression() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata = int [] } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadUnaryExpressionException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_alloc_type_sem_RSBR() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata = int [+5 } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadAllocExpressionException.class, parser::program);
   }

   @Test
   void deve_reconhecer_alloc_type() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata = int [+5] } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_if_sem_LPAREN() {
      String codigo = "class Fruta { int metodo(string teste) { if state } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadIfException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_variavel_em_statement_sem_ID() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "int 123; } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadVariableException.class, parser::program);
   }

   @Test
   void deve_reconhecer_variavel_em_statement() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "int batata; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_variavel_comecando_com_ID_num_statement() {
      String codigo = "class Fruta { int metodo(string teste) {" +
            "batata batata; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_if_sem_expression() {
      String codigo = "class Fruta { int metodo(string teste) { if () } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadUnaryExpressionException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_if_sem_RPAREN() {
      String codigo = "class Fruta { int metodo(string teste) { if (+3 } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadIfException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_if_sem_LCBR() {
      String codigo = "class Fruta { int metodo(string teste) { if (+3) } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadIfException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_if_sem_statements() {
      String codigo = "class Fruta { int metodo(string teste) { if (+3) { } } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadStatementException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_if_sem_RCBR() {
      String codigo = "class Fruta { int metodo(string teste) { if (+3) { ; ";
      Parser parser = new Parser(codigo);

      assertThrows(BadIfException.class, parser::program);
   }

   @Test
   void deve_reconhecer_if() {
      String codigo = "class Fruta { int metodo(string teste) { if (+3) {;} } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_if_else_sem_LCBR() {
      String codigo = "class Fruta { int metodo(string teste) { if (+3) {;} else } ";
      Parser parser = new Parser(codigo);

      assertThrows(BadIfException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_if_else_sem_statements() {
      String codigo = "class Fruta { int metodo(string teste) { if (+3) {;} else { } } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadStatementException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_if_else_sem_RCBR() {
      String codigo = "class Fruta { int metodo(string teste) { if (+3) {;} else { ;";
      Parser parser = new Parser(codigo);

      assertThrows(BadIfException.class, parser::program);
   }

   @Test
   void deve_reconhecer_if_else() {
      String codigo = "class Fruta { int metodo(string teste) { if (+3) {;} else {;} }}";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_for_sem_LPAREN() {
      String codigo = "class Fruta {int metodo(string teste) {for a } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadForException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_for_com_atrib_sem_ponto_e_virgula() {
      String codigo = "class Fruta {int metodo(string teste) {for (count = +1 } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadForException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_for_com_expression_sem_ponto_e_virgula() {
      String codigo = "class Fruta {int metodo(string teste) {for (; +1 } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadForException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_for_sem_RPAREN() {
      String codigo = "class Fruta {int metodo(string teste) {for (;; } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadForException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_for_sem_LCBR() {
      String codigo = "class Fruta {int metodo(string teste) {for (;;) } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadForException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_for_sem_statement() {
      String codigo = "class Fruta {int metodo(string teste) {for (;;){ } }";
      Parser parser = new Parser(codigo);

      assertThrows(BadStatementException.class, parser::program);
   }

   @Test
   void deve_identificar_erro_for_sem_RCBR() {
      String codigo = "class Fruta {int metodo(string teste) {for (;;) {;";
      Parser parser = new Parser(codigo);

      assertThrows(BadForException.class, parser::program);
   }

   @Test
   void deve_reconhecer_for_com_parenteses_vazios() {
      String codigo = "class Fruta {int metodo(string teste) {for (;;) {;} } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_for_com_primeiro_parametro_nos_parenteses() {
      String codigo = "class Fruta {int metodo(string teste) {for (batata = new Tuberculo();;) {;} } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_for_com_segunto_parametro_nos_parenteses() {
      String codigo = "class Fruta {int metodo(string teste) {for (;+5+-4;) {;} } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_for_com_terceiro_parametro_nos_parenteses() {
      String codigo = "class Fruta {int metodo(string teste) {for (;;batata = int[+5]) {;} } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_for_completo() {
      String codigo = "class Fruta {int metodo(string teste) {for (batata = new Tuberculo();+5+-4;batata = int[+5]) " +
            "{super();} } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_factor_inteiro() {
      String codigo = "class Fruta { int metodo(string teste) { print +5; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_reconhecer_factor_string_literal() {
      String codigo = "class Fruta { int metodo(string teste) { print +\"a\"; } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_fator_expression_sem_RPAREN() {
      String codigo = "class Fruta { int metodo(string teste) { print +(-5 ; }";
      Parser parser = new Parser(codigo);

      assertThrows(BadFactorException.class, parser::program);
   }

   @Test
   void deve_reconhecer_factor_expressao_entre_parenteses() {
      String codigo = "class Fruta { int metodo(string teste) { print +(-5); } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }

   @Test
   void deve_identificar_erro_unary_expression_sem_sinal() {
      String codigo = "class Fruta { int metodo(string teste) { print 5} }";
      Parser parser = new Parser(codigo);

      assertThrows(BadUnaryExpressionException.class, parser::program);
   }

   @Test
   void deve_reconhecer_statement_super_com_lista_de_expressoes() {
      String codigo = "class Fruta { int metodo(string teste) { super(+3, +3); } }";
      Parser parser = new Parser(codigo);

      String msgRetornada = parser.program();

      assertEquals(msgAnaliseConcluida, msgRetornada);
   }
}
